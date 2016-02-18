using UnityEngine;
using System.Collections;

public class MoveManeuver : MonoBehaviour
{
	public float speed = 10;

	protected void Start ()
	{
		GetComponent<Rigidbody>().velocity = transform.forward * speed;
	}
}
