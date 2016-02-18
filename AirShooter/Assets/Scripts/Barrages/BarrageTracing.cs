using UnityEngine;
using System.Collections;

public class BarrageTracing : AbstractBarrage
{
	protected override IEnumerator InternalFire()
	{
		GameObject player = GameObject.FindGameObjectWithTag ("Player");
		if (player != null) {
			for (int i = 0; i < 3; i++) {
				if (player != null) {
					Vector3 targetDir = player.transform.position - transform.parent.position;
					Vector3 forward = transform.forward;
					float angle = Vector3.Angle (targetDir, forward);
					if (transform.parent.position.x - player.transform.position.x >= 0)
						angle = 360 - angle;
					SkillManager.Instance.playSkill (9, transform.parent.position, Quaternion.Euler (new Vector3 (0, angle, 0)));	
				}
			}
			yield return new WaitForSeconds (0.2f);
		}
		GetComponent<AudioSource> ().Play ();
	}
}
