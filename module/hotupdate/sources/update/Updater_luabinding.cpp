/*
** Lua binding: Updater_luabinding
** Generated automatically by tolua++-1.0.92 on 07/10/14 16:02:21.
*/

#include "Updater_luabinding.h"
#include "CCLuaEngine.h"
#include "Updater.h"
using namespace cocos2d;
USING_NS_CC_EXT;




/* function to release collected object via destructor */
#ifdef __cplusplus

static int tolua_collect_Updater (lua_State* tolua_S)
{
 Updater* self = (Updater*) tolua_tousertype(tolua_S,1,0);
    Mtolua_delete(self);
    return 0;
}
#endif


/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 
 tolua_usertype(tolua_S,"Updater");
//toluafix_add_type_mapping(CLASS_HASH_CODE(typeid(Updater)), "Updater");
}

/* method: new of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_new00
static int tolua_Updater_luabinding_Updater_new00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   Updater* tolua_ret = (Updater*)  Mtolua_new((Updater)());
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"Updater");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'new'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: new_local of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_new00_local
static int tolua_Updater_luabinding_Updater_new00_local(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   Updater* tolua_ret = (Updater*)  Mtolua_new((Updater)());
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"Updater");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'new'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: delete of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_delete00
static int tolua_Updater_luabinding_Updater_delete00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'delete'", NULL);
#endif
  Mtolua_delete(self);
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'delete'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: update of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_update00
static int tolua_Updater_luabinding_Updater_update00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isstring(tolua_S,4,0,&tolua_err) ||
     !tolua_isboolean(tolua_S,5,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,6,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  const char* zipUrl = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* zipFile = ((const char*)  tolua_tostring(tolua_S,3,0));
  const char* unzipoTmpDir = ((const char*)  tolua_tostring(tolua_S,4,0));
  bool resetBeforeUnZip = ((bool)  tolua_toboolean(tolua_S,5,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'update'", NULL);
#endif
  {
   self->update(zipUrl,zipFile,unzipoTmpDir,resetBeforeUnZip);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'update'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setConnectionTimeout of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_setConnectionTimeout00
static int tolua_Updater_luabinding_Updater_setConnectionTimeout00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  unsigned int timeout = ((unsigned int)  tolua_tonumber(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setConnectionTimeout'", NULL);
#endif
  {
   self->setConnectionTimeout(timeout);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setConnectionTimeout'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getConnectionTimeout of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_getConnectionTimeout00
static int tolua_Updater_luabinding_Updater_getConnectionTimeout00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getConnectionTimeout'", NULL);
#endif
  {
   unsigned int tolua_ret = (unsigned int)  self->getConnectionTimeout();
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getConnectionTimeout'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: registerScriptHandler of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_registerScriptHandler00
static int tolua_Updater_luabinding_Updater_registerScriptHandler00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !toluafix_isfunction(tolua_S,2,"LUA_FUNCTION",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  LUA_FUNCTION handler = (  toluafix_ref_function(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'registerScriptHandler'", NULL);
#endif
  {
   self->registerScriptHandler(handler);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'registerScriptHandler'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: unregisterScriptHandler of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_unregisterScriptHandler00
static int tolua_Updater_luabinding_Updater_unregisterScriptHandler00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'unregisterScriptHandler'", NULL);
#endif
  {
   self->unregisterScriptHandler();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'unregisterScriptHandler'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: createDirectory of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_createDirectory00
static int tolua_Updater_luabinding_Updater_createDirectory00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  const char* path = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'createDirectory'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->createDirectory(path);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'createDirectory'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: removeDirectory of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_removeDirectory00
static int tolua_Updater_luabinding_Updater_removeDirectory00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  const char* path = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'removeDirectory'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->removeDirectory(path);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'removeDirectory'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getUpdateInfo of class  Updater */
#ifndef TOLUA_DISABLE_tolua_Updater_luabinding_Updater_getUpdateInfo00
static int tolua_Updater_luabinding_Updater_getUpdateInfo00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Updater",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Updater* self = (Updater*)  tolua_tousertype(tolua_S,1,0);
  const char* url = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getUpdateInfo'", NULL);
#endif
  {
   const char* tolua_ret = (const char*)  self->getUpdateInfo(url);
   tolua_pushstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getUpdateInfo'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_Updater_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  #ifdef __cplusplus
  tolua_cclass(tolua_S,"Updater","Updater","",tolua_collect_Updater);
  #else
  tolua_cclass(tolua_S,"Updater","Updater","",NULL);
  #endif
  tolua_beginmodule(tolua_S,"Updater");
   tolua_function(tolua_S,"new",tolua_Updater_luabinding_Updater_new00);
   tolua_function(tolua_S,"new_local",tolua_Updater_luabinding_Updater_new00_local);
   tolua_function(tolua_S,".call",tolua_Updater_luabinding_Updater_new00_local);
   tolua_function(tolua_S,"delete",tolua_Updater_luabinding_Updater_delete00);
   tolua_function(tolua_S,"update",tolua_Updater_luabinding_Updater_update00);
   tolua_function(tolua_S,"setConnectionTimeout",tolua_Updater_luabinding_Updater_setConnectionTimeout00);
   tolua_function(tolua_S,"getConnectionTimeout",tolua_Updater_luabinding_Updater_getConnectionTimeout00);
   tolua_function(tolua_S,"registerScriptHandler",tolua_Updater_luabinding_Updater_registerScriptHandler00);
   tolua_function(tolua_S,"unregisterScriptHandler",tolua_Updater_luabinding_Updater_unregisterScriptHandler00);
   tolua_function(tolua_S,"createDirectory",tolua_Updater_luabinding_Updater_createDirectory00);
   tolua_function(tolua_S,"removeDirectory",tolua_Updater_luabinding_Updater_removeDirectory00);
   tolua_function(tolua_S,"getUpdateInfo",tolua_Updater_luabinding_Updater_getUpdateInfo00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_Updater_luabinding (lua_State* tolua_S) {
 return tolua_Updater_luabinding_open(tolua_S);
};
#endif

