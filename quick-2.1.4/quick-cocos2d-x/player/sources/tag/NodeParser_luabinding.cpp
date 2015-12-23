/*
** Lua binding: NodeParser_luabinding
** Generated automatically by tolua++-1.0.92 on 10/28/13 18:06:31.
*/

#include "NodeParser_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;




#include"NodeParser.h"

/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 
 tolua_usertype(tolua_S,"CCSAXDelegator");
 tolua_usertype(tolua_S,"CCObject");
 tolua_usertype(tolua_S,"NodeParser");
 tolua_usertype(tolua_S,"CCNode");
}

/* method: create of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_NodeParser_luabinding_NodeParser_create00
static int tolua_NodeParser_luabinding_NodeParser_create00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"NodeParser",0,&tolua_err) ||
     !tolua_isusertype(tolua_S,2,"CCNode",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  CCNode* cNode = ((CCNode*)  tolua_tousertype(tolua_S,2,0));
  {
   NodeParser* tolua_ret = (NodeParser*)  NodeParser::create(cNode);
    int nID = (tolua_ret) ? tolua_ret->m_uID : -1;
int* pLuaID = (tolua_ret) ? &tolua_ret->m_nLuaID : NULL;
toluafix_pushusertype_ccobject(tolua_S, nID, pLuaID, (void*)tolua_ret,"NodeParser");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'create'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: registerTextScriptListener of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_NodeParser_luabinding_NodeParser_registerTextScriptListener00
static int tolua_NodeParser_luabinding_NodeParser_registerTextScriptListener00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NodeParser",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !toluafix_isfunction(tolua_S,2,"LUA_FUNCTION",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NodeParser* self = (NodeParser*)  tolua_tousertype(tolua_S,1,0);
  LUA_FUNCTION listener = (  toluafix_ref_function(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'registerTextScriptListener'", NULL);
#endif
  {
   self->registerTextScriptListener(listener);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'registerTextScriptListener'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: registerImageScriptListener of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_NodeParser_luabinding_NodeParser_registerImageScriptListener00
static int tolua_NodeParser_luabinding_NodeParser_registerImageScriptListener00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NodeParser",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !toluafix_isfunction(tolua_S,2,"LUA_FUNCTION",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NodeParser* self = (NodeParser*)  tolua_tousertype(tolua_S,1,0);
  LUA_FUNCTION listener = (  toluafix_ref_function(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'registerImageScriptListener'", NULL);
#endif
  {
   self->registerImageScriptListener(listener);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'registerImageScriptListener'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: parseStr of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_NodeParser_luabinding_NodeParser_parseStr00
static int tolua_NodeParser_luabinding_NodeParser_parseStr00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NodeParser",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NodeParser* self = (NodeParser*)  tolua_tousertype(tolua_S,1,0);
  char* text = ((char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'parseStr'", NULL);
#endif
  {
   self->parseStr(text);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'parseStr'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: deploy of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_NodeParser_luabinding_NodeParser_deploy00
static int tolua_NodeParser_luabinding_NodeParser_deploy00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"NodeParser",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  NodeParser* self = (NodeParser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'deploy'", NULL);
#endif
  {
   self->deploy();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'deploy'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* get function: __CCObject__ of class  NodeParser */
#ifndef TOLUA_DISABLE_tolua_get_NodeParser___CCObject__
static int tolua_get_NodeParser___CCObject__(lua_State* tolua_S)
{
  NodeParser* self = (NodeParser*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable '__CCObject__'",NULL);
#endif
#ifdef __cplusplus
   tolua_pushusertype(tolua_S,(void*)static_cast<CCObject*>(self), "CCObject");
#else
   tolua_pushusertype(tolua_S,(void*)((CCObject*)self), "CCObject");
#endif
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_NodeParser_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"NodeParser","NodeParser","CCSAXDelegator",NULL);
  tolua_beginmodule(tolua_S,"NodeParser");
   tolua_function(tolua_S,"create",tolua_NodeParser_luabinding_NodeParser_create00);
   tolua_function(tolua_S,"registerTextScriptListener",tolua_NodeParser_luabinding_NodeParser_registerTextScriptListener00);
   tolua_function(tolua_S,"registerImageScriptListener",tolua_NodeParser_luabinding_NodeParser_registerImageScriptListener00);
   tolua_function(tolua_S,"parseStr",tolua_NodeParser_luabinding_NodeParser_parseStr00);
   tolua_function(tolua_S,"deploy",tolua_NodeParser_luabinding_NodeParser_deploy00);
   tolua_variable(tolua_S,"__CCObject__",tolua_get_NodeParser___CCObject__,NULL);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_NodeParser_luabinding (lua_State* tolua_S) {
 return tolua_NodeParser_luabinding_open(tolua_S);
};
#endif

