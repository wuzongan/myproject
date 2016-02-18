using UnityEngine;
using System.Collections;

public class BossController : CraftController
{
	public Texture2D hp_bg;
	public Texture2D hp_fg;

	[System.Serializable]
	public class MovePhase
	{
		public enum PhaseName
		{
			PhaseOne,
			PhaseTwo,
			PhaseThree,
			PhaseFour,
			PhaseCount
		};
		public enum JumpOutCondition
		{
			Position,
			HpPercent,
		};
		public PhaseName name;
		public JumpOutCondition condition;

		public Vector3 position;
		[Range(0, 100)]
		public int hpPercent;

		public Vector3[] vertexes = new Vector3[0];
		public float moveSpeed = 5;
		public GameObject[] barrages = new GameObject[0];
		public float fireRate = 1.5f;
		public float fireDelay = 0.5f;

		private int currentMoveVertex;
		private int currentBarrage;

		public bool isJumpOut(BossController controller)
		{
			if (condition == JumpOutCondition.Position && controller.transform.position == position ||
				condition == JumpOutCondition.HpPercent && controller.currentHp * 100 / controller.hp < hpPercent)
				return true;
			return false;
		}

		public void Start(BossController controller)
		{
			foreach (GameObject obj in controller.barrageEmitterList)
			{
				Destroy(obj);
			}
			controller.barrageEmitterList.Clear();

			foreach (GameObject obj in barrages)
			{
				GameObject emitter = Instantiate(obj, Vector3.zero, Quaternion.identity) as GameObject;
				emitter.transform.parent = controller.transform;

				controller.barrageEmitterList.Add(emitter);
			}

			currentMoveVertex = 0;
			currentBarrage = 0;
		}

		public void MoveTowardTarget(BossController controller)
		{
			Vector3 targetPosition = controller.transform.position;
			if (condition == JumpOutCondition.Position)
				targetPosition = position;
			else if (vertexes.Length > 0)
				targetPosition = vertexes[currentMoveVertex];

			controller.transform.position = Vector3.MoveTowards(controller.transform.position, targetPosition, moveSpeed * Time.deltaTime);

			if (vertexes.Length > 0 && controller.transform.position == targetPosition)
				currentMoveVertex = (currentMoveVertex + 1) % vertexes.Length;
		}

		public void Fire(BossController controller)
		{
			if (controller.barrageEmitterList.Count == 0)
				return;
			
			((GameObject) controller.barrageEmitterList[currentBarrage]).GetComponent<AbstractBarrage>().Fire();
			currentBarrage = (currentBarrage + 1) % controller.barrageEmitterList.Count;
		}
	}

	public MovePhase[] movePhases = new MovePhase[0];
	private MovePhase.PhaseName currentMovePhase;
	private ArrayList barrageEmitterList;

	[HideInInspector]
	public bool[] movePhaseFold = new bool[0];

	protected new void Start()
	{
		base.Start();
		barrageEmitterList = new ArrayList();

		EnterPhase(MovePhase.PhaseName.PhaseOne);
	}

	void OnGUI()
	{
		GUI.DrawTexture(new Rect((Screen.width - 128) / 2, 16, 128, 16), hp_bg);
		GUI.DrawTexture(new Rect((Screen.width - 128) / 2, 16, 128 * currentHp / hp, 16), hp_fg);
	}

	void Update()
	{
		while (movePhases[(int) currentMovePhase].isJumpOut(this))
			EnterPhase(currentMovePhase + 1);
	}

	void EnterPhase(MovePhase.PhaseName name)
	{
		if (currentMovePhase == name)
			return;

		CancelInvoke("Fire");
		currentMovePhase = name;
		movePhases[(int) currentMovePhase].Start(this);
		InvokeRepeating("Fire", movePhases[(int) currentMovePhase].fireDelay, movePhases[(int) currentMovePhase].fireRate);
	}

	void FixedUpdate()
	{
		movePhases[(int) currentMovePhase].MoveTowardTarget(this);
	}

	public void Fire()
	{
		movePhases[(int) currentMovePhase].Fire(this);
	}
}

