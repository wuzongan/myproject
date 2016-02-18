using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class SkillManager : MonoBehaviour
{
	public Hashtable ht;

	private static SkillManager mInstance;
	public static SkillManager Instance
	{
		get
		{
			return mInstance;
		}
	}
		
	void Awake()
	{
		mInstance = this;

		ht = new Hashtable ();

	}
	// Use this for initialization
	void Start ()
	{
		
	}
	
	// Update is called once per frame
	void Update ()
	{
		
	}

	public GameObject playSkill(int skillId, Transform transform){
		return playSkill(skillId, transform.position, transform.rotation, transform);
	}

	public GameObject playSkill(int skillId, Vector3 startPos, Quaternion qua, Transform parent = null){
		Skill skill = SkillData.Instance.getSkillById (skillId);
		if (skill == null) {
			Debug.LogWarning ("skill is null");
			return null;
		}
		if ((SkillType)skill.type == SkillType.hero && skill.coolTime > 0) {
			if (Time.timeSinceLevelLoad > skill.nextPlayTime) {
				skill.nextPlayTime = Time.timeSinceLevelLoad + skill.coolTime;
			} else {
				Debug.LogWarning ("Skill in cooling");
				return null;
			}
		}

		GameObject obj = Instantiate(Resources.Load("Prefabs/"+skill.prefabName, typeof(GameObject)), startPos, qua) as GameObject;
		if (!ht.Contains (obj)) {
			ht.Add (obj, skill);
		} else {
			Debug.LogWarning ("=========add Skill Warning!==========");
		}
		if(skill.isFollow && parent != null){
			obj.transform.SetParent(parent);
		}
		if (skill.durationTime > 0) {
			StartCoroutine(WaitDestroy(skill.durationTime, obj));  
		}
		return obj;
	}

	IEnumerator WaitDestroy(float waitTime, GameObject obj)  
	{  
		yield return new WaitForSeconds(waitTime);  
		GameController.Instance.DestroyTarget (obj);
	}  

	public Skill getSkillByObj(GameObject obj){
		if (ht.Contains (obj)) {
			return ht[obj] as Skill;
		}
		return null;
	}

	public bool hasInvincible(SkillType st){
		Skill skill;
		foreach (DictionaryEntry de in ht) {
			skill = (Skill)de.Value;
			if((SkillEffectType)skill.effectType == SkillEffectType.invincible 
				&& st == (SkillType)skill.type){
				return true;
			}
		}
		return false;
	}

	public void removeSkillByObj(GameObject obj){
		if (ht.Contains (obj)) {
			ht.Remove (obj);
		}
	}

}

