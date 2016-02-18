using UnityEngine;
using System.Collections;
using System.Collections.Generic;

public class AIManager : MonoBehaviour {

	public GameObject[] hazards;
	public Vector3 spawnValues;
	public int hazardCount;
	public float spawnWait;
	public float waveWait;

	public GameObject boss;
	public Vector3 spawnPosition;

	public Texture warning;
	public float warningTime;
	private float enterWarningTime;

	public BattleStatus bossStatus = BattleStatus.none;

	private static AIManager mInstance;
	public static AIManager Instance
	{
		get
		{
			return mInstance;
		}
	}

	void Awake()
	{
		mInstance = this;
	}

	void Start(){
		StartCoroutine(SpawnEnemy());
	}
		

	void OnGUI()
	{
		if (bossStatus == BattleStatus.boss_warning && ((int)(Time.timeSinceLevelLoad * 2)) % 2 == 1)
			GUI.DrawTexture(new Rect((Screen.width - 256) / 2, 10, 256, 128), warning);
	}

	void Update ()
	{
		switch(bossStatus){
		case BattleStatus.none:
			bossStatus = BattleStatus.enemy_activate;
			StartCoroutine (SpawnEnemy ());
			break;
		case BattleStatus.enemy_activate:
			break;
		case BattleStatus.transition:
			if (GameController.Instance.targetList.Count == 0) {
				bossStatus = BattleStatus.boss_warning;
				enterWarningTime = Time.timeSinceLevelLoad;
			}
			break;
		case BattleStatus.boss_warning:
			if (Time.timeSinceLevelLoad - enterWarningTime > warningTime) {
				SpawnBoss();
				bossStatus = BattleStatus.boss_activate;
			}
			break;
		case BattleStatus.boss_activate:
			if (GameController.Instance.targetList.Count == 0) {
				bossStatus = BattleStatus.none;
			}
			break;
		}
	}
		

	IEnumerator SpawnEnemy(){
		while (bossStatus == BattleStatus.enemy_activate)
		{
			yield return new WaitForSeconds(2);
			for (int i = 0; i < hazardCount; i++)
			{
				if (GameController.Instance.gameOver)
					break;

				GameObject hazard = hazards [Random.Range (0, hazards.Length)];
				Vector3 spawnPosition = new Vector3 (Random.Range (-spawnValues.x, spawnValues.x), spawnValues.y, spawnValues.z);
				GameObject enemy = (GameObject) Instantiate (hazard, spawnPosition, hazard.transform.rotation);
				GameController.Instance.targetList.Add(enemy);
				yield return new WaitForSeconds (spawnWait);
			}

			if (GameController.Instance.gameOver)
			{
				break;
			}
			else
			{
				yield return new WaitForSeconds(waveWait);
			}
		}
	}

	public void transition(){
		bossStatus = BattleStatus.transition;
	}

	public void SpawnBoss()
	{
		GameObject enemy = (GameObject) Instantiate (boss, spawnPosition, boss.transform.rotation);
		GameController.Instance.targetList.Add(enemy);

		//定时触发被动技能
		List<Skill> skillList_passive = SkillData.Instance.getPlayerSkillListByType (ReleaseType.passive);
		for (int i = 0; i < skillList_passive.Count; i++) {
			StartCoroutine(triggerPassiveSkill(skillList_passive[i], enemy));  
		}
	}

	IEnumerator triggerPassiveSkill(Skill skill, GameObject m_boss){
		yield return new WaitForSeconds(skill.coolTime);
		while (m_boss.gameObject != null && skill != null) {
			if (m_boss.gameObject != null && skill != null) {
				float ran = Random.Range (0, 1.0f);
				if (ran > skill.triggerProb) {
					SkillManager.Instance.playSkill (skill.id, m_boss.gameObject.transform);
				}
			}
			yield return new WaitForSeconds(skill.coolTime);
		}

	}
}

public enum BattleStatus{
	none = 0,
	enemy_activate,
	transition,
	boss_warning,
	boss_activate,
}
