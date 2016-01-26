using UnityEngine;
using System.Collections;

public class SceneLogin : MonoBehaviour {
	private UIInput inputAcc;
	private UIInput inputPwd;
	private UITexture login_bg;
	// Use this for initialization
	void Start () {
		UIRoot root = GameObject.FindObjectOfType<UIRoot>();
		if (root != null) {
			float s = (float)root.activeHeight / Screen.height;
			int height =  Mathf.CeilToInt(Screen.height * s);
			int width = Mathf.CeilToInt(Screen.width * s);
			Debug.Log("height = " + height);
			Debug.Log("width = " + width);
			login_bg = transform.Find ("login_bg").GetComponent<UITexture> ();
			login_bg.width = width;
			login_bg.height = height;
		}

		inputAcc = transform.Find ("input_acc").GetComponent<UIInput> ();
		inputPwd = transform.Find ("input_pwd").GetComponent<UIInput> ();



		BoxCollider[] bc = transform.GetComponentsInChildren<BoxCollider> (true);
		foreach (BoxCollider box in bc) {
			UIEventListener el = UIEventListener.Get(box.gameObject);
			el.onClick = BtnClick;
		}
	}

	private void BtnClick(GameObject click){
		if (click.name.Equals ("btn_start")) {
			Debug.Log("button is click "+inputAcc.value+","+inputPwd.value);
		}
	}
	
	// Update is called once per frame
	void Update () {
	
	}
}
