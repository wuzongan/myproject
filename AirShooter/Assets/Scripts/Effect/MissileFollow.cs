using UnityEngine;
using System.Collections;

public class MissileFollow : MonoBehaviour
{
	private GameObject[] Enemys;
	public float speed = 10;
	// Use this for initialization
	void Start () {
		Enemys = GameObject.FindGameObjectsWithTag ("Enemy");
	}
	
	// Update is called once per frame
	void Update () {
		transform.Translate(transform.forward.normalized*speed*Time.deltaTime,Space.World);
		if (Enemys.Length > 0) {
			StartCoroutine (AttackEnemys (Enemys[Random.Range(0,Enemys.Length)]));
		}
	}

	IEnumerator AttackEnemys(GameObject target){
		while (target) {
			transform.LookAt(target.transform.position);
			yield return null;
		}
		yield return null;
	}
	
}

