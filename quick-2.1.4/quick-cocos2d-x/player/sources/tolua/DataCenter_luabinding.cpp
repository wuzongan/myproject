/*
** Lua binding: DataCenter_luabinding
** Generated automatically by tolua++-1.0.92 on Mon Aug 19 21:11:54 2013.
*/

#include "DataCenter_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;





/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"CCObject");
 tolua_usertype(tolua_S,"DataCenter");
}

/* method: shared of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_shared00
static int tolua_DataCenter_luabinding_DataCenter_shared00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   DataCenter* tolua_ret = (DataCenter*)  DataCenter::shared();
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"DataCenter");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'shared'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setStringForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_setStringForKey00
static int tolua_DataCenter_luabinding_DataCenter_setStringForKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  const char* str = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* key = ((const char*)  tolua_tostring(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setStringForKey'", NULL);
#endif
  {
   self->setStringForKey(str,key);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setStringForKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getStringForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_getStringForKey00
static int tolua_DataCenter_luabinding_DataCenter_getStringForKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getStringForKey'", NULL);
#endif
  {
   const char* tolua_ret = (const char*)  self->getStringForKey(key);
   tolua_pushstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getStringForKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setStringForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_setStringForKey01
static int tolua_DataCenter_luabinding_DataCenter_setStringForKey01(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  const char* str = ((const char*)  tolua_tostring(tolua_S,2,0));
  int key = ((int)  tolua_tonumber(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setStringForKey'", NULL);
#endif
  {
   self->setStringForKey(str,key);
  }
 }
 return 0;
tolua_lerror:
 return tolua_DataCenter_luabinding_DataCenter_setStringForKey00(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: getStringForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_getStringForKey01
static int tolua_DataCenter_luabinding_DataCenter_getStringForKey01(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  int key = ((int)  tolua_tonumber(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getStringForKey'", NULL);
#endif
  {
   const char* tolua_ret = (const char*)  self->getStringForKey(key);
   tolua_pushstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
tolua_lerror:
 return tolua_DataCenter_luabinding_DataCenter_getStringForKey00(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: setValueForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_setValueForKey00
static int tolua_DataCenter_luabinding_DataCenter_setValueForKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  int val = ((int)  tolua_tonumber(tolua_S,2,0));
  int key = ((int)  tolua_tonumber(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setValueForKey'", NULL);
#endif
  {
   self->setValueForKey(val,key);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setValueForKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getValueForKey of class  DataCenter */
#ifndef TOLUA_DISABLE_tolua_DataCenter_luabinding_DataCenter_getValueForKey00
static int tolua_DataCenter_luabinding_DataCenter_getValueForKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"DataCenter",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  DataCenter* self = (DataCenter*)  tolua_tousertype(tolua_S,1,0);
  int key = ((int)  tolua_tonumber(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getValueForKey'", NULL);
#endif
  {
   int tolua_ret = (int)  self->getValueForKey(key);
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getValueForKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_DataCenter_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"DataCenter","DataCenter","CCObject",NULL);
  tolua_beginmodule(tolua_S,"DataCenter");
   tolua_function(tolua_S,"shared",tolua_DataCenter_luabinding_DataCenter_shared00);
   tolua_function(tolua_S,"setStringForKey",tolua_DataCenter_luabinding_DataCenter_setStringForKey00);
   tolua_function(tolua_S,"getStringForKey",tolua_DataCenter_luabinding_DataCenter_getStringForKey00);
   tolua_function(tolua_S,"setStringForKey",tolua_DataCenter_luabinding_DataCenter_setStringForKey01);
   tolua_function(tolua_S,"getStringForKey",tolua_DataCenter_luabinding_DataCenter_getStringForKey01);
   tolua_function(tolua_S,"setValueForKey",tolua_DataCenter_luabinding_DataCenter_setValueForKey00);
   tolua_function(tolua_S,"getValueForKey",tolua_DataCenter_luabinding_DataCenter_getValueForKey00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_DataCenter_luabinding (lua_State* tolua_S) {
 return tolua_DataCenter_luabinding_open(tolua_S);
};
#endif

