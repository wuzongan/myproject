/*
** Lua binding: Plugin_luabinding
** Generated automatically by tolua++-1.0.92 on 04/04/14 16:35:37.
*/

#include "Plugin_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;




#include "PluginProtocol.h"
#include <map>
#include <string>
#include "ProtocolUser.h"
#include "ProtocolIAP.h"
#include "PluginManager.h"
using namespace cocos2d::plugin;

/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"ProtocolUser");
 
 tolua_usertype(tolua_S,"PluginManager");
 tolua_usertype(tolua_S,"PluginProtocol");
 tolua_usertype(tolua_S,"ProtocolIAP");
}

/* method: getInstance of class  PluginManager */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_PluginManager_getInstance00
static int tolua_Plugin_luabinding_PluginManager_getInstance00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"PluginManager",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   PluginManager* tolua_ret = (PluginManager*)  PluginManager::getInstance();
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"PluginManager");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getInstance'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: end of class  PluginManager */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_PluginManager_end00
static int tolua_Plugin_luabinding_PluginManager_end00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"PluginManager",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   PluginManager::end();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'end'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: loadPlugin of class  PluginManager */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_PluginManager_loadPlugin00
static int tolua_Plugin_luabinding_PluginManager_loadPlugin00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"PluginManager",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  PluginManager* self = (PluginManager*)  tolua_tousertype(tolua_S,1,0);
  const char* name = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'loadPlugin'", NULL);
#endif
  {
   PluginProtocol* tolua_ret = (PluginProtocol*)  self->loadPlugin(name);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"PluginProtocol");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'loadPlugin'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: unloadPlugin of class  PluginManager */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_PluginManager_unloadPlugin00
static int tolua_Plugin_luabinding_PluginManager_unloadPlugin00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"PluginManager",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  PluginManager* self = (PluginManager*)  tolua_tousertype(tolua_S,1,0);
  const char* name = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'unloadPlugin'", NULL);
#endif
  {
   self->unloadPlugin(name);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'unloadPlugin'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setDebugMode of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_setDebugMode00
static int tolua_Plugin_luabinding_ProtocolUser_setDebugMode00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isboolean(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
  bool bDebug = ((bool)  tolua_toboolean(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setDebugMode'", NULL);
#endif
  {
   self->setDebugMode(bDebug);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setDebugMode'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: configDeveloperInfo of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_configDeveloperInfo00
static int tolua_Plugin_luabinding_ProtocolUser_configDeveloperInfo00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'configDeveloperInfo'", NULL);
#endif
  {
   self->configDeveloperInfo();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'configDeveloperInfo'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: set of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_set00
static int tolua_Plugin_luabinding_ProtocolUser_set00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* value = ((const char*)  tolua_tostring(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'set'", NULL);
#endif
  {
   self->set(key,value);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'set'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: login of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_login00
static int tolua_Plugin_luabinding_ProtocolUser_login00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'login'", NULL);
#endif
  {
   self->login();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'login'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: logout of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_logout00
static int tolua_Plugin_luabinding_ProtocolUser_logout00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'logout'", NULL);
#endif
  {
   self->logout();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'logout'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: isLogined of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_isLogined00
static int tolua_Plugin_luabinding_ProtocolUser_isLogined00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'isLogined'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->isLogined();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'isLogined'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getSessionID of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_getSessionID00
static int tolua_Plugin_luabinding_ProtocolUser_getSessionID00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getSessionID'", NULL);
#endif
  {
   std::string tolua_ret = (std::string)  self->getSessionID();
   tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getSessionID'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setListener of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_setListener00
static int tolua_Plugin_luabinding_ProtocolUser_setListener00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !toluafix_isfunction(tolua_S,2,"LUA_FUNCTION",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
  LUA_FUNCTION listener = (  toluafix_ref_function(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setListener'", NULL);
#endif
  {
   self->setListener(listener);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setListener'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: callFunction of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_callFunction00
static int tolua_Plugin_luabinding_ProtocolUser_callFunction00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
  const char* funcName = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'callFunction'", NULL);
#endif
  {
   std::string tolua_ret = (std::string)  self->callFunction(funcName);
   tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'callFunction'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: callFunctionWithString of class  ProtocolUser */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolUser_callFunctionWithString00
static int tolua_Plugin_luabinding_ProtocolUser_callFunctionWithString00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolUser",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolUser* self = (ProtocolUser*)  tolua_tousertype(tolua_S,1,0);
  const char* funcName = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* parm = ((const char*)  tolua_tostring(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'callFunctionWithString'", NULL);
#endif
  {
   self->callFunctionWithString(funcName,parm);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'callFunctionWithString'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setDebugMode of class  ProtocolIAP */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolIAP_setDebugMode00
static int tolua_Plugin_luabinding_ProtocolIAP_setDebugMode00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolIAP",0,&tolua_err) ||
     !tolua_isboolean(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolIAP* self = (ProtocolIAP*)  tolua_tousertype(tolua_S,1,0);
  bool bDebug = ((bool)  tolua_toboolean(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setDebugMode'", NULL);
#endif
  {
   self->setDebugMode(bDebug);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setDebugMode'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: configDeveloperInfo of class  ProtocolIAP */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolIAP_configDeveloperInfo00
static int tolua_Plugin_luabinding_ProtocolIAP_configDeveloperInfo00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolIAP",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolIAP* self = (ProtocolIAP*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'configDeveloperInfo'", NULL);
#endif
  {
   self->configDeveloperInfo();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'configDeveloperInfo'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: payForProduct of class  ProtocolIAP */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolIAP_payForProduct00
static int tolua_Plugin_luabinding_ProtocolIAP_payForProduct00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolIAP",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolIAP* self = (ProtocolIAP*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'payForProduct'", NULL);
#endif
  {
   self->payForProduct();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'payForProduct'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: set of class  ProtocolIAP */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolIAP_set00
static int tolua_Plugin_luabinding_ProtocolIAP_set00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolIAP",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolIAP* self = (ProtocolIAP*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* value = ((const char*)  tolua_tostring(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'set'", NULL);
#endif
  {
   self->set(key,value);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'set'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setListener of class  ProtocolIAP */
#ifndef TOLUA_DISABLE_tolua_Plugin_luabinding_ProtocolIAP_setListener00
static int tolua_Plugin_luabinding_ProtocolIAP_setListener00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"ProtocolIAP",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !toluafix_isfunction(tolua_S,2,"LUA_FUNCTION",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  ProtocolIAP* self = (ProtocolIAP*)  tolua_tousertype(tolua_S,1,0);
  LUA_FUNCTION listener = (  toluafix_ref_function(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setListener'", NULL);
#endif
  {
   self->setListener(listener);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setListener'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_Plugin_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"PluginManager","PluginManager","",NULL);
  tolua_beginmodule(tolua_S,"PluginManager");
   tolua_function(tolua_S,"getInstance",tolua_Plugin_luabinding_PluginManager_getInstance00);
   tolua_function(tolua_S,"end",tolua_Plugin_luabinding_PluginManager_end00);
   tolua_function(tolua_S,"loadPlugin",tolua_Plugin_luabinding_PluginManager_loadPlugin00);
   tolua_function(tolua_S,"unloadPlugin",tolua_Plugin_luabinding_PluginManager_unloadPlugin00);
  tolua_endmodule(tolua_S);
  tolua_constant(tolua_S,"kLoginSucceed",kLoginSucceed);
  tolua_constant(tolua_S,"kLoginFailed",kLoginFailed);
  tolua_constant(tolua_S,"kLogoutSucceed",kLogoutSucceed);
  tolua_constant(tolua_S,"kInitFinish",kInitFinish);
  tolua_constant(tolua_S,"kUpdateNew",kUpdateNew);
  tolua_constant(tolua_S,"kNoUpdate",kNoUpdate);
  tolua_cclass(tolua_S,"ProtocolUser","ProtocolUser","PluginProtocol",NULL);
  tolua_beginmodule(tolua_S,"ProtocolUser");
   tolua_function(tolua_S,"setDebugMode",tolua_Plugin_luabinding_ProtocolUser_setDebugMode00);
   tolua_function(tolua_S,"configDeveloperInfo",tolua_Plugin_luabinding_ProtocolUser_configDeveloperInfo00);
   tolua_function(tolua_S,"set",tolua_Plugin_luabinding_ProtocolUser_set00);
   tolua_function(tolua_S,"login",tolua_Plugin_luabinding_ProtocolUser_login00);
   tolua_function(tolua_S,"logout",tolua_Plugin_luabinding_ProtocolUser_logout00);
   tolua_function(tolua_S,"isLogined",tolua_Plugin_luabinding_ProtocolUser_isLogined00);
   tolua_function(tolua_S,"getSessionID",tolua_Plugin_luabinding_ProtocolUser_getSessionID00);
   tolua_function(tolua_S,"setListener",tolua_Plugin_luabinding_ProtocolUser_setListener00);
   tolua_function(tolua_S,"callFunction",tolua_Plugin_luabinding_ProtocolUser_callFunction00);
   tolua_function(tolua_S,"callFunctionWithString",tolua_Plugin_luabinding_ProtocolUser_callFunctionWithString00);
  tolua_endmodule(tolua_S);
  tolua_constant(tolua_S,"kPaySuccess",kPaySuccess);
  tolua_constant(tolua_S,"kPayFail",kPayFail);
  tolua_constant(tolua_S,"kPayCancel",kPayCancel);
  tolua_constant(tolua_S,"kPayTimeOut",kPayTimeOut);
  tolua_cclass(tolua_S,"ProtocolIAP","ProtocolIAP","PluginProtocol",NULL);
  tolua_beginmodule(tolua_S,"ProtocolIAP");
   tolua_function(tolua_S,"setDebugMode",tolua_Plugin_luabinding_ProtocolIAP_setDebugMode00);
   tolua_function(tolua_S,"configDeveloperInfo",tolua_Plugin_luabinding_ProtocolIAP_configDeveloperInfo00);
   tolua_function(tolua_S,"payForProduct",tolua_Plugin_luabinding_ProtocolIAP_payForProduct00);
   tolua_function(tolua_S,"set",tolua_Plugin_luabinding_ProtocolIAP_set00);
   tolua_function(tolua_S,"setListener",tolua_Plugin_luabinding_ProtocolIAP_setListener00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_Plugin_luabinding (lua_State* tolua_S) {
 return tolua_Plugin_luabinding_open(tolua_S);
};
#endif

