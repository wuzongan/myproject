/*
** Lua binding: NetworkLogic_luabinding
** Generated automatically by tolua++-1.0.92 on 02/25/14 11:55:27.
*/

#include "NetworkLogic_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;





/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"NetworkLogic");
}

/* method: init of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_init00
static int tolua_NetworkLogic_luabinding_NetworkLogic_init00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NetworkLogic* self = (NetworkLogic*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'init'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->init();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'init'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: start of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_start00
static int tolua_NetworkLogic_luabinding_NetworkLogic_start00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NetworkLogic* self = (NetworkLogic*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'start'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->start();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'start'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: handle_message of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_handle_message00
static int tolua_NetworkLogic_luabinding_NetworkLogic_handle_message00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NetworkLogic* self = (NetworkLogic*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'handle_message'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->handle_message();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'handle_message'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: reconnect of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_reconnect00
static int tolua_NetworkLogic_luabinding_NetworkLogic_reconnect00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_iscppstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  string host = ((string)  tolua_tocppstring(tolua_S,2,0));
  int port = ((int)  tolua_tonumber(tolua_S,3,0));
  {
   bool tolua_ret = (bool)  NetworkLogic::reconnect(host,port);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'reconnect'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: connect_server of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_connect_server00
static int tolua_NetworkLogic_luabinding_NetworkLogic_connect_server00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_iscppstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  string host = ((string)  tolua_tocppstring(tolua_S,2,0));
  int port = ((int)  tolua_tonumber(tolua_S,3,0));
  {
   bool tolua_ret = (bool)  NetworkLogic::connect_server(host,port);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'connect_server'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: clearSock of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_clearSock00
static int tolua_NetworkLogic_luabinding_NetworkLogic_clearSock00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   bool tolua_ret = (bool)  NetworkLogic::clearSock();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'clearSock'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: send_message of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_send_message00
static int tolua_NetworkLogic_luabinding_NetworkLogic_send_message00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  int length = ((int)  tolua_tonumber(tolua_S,2,0));
  char* pack = ((char*)  tolua_tostring(tolua_S,3,0));
  {
   NetworkLogic::send_message(length,pack);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'send_message'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: sharedInstance of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_sharedInstance00
static int tolua_NetworkLogic_luabinding_NetworkLogic_sharedInstance00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   NetworkLogic* tolua_ret = (NetworkLogic*)  NetworkLogic::sharedInstance();
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"NetworkLogic");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'sharedInstance'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* get function: s_sharedInstance of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_get_NetworkLogic_s_sharedInstance_ptr
static int tolua_get_NetworkLogic_s_sharedInstance_ptr(lua_State* tolua_S)
{
   tolua_pushusertype(tolua_S,(void*)NetworkLogic::s_sharedInstance,"NetworkLogic");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: s_sharedInstance of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_set_NetworkLogic_s_sharedInstance_ptr
static int tolua_set_NetworkLogic_s_sharedInstance_ptr(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!tolua_isusertype(tolua_S,2,"NetworkLogic",0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  NetworkLogic::s_sharedInstance = ((NetworkLogic*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* method: is_connected of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_is_connected00
static int tolua_NetworkLogic_luabinding_NetworkLogic_is_connected00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NetworkLogic* self = (NetworkLogic*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'is_connected'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->is_connected();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'is_connected'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getSystemTime of class  NetworkLogic */
#ifndef TOLUA_DISABLE_tolua_NetworkLogic_luabinding_NetworkLogic_getSystemTime00
static int tolua_NetworkLogic_luabinding_NetworkLogic_getSystemTime00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NetworkLogic",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NetworkLogic* self = (NetworkLogic*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getSystemTime'", NULL);
#endif
  {
   string tolua_ret = (string)  self->getSystemTime();
   tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getSystemTime'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_NetworkLogic_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"NetworkLogic","NetworkLogic","",NULL);
  tolua_beginmodule(tolua_S,"NetworkLogic");
   tolua_function(tolua_S,"init",tolua_NetworkLogic_luabinding_NetworkLogic_init00);
   tolua_function(tolua_S,"start",tolua_NetworkLogic_luabinding_NetworkLogic_start00);
   tolua_function(tolua_S,"handle_message",tolua_NetworkLogic_luabinding_NetworkLogic_handle_message00);
   tolua_function(tolua_S,"reconnect",tolua_NetworkLogic_luabinding_NetworkLogic_reconnect00);
   tolua_function(tolua_S,"connect_server",tolua_NetworkLogic_luabinding_NetworkLogic_connect_server00);
   tolua_function(tolua_S,"clearSock",tolua_NetworkLogic_luabinding_NetworkLogic_clearSock00);
   tolua_function(tolua_S,"send_message",tolua_NetworkLogic_luabinding_NetworkLogic_send_message00);
   tolua_function(tolua_S,"sharedInstance",tolua_NetworkLogic_luabinding_NetworkLogic_sharedInstance00);
   tolua_variable(tolua_S,"s_sharedInstance",tolua_get_NetworkLogic_s_sharedInstance_ptr,tolua_set_NetworkLogic_s_sharedInstance_ptr);
   tolua_function(tolua_S,"is_connected",tolua_NetworkLogic_luabinding_NetworkLogic_is_connected00);
   tolua_function(tolua_S,"getSystemTime",tolua_NetworkLogic_luabinding_NetworkLogic_getSystemTime00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_NetworkLogic_luabinding (lua_State* tolua_S) {
 return tolua_NetworkLogic_luabinding_open(tolua_S);
};
#endif

