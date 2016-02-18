using UnityEngine;
using System.Collections;

public class CraftController : MonoBehaviour
{
	public int scoreValue;
	public int hp = 1;
	[HideInInspector]
	public int currentHp;

	protected void Start()
	{
		currentHp = hp;
	}
	
	public void DamagedBy(GameObject other)
	{
		Skill skill = SkillManager.Instance.getSkillByObj (other);

		if (!DamageHandle(other, skill))
			return;
		if (skill == null) {
			skill = SkillData.Instance.getSkillById (100);
		}
		currentHp -= skill.harm;
		if (currentHp <= 0){
			GameObject explosion = GetComponent<DestroyByContact>().explosion;
			if (explosion != null)
			{
				Instantiate(explosion, transform.position, transform.rotation);
			}

			if (this.tag == "Player")
			{
				GameController.Instance.GameOver();
			}
			else
			{
				GameController.Instance.AddScore(scoreValue);
			}
			GameController.Instance.DestroyTarget(gameObject);


		}
		if (other.tag != "Player" && other.tag != "Enemy" && skill.durationTime == 0) {
			Instantiate(Resources.Load("Prefabs/explosion_bolt", typeof(GameObject)), transform.position, transform.rotation);
			GameController.Instance.DestroyTarget (other);
		}
	}

	private bool DamageHandle(GameObject other, Skill skill){
		if ((other.tag == "Enemy" && this.tag == "Player") || (other.tag == "Player" && this.tag == "Enemy")) {
			return true;
		} else if(skill != null){
			SkillType st = SkillType.hero;
			if (((SkillType)skill.type == SkillType.hero && this.tag == "Enemy") ||
				((SkillType)skill.type == SkillType.enemy && this.tag == "Player") ) {
				if (this.tag == "Enemy") {
					st = SkillType.enemy;
				}
				if (!SkillManager.Instance.hasInvincible (st)) {
					return true;
				}
			}
		}
		return false;
	} 


}
