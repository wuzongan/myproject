using UnityEngine;  
using System.Collections;  

[RequireComponent(typeof(UICamera))]  
public class BaseAspect : MonoBehaviour  
{  
	float standard_width = 960f;        //初始宽度  
	float standard_height = 640f;       //初始高度  
	float device_width = 0f;                //当前设备宽度  
	float device_height = 0f;               //当前设备高度  
	public float adjustor = 0f;         //屏幕矫正比例  
	void Awake()  
	{  
		
		//获取设备宽高  
		device_width = Screen.width;  
		device_height = Screen.height;  
		//计算宽高比例  
		float standard_aspect = Screen.width / standard_height;  
		float device_aspect = device_width / device_height;  
		//计算矫正比例  
		if (device_aspect < standard_aspect)  
		{  
			adjustor = standard_aspect / device_aspect;  
			//Debug.Log(standard_aspect);  
		}  
		Debug.Log("屏幕的比例" + adjustor);  
		if (adjustor < 2 && adjustor > 0)  
		{  
			GetComponent<Camera>().orthographicSize = adjustor;  
		}  
		
	}  
	// Use this for initialization  
	void Start()  
	{  
		
	}  
	
	// Update is called once per frame  
	void Update()  
	{  
		
	}  
}  