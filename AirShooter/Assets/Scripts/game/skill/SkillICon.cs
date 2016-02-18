using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using UnityEngine.Events;
using UnityEngine.UI;

public class SkillICon : MonoBehaviour {
	public skillSprite sprite;

	void Start(){
		
	}

	void Update(){
		if (sprite.isPressDown)  
		{
			sprite.timerMask.enabled = true;
			sprite.timerMask.fillAmount -= (Time.deltaTime / sprite.cdTime);
		}
	}


	[System.Serializable]
	public struct skillSprite
	{
		[HideInInspector]
		public int skillId;
		[HideInInspector]
		public float cdTime;
		public Image timerMask;
		[HideInInspector]
		public bool isPressDown;   //是否使用了技能
	}
}
