using UnityEngine;
using System.Collections;

public class BarrageCross : AbstractBarrage
{
	protected override IEnumerator InternalFire()
	{
		SkillManager.Instance.playSkill (9, transform.parent.position, transform.parent.rotation);
		GetComponent<AudioSource>().Play();
		yield return null;
	}
}
