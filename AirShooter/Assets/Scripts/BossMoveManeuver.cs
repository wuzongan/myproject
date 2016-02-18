using UnityEngine;
using System.Collections;

public class BossMoveManeuver : MoveManeuver
{
	public Vector3[] bossPhaseTwoVertex;
	public Vector3[] bossPhaseThreeVertex;
	private int moveVertexIndex;

	private enum MovePhase {
		PhaseOne,
		PhaseTwo,
		PhaseThree,
		PhaseFour
	};
	private MovePhase movePhase;

	// Use this for initialization
	new void Start()
	{
		base.Start();
		movePhase = MovePhase.PhaseOne;
	}
	
	// Update is called once per frame
	void Update()
	{
		if (movePhase == MovePhase.PhaseOne)
		{
			if (transform.position.z <= 12)
			{
				if (GetComponent<CraftController>().currentHp * 100 / GetComponent<CraftController>().hp >= 80)
				{
					movePhase = MovePhase.PhaseTwo;
				}
				else
				{
					movePhase = MovePhase.PhaseThree;
				}
				GetComponent<Rigidbody>().velocity = Vector3.zero;
				moveVertexIndex = 0;
			}
		}
		else if (movePhase == MovePhase.PhaseTwo)
		{
			if (GetComponent<CraftController>().currentHp * 100 / GetComponent<CraftController>().hp <= 80)
			{
				movePhase = MovePhase.PhaseThree;
				moveVertexIndex = 0;
			}
		}
	}

	void FixedUpdate()
	{
//		Rigidbody rigidbody = GetComponent<Rigidbody>();

		if (movePhase == MovePhase.PhaseOne)
		{
		}
		else if (movePhase == MovePhase.PhaseTwo)
		{
			transform.position = Vector3.MoveTowards(transform.position, bossPhaseTwoVertex[moveVertexIndex], speed * Time.deltaTime);
			if (transform.position == bossPhaseTwoVertex[moveVertexIndex])
				moveVertexIndex = (moveVertexIndex + 1) % bossPhaseTwoVertex.Length;
		}
		else if (movePhase == MovePhase.PhaseThree)
		{
			transform.position = Vector3.MoveTowards(transform.position, bossPhaseThreeVertex[moveVertexIndex], speed * Time.deltaTime);
			if (transform.position == bossPhaseThreeVertex[moveVertexIndex])
				moveVertexIndex = (moveVertexIndex + 1) % bossPhaseThreeVertex.Length;
		}
	}
}
