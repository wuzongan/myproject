using UnityEngine;
using System.Collections;

public class DestroyByContact : MonoBehaviour
{
	public GameObject explosion;

	void Start ()
	{
	}

	void OnTriggerEnter (Collider other)
	{
		CraftController controller = this.GetComponent<CraftController>();
		if (controller != null)
		{
			controller.DamagedBy(other.gameObject);
		}
		else
		{
			Debug.Log("Cannot find 'CraftController' script");
		}

	}
}