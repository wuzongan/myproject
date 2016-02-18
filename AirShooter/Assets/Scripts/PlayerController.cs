using UnityEngine;
using System.Collections;

[System.Serializable]
public class Boundary 
{
	public float xMin, xMax, zMin, zMax;
}

public class PlayerController : CraftController
{
	public Texture2D hp_bg;
	public Texture2D hp_fg;
	public float tilt;
	public Boundary boundary;

//	public GameObject shot;
	public Transform shotSpawn;
	public float fireRate;

	public Transform missileSpawn;
//	public GameObject missile;
	public float missileTime;
	public float missileNum;
	public float missileOffX;
	private float nextMissile;
	private bool b_first;

	private float nextFire;

	public float speed;


	void OnGUI()
	{
		GUI.DrawTexture(new Rect((Screen.width - 128) / 2, Screen.height - 24, 128, 16), hp_bg);
		GUI.DrawTexture(new Rect((Screen.width - 128) / 2, Screen.height - 24, 128 * currentHp / hp, 16), hp_fg);
	}

	void Update ()
	{
		StartCoroutine (OnMouseDown());
		if (AIManager.Instance.bossStatus == BattleStatus.boss_warning)
			return;

		if (Time.timeSinceLevelLoad > nextFire) 
		{
			nextFire = Time.timeSinceLevelLoad + fireRate;
//			Instantiate(shot, shotSpawn.position, transform.rotation);
			SkillManager.Instance.playSkill (1, shotSpawn.position, transform.rotation);
			GetComponent<AudioSource>().Play ();
		}

		if (Time.timeSinceLevelLoad > nextMissile) 
		{
			nextMissile = Time.timeSinceLevelLoad + missileTime;
			if(b_first) {
				for(int i = 0; i < missileNum; i++){
					float offX = Random.Range(-missileOffX,missileOffX);
//					Instantiate(missile, new Vector3(missileSpawn.transform.position.x+offX, missileSpawn.transform.position.y, missileSpawn.transform.position.z), Quaternion.identity);
					SkillManager.Instance.playSkill (2, new Vector3(missileSpawn.transform.position.x+offX, missileSpawn.transform.position.y, missileSpawn.transform.position.z),Quaternion.identity);
				}
//				GetComponent<AudioSource>().Play ();
			}
			b_first = true;
		}
	}


	IEnumerator OnMouseDown()  
	{  
		Vector3 ScreenSpace = Camera.main.WorldToScreenPoint(transform.position);  

		Vector3 offset = transform.position-Camera.main.ScreenToWorldPoint(new Vector3(Input.mousePosition.x,Input.mousePosition.y,ScreenSpace.z));  

		//当鼠标左键按下时  
		while(Input.GetMouseButton(0))  
		{  
			//得到现在鼠标的2维坐标系位置  
			Vector3 curScreenSpace =  new Vector3(Input.mousePosition.x,Input.mousePosition.y,ScreenSpace.z);     
			//将当前鼠标的2维位置转化成三维的位置，再加上鼠标的移动量  
			Vector3 CurPosition = Camera.main.ScreenToWorldPoint(curScreenSpace)+offset;          
			//CurPosition就是物体应该的移动向量赋给transform的position属性        
			transform.position = new Vector3
			(
				Mathf.Clamp (CurPosition.x, boundary.xMin, boundary.xMax), 
				0.0f, 
				Mathf.Clamp (CurPosition.z, boundary.zMin, boundary.zMax)
			);   
			yield return new WaitForFixedUpdate();  
		}  


	}
}
