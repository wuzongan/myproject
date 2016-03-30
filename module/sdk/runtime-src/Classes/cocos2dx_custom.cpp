#include "cocos2dx_custom.h"
#include "../cocos2d-x/cocos/scripting/lua-bindings/manual/CCLuaEngine.h"

static cocos2d::LUA_FUNCTION _function = 0;

//namespace cocos2d {
int CustomClass::aab()
{
	return 23;
}

int CustomClass::bb()
{
	return 24;
}

void CustomClass::onLuaFunc(cocos2d::LUA_FUNCTION i)
{
    _function = i;
}

void CustomClass::luaFunc()
{
	
	        if(_function != 0)
        {
            auto stack = cocos2d::LuaEngine::getInstance()->getLuaStack();
            stack->executeFunctionByHandler(_function, 0);
        }
        
}


//}