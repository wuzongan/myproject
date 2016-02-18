using UnityEngine;
using System.Collections;

public class Skill
{
	public int id{ get; set;}

	public string name{ get; set;}

	public int type{ get; set;}

	public int effectType{ get; set;}

	public int releaseType{ get; set;}

	public int harm{ get; set;}

	public float coolTime{ get; set;}

	public float durationTime{ get; set;}

	public float triggerProb{ get; set;}

	public string prefabName{ get; set;}

	public string iconName{ get; set;}

	public float nextPlayTime{ get; set;}

	public bool isFollow{ get; set;}

	public Skill(int m_id,string m_name,int m_harm,float m_coolTime = 0,float m_triggerProb = 0, 
		string m_prefabName = "", float m_durationTime = 0, bool m_isFollow = false,
		int m_releaseType = (int)ReleaseType.general, string m_iconName = "",
		int m_type = (int)SkillType.hero, int m_effectType = (int)SkillEffectType.Default){
		id = m_id;
		name = m_name;
		harm = m_harm;
		coolTime = m_coolTime;
		triggerProb = m_triggerProb;
		type = m_type;
		effectType = m_effectType;
		prefabName = m_prefabName;
		durationTime = m_durationTime;
		isFollow = m_isFollow;
		releaseType = m_releaseType;
		iconName = m_iconName;
	}
		
}

