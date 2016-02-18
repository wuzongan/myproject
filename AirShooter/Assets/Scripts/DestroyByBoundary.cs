using UnityEngine;
using System.Collections;

public class DestroyByBoundary : MonoBehaviour
{

	void Start()
	{

	}

	void OnTriggerExit (Collider other) 
	{
		Transform root = other.transform;
		while (root.parent != null)
			root = root.parent;
		GameController.Instance.DestroyTarget(root.gameObject);
	}

}