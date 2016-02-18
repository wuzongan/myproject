using UnityEngine;
using System.Collections;

public class BarrageFan : AbstractBarrage
{
	public float startAngle;
	public float endAngle;
	public int boltNum;

	protected override IEnumerator InternalFire()
	{
		float dangle = Mathf.Abs(endAngle - startAngle) / boltNum;
		float angle = startAngle;
		for (int i = 0; i <= boltNum; i++)
		{
			SkillManager.Instance.playSkill (9, transform.parent.position, Quaternion.Euler(new Vector3(0, angle, 0)));
			angle += dangle;
		}
		GetComponent<AudioSource>().Play();

		yield return null;
	}
}
