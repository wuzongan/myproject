using UnityEngine;
using System.Collections;

public class BarrageRing: AbstractBarrage
{
	public int boltNum;

	protected override IEnumerator InternalFire()
	{
		float dangle = 360f / boltNum;
		float angle = 0;
		for (int i = 0; i < boltNum; i++)
		{
			SkillManager.Instance.playSkill (9, transform.parent.position, Quaternion.Euler(new Vector3(0, angle, 0)));
			angle += dangle;
			yield return new WaitForSeconds(0.05f);
		}
		GetComponent<AudioSource>().Play();
	}
}
