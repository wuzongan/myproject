#include "lua_cocos2dx_custom.hpp"
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_MAC || CC_TARGET_PLATFORM == CC_PLATFORM_WIN32
#include "cocos2dx_custom.h"
#include "tolua_fix.h"
#include "LuaBasicConversions.h"



int lua_cocos2dx_custom_CustomClass_luaFunc(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"CustomClass",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_cocos2dx_custom_CustomClass_luaFunc'", nullptr);
            return 0;
        }
        CustomClass::luaFunc();
        return 0;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "CustomClass:luaFunc",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_cocos2dx_custom_CustomClass_luaFunc'.",&tolua_err);
#endif
    return 0;
}
int lua_cocos2dx_custom_CustomClass_onLuaFunc(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"CustomClass",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 1)
    {
        /*
        int arg0;
        ok &= luaval_to_int32(tolua_S, 2,(int *)&arg0, "CustomClass:onLuaFunc");
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_cocos2dx_custom_CustomClass_onLuaFunc'", nullptr);
            return 0;
        }
        CustomClass::onLuaFunc(arg0);
        return 0;
        */
		
        int arg0 = 0;
        LUA_FUNCTION handler = (  toluafix_ref_function(tolua_S,2,*(int *)&arg0));
        CustomClass::onLuaFunc(handler);
        return 0;
		
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "CustomClass:onLuaFunc",argc, 1);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_cocos2dx_custom_CustomClass_onLuaFunc'.",&tolua_err);
#endif
    return 0;
}
int lua_cocos2dx_custom_CustomClass_bb(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"CustomClass",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_cocos2dx_custom_CustomClass_bb'", nullptr);
            return 0;
        }
        int ret = CustomClass::bb();
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "CustomClass:bb",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_cocos2dx_custom_CustomClass_bb'.",&tolua_err);
#endif
    return 0;
}
int lua_cocos2dx_custom_CustomClass_aab(lua_State* tolua_S)
{
    int argc = 0;
    bool ok  = true;

#if COCOS2D_DEBUG >= 1
    tolua_Error tolua_err;
#endif

#if COCOS2D_DEBUG >= 1
    if (!tolua_isusertable(tolua_S,1,"CustomClass",0,&tolua_err)) goto tolua_lerror;
#endif

    argc = lua_gettop(tolua_S) - 1;

    if (argc == 0)
    {
        if(!ok)
        {
            tolua_error(tolua_S,"invalid arguments in function 'lua_cocos2dx_custom_CustomClass_aab'", nullptr);
            return 0;
        }
        int ret = CustomClass::aab();
        tolua_pushnumber(tolua_S,(lua_Number)ret);
        return 1;
    }
    luaL_error(tolua_S, "%s has wrong number of arguments: %d, was expecting %d\n ", "CustomClass:aab",argc, 0);
    return 0;
#if COCOS2D_DEBUG >= 1
    tolua_lerror:
    tolua_error(tolua_S,"#ferror in function 'lua_cocos2dx_custom_CustomClass_aab'.",&tolua_err);
#endif
    return 0;
}
static int lua_cocos2dx_custom_CustomClass_finalize(lua_State* tolua_S)
{
    printf("luabindings: finalizing LUA object (CustomClass)");
    return 0;
}

int lua_register_cocos2dx_custom_CustomClass(lua_State* tolua_S)
{
    tolua_usertype(tolua_S,"CustomClass");
    tolua_cclass(tolua_S,"CustomClass","CustomClass","cc.Ref",nullptr);

    tolua_beginmodule(tolua_S,"CustomClass");
        tolua_function(tolua_S,"luaFunc", lua_cocos2dx_custom_CustomClass_luaFunc);
        tolua_function(tolua_S,"onLuaFunc", lua_cocos2dx_custom_CustomClass_onLuaFunc);
        tolua_function(tolua_S,"bb", lua_cocos2dx_custom_CustomClass_bb);
        tolua_function(tolua_S,"aab", lua_cocos2dx_custom_CustomClass_aab);
    tolua_endmodule(tolua_S);
    std::string typeName = typeid(CustomClass).name();
    g_luaType[typeName] = "CustomClass";
    g_typeCast["CustomClass"] = "CustomClass";
    return 1;
}
TOLUA_API int register_all_cocos2dx_custom(lua_State* tolua_S)
{
	tolua_open(tolua_S);
	
	tolua_module(tolua_S,nullptr,0);
	tolua_beginmodule(tolua_S,nullptr);

	lua_register_cocos2dx_custom_CustomClass(tolua_S);

	tolua_endmodule(tolua_S);
	return 1;
}

#endif
