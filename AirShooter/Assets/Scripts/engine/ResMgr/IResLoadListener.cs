using UnityEngine;
using System.Collections;

/// <summary>
/// 资源加载回调
/// </summary>
public interface IResLoadListener  
{
    void Finish(object asset);

    void Failure();
}
