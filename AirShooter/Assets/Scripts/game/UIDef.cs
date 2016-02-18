using UnityEngine;
using System.Collections;

public class UIDef
{
    /// <summary>
    /// 死亡界面UI名称
    /// </summary>
    public const string DieUI = "CanvasDie";

    /// <summary>
    /// 主界面UI名称
    /// </summary>
    public const string StartUI = "CanvasStart";

    /// <summary>
    /// 设置界面UI
    /// </summary>
    public const string SettingsUI = "CanvasSettings";

    /// <summary>
    /// 选关界面UI
    /// </summary>
    public const string SelectLevelUI = "CanvasSelectLevel";

    /// <summary>
    /// 获取界面的顺序
    /// </summary>
    /// <param name="uianme"></param>
    /// <returns></returns>
    public static int GetUIOrderLayer(string uianme)
    {
        switch (uianme)
        {
            case UIDef.StartUI:
                return 1;

            case UIDef.SettingsUI:
            case UIDef.SelectLevelUI:
            case UIDef.DieUI:
                return 2;
        }
        return 0;
    }

    /// <summary>
    /// 获取level等级
    /// </summary>
    /// <param name="level"></param>
    /// <returns></returns>
    public static string GetLevelName(int level)
    {
        return string.Format("Level_{0}", level);
    }
}
