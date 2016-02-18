using UnityEngine;
using System.Reflection;
using System.Collections;

public class WeaponController : MonoBehaviour
{
	public float fireRate;
	public float delay;

	void Start ()
	{
		InvokeRepeating("Fire", delay, fireRate);
	}

	void Fire ()
	{
		GetComponent<BossController>().Fire();
	}
}
