using UnityEngine;
using UnityEditor;
using System.Collections;

[CustomEditor(typeof(BossController))]
[CanEditMultipleObjects]
public class BossControllerEditor : Editor
{
	void OnEnable()
	{
		Renew((BossController) target, ((BossController) target).movePhases.Length);
	}

	void Renew(BossController controller, int length)
	{
		bool[] mp = new bool[length];
		for (int i = 0; i < Mathf.Min(mp.Length, controller.movePhaseFold.Length); i++)
			mp[i] = controller.movePhaseFold[i];
		controller.movePhaseFold = mp;
	}

	override public void OnInspectorGUI()
	{
		BossController controller = (BossController) target;

		controller.scoreValue = EditorGUILayout.IntField(new GUIContent("Score Value"), controller.scoreValue);
		controller.hp = EditorGUILayout.IntField(new GUIContent("Hp"), controller.hp);
		controller.hp_bg = (Texture2D) EditorGUILayout.ObjectField(new GUIContent("Hp Bg"), controller.hp_bg, typeof(Texture2D), false);
		controller.hp_fg = (Texture2D) EditorGUILayout.ObjectField(new GUIContent("Hp Fg"), controller.hp_fg, typeof(Texture2D), false);

		int length = EditorGUILayout.IntField(new GUIContent("Move Phases"), controller.movePhases.Length);
		if (length > (int) BossController.MovePhase.PhaseName.PhaseCount)
			length = (int) BossController.MovePhase.PhaseName.PhaseCount;
		if (controller.movePhases.Length != length)
		{
			Renew(controller, length);
			BossController.MovePhase[] phases = new BossController.MovePhase[length];
			for (int i = 0; i < Mathf.Max(controller.movePhases.Length, length); i++)
			{
				if (i >= controller.movePhases.Length)
					phases[i] = new BossController.MovePhase();
				else if (i < length)
					phases[i] = controller.movePhases[i];
			}
			controller.movePhases = phases;
		}
		for (int i = 0; i < controller.movePhases.Length; i++)
		{
			BossController.MovePhase movePhase = controller.movePhases[i];
			movePhase.name = (BossController.MovePhase.PhaseName) i;
			controller.movePhaseFold[i] = EditorGUILayout.Foldout(controller.movePhaseFold[i], movePhase.name.ToString());
			if (controller.movePhaseFold[i])
			{
				EditorGUI.indentLevel++;
				movePhase.condition = (BossController.MovePhase.JumpOutCondition)EditorGUILayout.EnumPopup("Jump Out Condition", (BossController.MovePhase.JumpOutCondition) movePhase.condition);
				if (movePhase.condition == BossController.MovePhase.JumpOutCondition.Position)
					movePhase.position = EditorGUILayout.Vector3Field("Target Position", movePhase.position);
				if (movePhase.condition == BossController.MovePhase.JumpOutCondition.HpPercent)
					movePhase.hpPercent = EditorGUILayout.IntField("Hp Percent", movePhase.hpPercent);
				EditorGUILayout.LabelField("----------------------------------------");

				// move vertexes
				if (movePhase.condition != BossController.MovePhase.JumpOutCondition.Position)
				{
					int vlength = EditorGUILayout.IntField(new GUIContent("Vertexes"), movePhase.vertexes.Length);
					if (movePhase.vertexes.Length != vlength)
					{
						Vector3[] vs = new Vector3[vlength];
						for (int j = 0; j < Mathf.Min(movePhase.vertexes.Length, vlength); j++)
							vs[j] = movePhase.vertexes[j];
						movePhase.vertexes = vs;
					}
					EditorGUI.indentLevel += 7;
					for (int j = 0; j < movePhase.vertexes.Length; j++)
						movePhase.vertexes[j] = EditorGUILayout.Vector3Field(GUIContent.none, movePhase.vertexes[j]);
					EditorGUI.indentLevel -= 7;
				}
				movePhase.moveSpeed = EditorGUILayout.FloatField(new GUIContent("Move Speed"), movePhase.moveSpeed);
				EditorGUILayout.LabelField("----------------------------------------");

				// barrages
				int blength = EditorGUILayout.IntField(new GUIContent("Barrages"), movePhase.barrages.Length);
				if (movePhase.barrages.Length != blength)
				{
					GameObject[] bs = new GameObject[blength];
					for (int j = 0; j < Mathf.Min(movePhase.barrages.Length, blength); j++)
						bs[j] = movePhase.barrages[j];
					movePhase.barrages = bs;
				}
				EditorGUI.indentLevel += 7;
				for (int j = 0; j < movePhase.barrages.Length; j++)
					movePhase.barrages[j] = (GameObject)EditorGUILayout.ObjectField(movePhase.barrages[j], typeof(GameObject), false);
				EditorGUI.indentLevel -= 7;
				movePhase.fireRate = EditorGUILayout.FloatField(new GUIContent("Fire Rate"), movePhase.fireRate);
				movePhase.fireDelay = EditorGUILayout.FloatField(new GUIContent("Fire Delay"), movePhase.fireDelay);

				EditorGUI.indentLevel--;
			}
		}
	}
}
