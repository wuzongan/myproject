#ifndef __CUSTOM__CLASS
#define __CUSTOM__CLASS

#include <stdio.h>
#include "cocos2d.h"
#include "../../cocos2d-x/cocos/scripting/lua-bindings/manual/CCLuaValue.h"

//namespace cocos2d {
class  CustomClass : public cocos2d::Ref
{
public:
    //name can't be empty
    //std::string name;
    
    //unsigned int maxInstances;
    
    /* minimum delay in between sounds */
    //double minDelay;
    static int aab();
    static int bb();
    /** 设置Lua回调入口 */
    //static void luaFunc();
    //staitc void aaa();
    static void onLuaFunc(cocos2d::LUA_FUNCTION i);
    static void luaFunc();
 private:
 /*
    AudioProfile()
    : maxInstances(0)
    , minDelay(0.0)
    {
        
    }
    */
};
//} //namespace cocos2d

#endif // __CUSTOM__CLASS