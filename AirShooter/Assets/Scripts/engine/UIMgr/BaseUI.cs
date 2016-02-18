using UnityEngine;
using System.Collections;

public abstract class BaseUI : MonoBehaviour 
{
    
    /// <summary>
    /// 当前界面名称
    /// </summary>
    [HideInInspector]
    public string UIName;

    private Transform mTransform;
    public Transform CacheTransform
    {
        get
        {
            if (mTransform == null) mTransform = this.transform;
            return mTransform;
        }
    }

    private GameObject mGo;
    public GameObject CacheGameObject
    {
        get
        {
            if (mGo == null) mGo = this.gameObject;
            return mGo;
        }
    }

    /// <summary>
    /// 显示当前UI
    /// </summary>
    /// <param name="param">附加参数</param>
    public void Show(object param)
    {
        CacheGameObject.SetActive(true);
        OnShow(param);
    }

    /// <summary>
    /// 隐藏当前界面
    /// </summary>
    public void Hide()
    {
        CacheGameObject.SetActive(false);
        OnHide();
    }

    [HideInInspector]
    public Canvas mainCanvas = null;

	/// <summary>
	/// 绑定脚本并且激活游戏物体会调用的方法
	/// </summary>
	void Awake() 
	{
        OnAwake();
	}

    /// <summary>
    /// 初始化UI主要用于寻找组件等
    /// </summary>
    public void UIInit()
    {
        if (mainCanvas == null)
        {
            mainCanvas = this.GetComponent<Canvas>();
        }
        if (mainCanvas != null)
        {
            mainCanvas.worldCamera = AppMgr.Instance.MainCamera;
        }
        mainCanvas.sortingOrder = UIDef.GetUIOrderLayer(UIName);
        OnInit();
    }

    /// <summary>
    /// 初始化当前界面
    /// </summary>
    protected abstract void OnInit();
    protected abstract void OnAwake();

    /// <summary>
    /// 显示当前界面
    /// </summary>
    /// <param name="param">附加参数</param>
    protected abstract void OnShow(object param);

    /// <summary>
    /// 隐藏当前界面
    /// </summary>
    protected abstract void OnHide();
    /// <summary>
    /// 删除当前UI 
    /// </summary>
    protected abstract void OnDestroy();
}
