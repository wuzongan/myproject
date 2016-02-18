using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class SkillData : MonoBehaviour
{
	private List<Skill> skillList = new List<Skill> ();
	public readonly string Bolt = "Bolt";
	public readonly string BoltEnemy = "Bolt-Enemy";
	public readonly string Missile = "missile";
	public readonly string Afterburner = "Afterburner";
	public readonly string invincible = "invincible";

	private static SkillData mInstance;
	public static SkillData Instance
	{
		get
		{
			return mInstance;
		}
	}

	void Awake()
	{
		mInstance = this;
		initSkillData ("");
	}

	void Start(){
		
	}

	public void initSkillData(string json){
		skillList.Add( new Skill (1,"普通攻击",100,0,1,Bolt) );
		skillList.Add( new Skill (2,"普通攻击",200,0,1,Missile) );

		skillList.Add( new Skill (6,"主角被动技能1",2400,10f,0.5f,invincible,1.0f,true,1) );
		skillList.Add( new Skill (7,"主角主动技能1",1500,5f,1,Afterburner,1.0f,true,2,"buff_9931") );
		skillList.Add( new Skill (8,"主角主动技能2",0,5f,1,invincible,2.0f,true,2,"buff_9921",0,1) );

		skillList.Add( new Skill (9,"boss普通攻击",120,2,1,BoltEnemy,0,false,0,"",1) );

		skillList.Add( new Skill (100,"碰撞的伤害",100,0,1) );

	}

	public List<Skill> getSkillListByHero(){
		List<Skill> list = new List<Skill>();
		foreach(Skill skill in skillList){
			if((SkillType)skill.type == SkillType.hero){
				list.Add (skill);
			}
		}
		return list;
	}

	public List<Skill> getPlayerSkillListByType(ReleaseType type){
		List<Skill> list = new List<Skill>();
		foreach(Skill skill in skillList){
			if((SkillType)skill.type == SkillType.hero && (ReleaseType)skill.releaseType == type){
				list.Add (skill);
			}
		}
		return list;
	}
		
	public Skill getSkillById(int id){
		Skill skill = null;
		foreach(Skill sk in skillList){
			if(sk.id == id){
				skill = sk;
				break;
			}
		}
		return skill;
	}



}

public enum SkillType
{
	//主角
	hero = 0,
	//敌方
	enemy = 1
}

public enum ReleaseType
{
	//普通攻击
	general = 0,
	//被动技能
	passive = 1,
	//主动技能
	initiative = 2
}

public enum SkillEffectType
{
	//默认
	Default = 0,
	//无敌
	invincible = 1
}



