using UnityEngine;
using System.Collections;

public class EnemyMoveManeuver : MoveManeuver
{
	public Boundary boundary;
	public float tilt;
	public float dodge;
	public float smoothing;
	public Vector2 startWait;
	public Vector2 maneuverTime;
	public Vector2 maneuverWait;

	private float currentSpeed;
	private float targetManeuver;

	new void Start()
	{
		base.Start();
		currentSpeed = GetComponent<Rigidbody>().velocity.z;
		StartCoroutine(Evade());
	}

	IEnumerator Evade ()
	{
		yield return new WaitForSeconds (Random.Range (startWait.x, startWait.y));
		while (true)
		{
			targetManeuver = Random.Range (1, dodge) * -Mathf.Sign (transform.position.x);
			yield return new WaitForSeconds (Random.Range (maneuverTime.x, maneuverTime.y));
			targetManeuver = 0;
			yield return new WaitForSeconds (Random.Range (maneuverWait.x, maneuverWait.y));
		}
	}

	void FixedUpdate ()
	{
		float newManeuver = Mathf.MoveTowards (GetComponent<Rigidbody>().velocity.x, targetManeuver, smoothing * Time.deltaTime);
		GetComponent<Rigidbody>().velocity = new Vector3 (newManeuver, 0.0f, currentSpeed);
		transform.position = new Vector3
			(
				Mathf.Clamp(transform.position.x, boundary.xMin, boundary.xMax), 
				0.0f, 
				Mathf.Clamp(transform.position.z, boundary.zMin, boundary.zMax)
			);

		Vector3 euler = transform.rotation.eulerAngles;
		transform.rotation = Quaternion.Euler(euler.x, euler.y, GetComponent<Rigidbody>().velocity.x * -tilt);
	}
}

