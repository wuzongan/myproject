using UnityEngine;
using System.Collections;

public class GameLoad : MonoBehaviour {

	void Start()
	{
//		DontDestroyOnLoad(this.gameObject);

		StartGame();


	}

	void OnDestroy()
	{
	}


	void StartGame()
	{
		this.gameObject.AddComponent<SkillData>();
		this.gameObject.AddComponent<SkillManager>();
		this.gameObject.AddComponent<SkillIconHandle>();
	}
}
