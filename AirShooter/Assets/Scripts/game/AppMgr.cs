// **********************************************************************
// 
// 文件名(File Name)：             AppMgr
// 
// 作者(Author)：                  wza
// 
// 创建时间(CreateTime):           2016/1/26 19:16:45
//
// 网址：                          www.youke.pro
//
// **********************************************************************

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using System.Text;

public class AppMgr : EventNode 
{
    private static AppMgr mInstance;
	
    public static AppMgr Instance
    {
        get
        {
            return mInstance;
        }
    }
    private Camera mMainCamera;
    public Camera MainCamera
    {
        get
        {
            if (mMainCamera == null)
            {
                mMainCamera = Camera.current;
            }
            return mMainCamera;
        }
    }

    private Vector2 mSceneSize = new Vector2(960,640);
    /// <summary>
    /// 屏幕完美大小
    /// </summary>
    public Vector2 SceneSize
    {
        get
        {
            return mSceneSize;
        }
    }

    void Awake()
    {
        mInstance = this;
    }

    private const string mSoundValueKey = "YOUKESoundValueKEY";
    public bool SoundValue
    {
        get
        {
            return PlayerPrefs.GetInt(mSoundValueKey, 0) == 0;
        }
        set
        {
            PlayerPrefs.SetInt(mSoundValueKey, value ? 0 : 1);
            PlayerPrefs.Save();
        }
    }

    private const string mMusicValueKey = "YOUKEMusicValueKEY";
    public bool MusicValue
    {
        get
        {
            return PlayerPrefs.GetInt(mMusicValueKey, 0) == 0;
        }
        set
        {
            PlayerPrefs.SetInt(mMusicValueKey, value ? 0 : 1);
            PlayerPrefs.Save();
        }
    }

    public Vector3 HeroPos
    {
        get;
        set;
    }
#region 保存数据块

    private const string mOpenLevelsKey = "YOUKEOpenLevelsKEY";

    
    /// <summary>
    /// 所有以开放关卡
    /// </summary>
    public List<int> OpenLevels
    {
        get
        {
            List<int> list = new List<int>() {  };
            list.Add(1);
            string s = PlayerPrefs.GetString(mOpenLevelsKey, "");
            if (s.Contains("-"))
            {
                string[] ss = s.Split('-');
                for (int i = 0; i < ss.Length;i++ )
                {
                    int num = 0;
                    int.TryParse(ss[i], out num);
                    if (num != 0 && !list.Contains(num))
                    {
                        list.Add(num);
                    }
                }
            }
            return list;
        }
    }

    public void AddOpenLevel(int level)
    {
        StringBuilder sb = new StringBuilder();
        string openLevelStr = PlayerPrefs.GetString(mOpenLevelsKey,"");
        if (string.IsNullOrEmpty(openLevelStr))
        {
            openLevelStr = "1";
        }
        sb.Append(openLevelStr)
          .Append("-")
          .Append(level);
        
        PlayerPrefs.SetString(mOpenLevelsKey, sb.ToString());
        PlayerPrefs.Save();
    }
#endregion
}
