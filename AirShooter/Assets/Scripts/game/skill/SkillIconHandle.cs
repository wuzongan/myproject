using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.Events;
using UnityEngine.UI;

public class SkillIconHandle : MonoBehaviour {


	// Use this for initialization
	void Start () {
		List<Skill> skillList = SkillData.Instance.getPlayerSkillListByType (ReleaseType.initiative);
		Skill skill;
		GameObject skill_icon = GameObject.Find ("skill_icon");
		for(int i = 0; i < skillList.Count; i++){
			GameObject obj = Instantiate (Resources.Load ("Prefabs/skillIcon")) as GameObject;
			obj.transform.SetParent (skill_icon.transform);
//			obj.transform.parent = skill_icon.transform;
			obj.transform.localPosition = new Vector3(0,-180*i,0);
			obj.transform.localEulerAngles = Vector3.zero;
			obj.transform.localScale = Vector3.one;

			skill = skillList [i];
			Button btn = obj.GetComponent<Button>();
			Sprite sprite = Resources.Load("Images/"+skill.iconName, typeof(Sprite)) as Sprite;
			btn.image.sprite = sprite;
			SkillICon icon = btn.GetComponent<SkillICon> ();
			icon.sprite.cdTime = skill.coolTime;
			icon.sprite.skillId = skill.id;
			btn.image.sprite = sprite;
			btn.onClick.AddListener(delegate() {
				this.OnClick(obj); 
			});
		}
	}

	public void OnClick(GameObject sender)
	{
		GameObject player = GameObject.FindGameObjectWithTag("Player");
		SkillICon icon = sender.GetComponent<SkillICon> ();

		if(icon.sprite.timerMask.fillAmount == 0){
			icon.sprite.isPressDown = true; 
			icon.sprite.timerMask.fillAmount = 1f;
			SkillManager.Instance.playSkill (icon.sprite.skillId, player.gameObject.transform);
		}
	}
	
	// Update is called once per frame
	void Update () {
		
	}
}
