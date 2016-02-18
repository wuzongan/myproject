using UnityEngine;
using System.Collections;

public abstract class AbstractBarrage : MonoBehaviour
{
	public GameObject shot;
	public float speed;

	public void Fire()
	{
		StartCoroutine(InternalFire());
	}
	protected abstract IEnumerator InternalFire();
}
