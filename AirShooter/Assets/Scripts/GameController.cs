using UnityEngine;
using System.Collections;
using UnityEngine.SceneManagement;
using UnityEngine.UI;

public class GameController : MonoBehaviour
{
	
	private Text scoreText;
	private Button restart;
	private Text gameOverText;

	public bool gameOver;
	protected int score;

	public ArrayList targetList;

	private int killEmemyNum;

	private readonly int KILL_NUM = 30;


	private static GameController mInstance;
	public static GameController Instance
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

	// Use this for initialization
	void Start ()
	{
		gameOver = false;
		restart = transform.Find("restart").GetComponent<Button>();
		restart.onClick.AddListener(delegate() {
			SceneManager.LoadScene(SceneManager.GetActiveScene().name);
		});
		restart.gameObject.SetActive (false);
		gameOverText = transform.Find("game_over").GetComponent<Text>();
		scoreText = transform.Find("score").GetComponent<Text>();
		gameOverText.text = "";
		scoreText.text = "";
		targetList = new ArrayList ();
	}

	// Update is called once per frame
	void Update ()
	{
		if(killEmemyNum > KILL_NUM){
			killEmemyNum = 0;
			AIManager.Instance.transition ();
		}
		if (gameOver)
		{
			restart.gameObject.SetActive (true);
		}
	}

	public void AddScore (int newScoreValue)
	{
		score += newScoreValue;
		UpdateScore ();
	}		

	protected void UpdateScore ()
	{
		scoreText.text = "Score: " + score;
	}

	public int getScore(){
		return score;
	}

	public void GameOver ()
	{
		gameOverText.text = "Game Over!";
		gameOver = true;
	}

	public void GameWin ()
	{
		gameOverText.text = "You Win!";
		gameOver = true;
	}

	public void DestroyTarget(GameObject gameObject)
	{
		if (gameObject != null) {
			if (gameObject.tag == "Enemy") {
				killEmemyNum++;
				targetList.Remove (gameObject);
			}
			SkillManager.Instance.removeSkillByObj (gameObject);
			Destroy (gameObject);
		}
	}
}
