using UnityEngine;
using System.Collections;

public class GameConfig : Singleton<GameConfig>
{
	
//	public static void AdaptiveUI()
//	{
//		int ManualWidth = 960;
//		int ManualHeight = 640;
//		UIRoot uiRoot = GameObject.FindObjectOfType<UIRoot>();
//		if (uiRoot != null)
//		{
//			if (System.Convert.ToSingle(Screen.height) / Screen.width > System.Convert.ToSingle(ManualHeight) / ManualWidth)
//				uiRoot.manualHeight = Mathf.RoundToInt(System.Convert.ToSingle(ManualWidth) / Screen.width * Screen.height);
//			else
//				uiRoot.manualHeight = ManualHeight;
//		}
//	}

//	public static float getCameraFOV(float currentFOV)
//	{
//		UIRoot root = GameObject.FindObjectOfType<UIRoot>();
//		float scale =Convert.ToSingle(root.manualHeight / 640f);
//		return currentFOV * scale;
//	}
//
//	Camera.main.fieldOfView = getCameraFOV(60);
}

