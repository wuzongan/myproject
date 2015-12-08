/*
** Lua binding: util_json
** Generated automatically by tolua++-1.0.92 on Fri Jan 25 19:22:01 2013.
*/

#ifndef __cplusplus
#include "stdlib.h"
#endif
#include "string.h"
#include "lua_jsonInterface.h"
#include "util_json.h"
/* Exported function */
TOLUA_API int  tolua_util_json_open (lua_State* tolua_S);


/* function to release collected object via destructor */
#ifdef __cplusplus

static int tolua_collect_util_json_iterator (lua_State* tolua_S)
{
 util_json_iterator* self = (util_json_iterator*) tolua_tousertype(tolua_S,1,0);
	Mtolua_delete(self);
	return 0;
}

static int tolua_collect_util_json (lua_State* tolua_S)
{
 util_json* self = (util_json*) tolua_tousertype(tolua_S,1,0);
	Mtolua_delete(self);
	return 0;
}
#endif


/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"util_json");
 tolua_usertype(tolua_S,"util_json_iterator");
}

/* method: new of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new00
static int tolua_util_json_util_json_new00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)());
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
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

/* method: new_local of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new00_local
static int tolua_util_json_util_json_new00_local(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)());
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
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

/* method: new of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new01
static int tolua_util_json_util_json_new01(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isusertype(tolua_S,2,"const util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  const util_json* node = ((const util_json*)  tolua_tousertype(tolua_S,2,0));
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)(node));
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
  }
 }
 return 1;
tolua_lerror:
 return tolua_util_json_util_json_new00(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: new_local of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new01_local
static int tolua_util_json_util_json_new01_local(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isusertype(tolua_S,2,"const util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  const util_json* node = ((const util_json*)  tolua_tousertype(tolua_S,2,0));
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)(node));
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
  }
 }
 return 1;
tolua_lerror:
 return tolua_util_json_util_json_new00_local(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: new of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new02
static int tolua_util_json_util_json_new02(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  const char* jsonbuffer = ((const char*)  tolua_tostring(tolua_S,2,0));
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)(jsonbuffer));
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
  }
 }
 return 1;
tolua_lerror:
 return tolua_util_json_util_json_new01(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: new_local of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_new02_local
static int tolua_util_json_util_json_new02_local(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  const char* jsonbuffer = ((const char*)  tolua_tostring(tolua_S,2,0));
  {
   util_json* tolua_ret = (util_json*)  Mtolua_new((util_json)(jsonbuffer));
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
  }
 }
 return 1;
tolua_lerror:
 return tolua_util_json_util_json_new01_local(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: delete of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_delete00
static int tolua_util_json_util_json_delete00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
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

/* method: addJson of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_addJson00
static int tolua_util_json_util_json_addJson00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* value = ((const char*)  tolua_tostring(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'addJson'", NULL);
#endif
  {
   self->addJson(key,value);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'addJson'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: addJson of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_addJson01
static int tolua_util_json_util_json_addJson01(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  float value = ((float)  tolua_tonumber(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'addJson'", NULL);
#endif
  {
   self->addJson(key,value);
  }
 }
 return 0;
tolua_lerror:
 return tolua_util_json_util_json_addJson00(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: addJson of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_addJson02
static int tolua_util_json_util_json_addJson02(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  int value = ((int)  tolua_tonumber(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'addJson'", NULL);
#endif
  {
   self->addJson(key,value);
  }
 }
 return 0;
tolua_lerror:
 return tolua_util_json_util_json_addJson01(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: addJson of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_addJson03
static int tolua_util_json_util_json_addJson03(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isboolean(tolua_S,3,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  bool value = ((bool)  tolua_toboolean(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'addJson'", NULL);
#endif
  {
   self->addJson(key,value);
  }
 }
 return 0;
tolua_lerror:
 return tolua_util_json_util_json_addJson02(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: addJson of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_addJson04
static int tolua_util_json_util_json_addJson04(lua_State* tolua_S)
{
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isusertype(tolua_S,3,"const util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* key = ((const char*)  tolua_tostring(tolua_S,2,0));
  const util_json* value = ((const util_json*)  tolua_tousertype(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'addJson'", NULL);
#endif
  {
   self->addJson(key,value);
  }
 }
 return 0;
tolua_lerror:
 return tolua_util_json_util_json_addJson03(tolua_S);
}
#endif //#ifndef TOLUA_DISABLE

/* method: createNode of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_createNode00
static int tolua_util_json_util_json_createNode00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'createNode'", NULL);
#endif
  {
   self->createNode();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'createNode'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: createArrayNode of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_createArrayNode00
static int tolua_util_json_util_json_createArrayNode00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'createArrayNode'", NULL);
#endif
  {
   self->createArrayNode();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'createArrayNode'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: setJsonNodeName of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_setJsonNodeName00
static int tolua_util_json_util_json_setJsonNodeName00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  const char* name = ((const char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setJsonNodeName'", NULL);
#endif
  {
   self->setJsonNodeName(name);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setJsonNodeName'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getNodeCount of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_getNodeCount00
static int tolua_util_json_util_json_getNodeCount00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getNodeCount'", NULL);
#endif
  {
   int tolua_ret = (int)  self->getNodeCount();
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getNodeCount'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getNodeAt of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_getNodeAt00
static int tolua_util_json_util_json_getNodeAt00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  int index = ((int)  tolua_tonumber(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getNodeAt'", NULL);
#endif
  {
   util_json* tolua_ret = (util_json*)  self->getNodeAt(index);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getNodeAt'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getNodeWithKey of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_getNodeWithKey00
static int tolua_util_json_util_json_getNodeWithKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
  char* key = ((char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getNodeWithKey'", NULL);
#endif
  {
   util_json* tolua_ret = (util_json*)  self->getNodeWithKey(key);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"util_json");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getNodeWithKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: begin of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_begin00
static int tolua_util_json_util_json_begin00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'begin'", NULL);
#endif
  {
   util_json_iterator tolua_ret = (util_json_iterator)  self->begin();
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((util_json_iterator)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"util_json_iterator");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(util_json_iterator));
     tolua_pushusertype(tolua_S,tolua_obj,"util_json_iterator");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'begin'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: end of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_end00
static int tolua_util_json_util_json_end00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'end'", NULL);
#endif
  {
   util_json_iterator tolua_ret = (util_json_iterator)  self->end();
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((util_json_iterator)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"util_json_iterator");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(util_json_iterator));
     tolua_pushusertype(tolua_S,tolua_obj,"util_json_iterator");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'end'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: toStr of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_toStr00
static int tolua_util_json_util_json_toStr00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'toStr'", NULL);
#endif
  {
   std::string tolua_ret = (std::string)  self->toStr();
   tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'toStr'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: toInt of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_toInt00
static int tolua_util_json_util_json_toInt00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'toInt'", NULL);
#endif
  {
   int tolua_ret = (int)  self->toInt();
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'toInt'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: toFloat of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_toFloat00
static int tolua_util_json_util_json_toFloat00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'toFloat'", NULL);
#endif
  {
   float tolua_ret = (float)  self->toFloat();
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'toFloat'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: toBool of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_toBool00
static int tolua_util_json_util_json_toBool00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'toBool'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->toBool();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'toBool'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: nodeType of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_nodeType00
static int tolua_util_json_util_json_nodeType00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'nodeType'", NULL);
#endif
  {
   int tolua_ret = (int)  self->nodeType();
   tolua_pushnumber(tolua_S,(lua_Number)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'nodeType'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getKey of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_getKey00
static int tolua_util_json_util_json_getKey00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getKey'", NULL);
#endif
  {
   std::string tolua_ret = (std::string)  self->getKey();
   tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getKey'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: isEmpty of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_isEmpty00
static int tolua_util_json_util_json_isEmpty00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'isEmpty'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->isEmpty();
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'isEmpty'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getFormatBuffer of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_getFormatBuffer00
static int tolua_util_json_util_json_getFormatBuffer00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  util_json* self = (util_json*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getFormatBuffer'", NULL);
#endif
  {
      std::string tolua_ret = (std::string)  self->getFormatBuffer();
      tolua_pushcppstring(tolua_S,(const char*)tolua_ret);
//   char* tolua_ret = (char*)  self->getFormatBuffer().c_str();
//   tolua_pushstring(tolua_S,(const char*)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getFormatBuffer'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: freeWriteBuffer of class  util_json */
#ifndef TOLUA_DISABLE_tolua_util_json_util_json_freeWriteBuffer00
static int tolua_util_json_util_json_freeWriteBuffer00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"util_json",0,&tolua_err) ||
     !tolua_isuserdata(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  void* buffer = ((void*)  tolua_touserdata(tolua_S,2,0));
  {
   util_json::freeWriteBuffer(buffer);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'freeWriteBuffer'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_util_json_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  #ifdef __cplusplus
  tolua_cclass(tolua_S,"util_json","util_json","",tolua_collect_util_json);
  #else
  tolua_cclass(tolua_S,"util_json","util_json","",NULL);
  #endif
  tolua_beginmodule(tolua_S,"util_json");
   tolua_constant(tolua_S,"ENull",util_json::ENull);
   tolua_constant(tolua_S,"EString",util_json::EString);
   tolua_constant(tolua_S,"ENumber",util_json::ENumber);
   tolua_constant(tolua_S,"EBool",util_json::EBool);
   tolua_constant(tolua_S,"EArray",util_json::EArray);
   tolua_constant(tolua_S,"ENode",util_json::ENode);
   tolua_constant(tolua_S,"EError",util_json::EError);
   tolua_function(tolua_S,"new",tolua_util_json_util_json_new00);
   tolua_function(tolua_S,"new_local",tolua_util_json_util_json_new00_local);
   tolua_function(tolua_S,".call",tolua_util_json_util_json_new00_local);
   tolua_function(tolua_S,"new",tolua_util_json_util_json_new01);
   tolua_function(tolua_S,"new_local",tolua_util_json_util_json_new01_local);
   tolua_function(tolua_S,".call",tolua_util_json_util_json_new01_local);
   tolua_function(tolua_S,"new",tolua_util_json_util_json_new02);
   tolua_function(tolua_S,"new_local",tolua_util_json_util_json_new02_local);
   tolua_function(tolua_S,".call",tolua_util_json_util_json_new02_local);
   tolua_function(tolua_S,"delete",tolua_util_json_util_json_delete00);
   tolua_function(tolua_S,"addJson",tolua_util_json_util_json_addJson00);
   tolua_function(tolua_S,"addJson",tolua_util_json_util_json_addJson01);
   tolua_function(tolua_S,"addJson",tolua_util_json_util_json_addJson02);
   tolua_function(tolua_S,"addJson",tolua_util_json_util_json_addJson03);
   tolua_function(tolua_S,"addJson",tolua_util_json_util_json_addJson04);
   tolua_function(tolua_S,"createNode",tolua_util_json_util_json_createNode00);
   tolua_function(tolua_S,"createArrayNode",tolua_util_json_util_json_createArrayNode00);
   tolua_function(tolua_S,"setJsonNodeName",tolua_util_json_util_json_setJsonNodeName00);
   tolua_function(tolua_S,"getNodeCount",tolua_util_json_util_json_getNodeCount00);
   tolua_function(tolua_S,"getNodeAt",tolua_util_json_util_json_getNodeAt00);
   tolua_function(tolua_S,"getNodeWithKey",tolua_util_json_util_json_getNodeWithKey00);
   tolua_function(tolua_S,"begin",tolua_util_json_util_json_begin00);
   tolua_function(tolua_S,"end",tolua_util_json_util_json_end00);
   tolua_function(tolua_S,"toStr",tolua_util_json_util_json_toStr00);
   tolua_function(tolua_S,"toInt",tolua_util_json_util_json_toInt00);
   tolua_function(tolua_S,"toFloat",tolua_util_json_util_json_toFloat00);
   tolua_function(tolua_S,"toBool",tolua_util_json_util_json_toBool00);
   tolua_function(tolua_S,"nodeType",tolua_util_json_util_json_nodeType00);
   tolua_function(tolua_S,"getKey",tolua_util_json_util_json_getKey00);
   tolua_function(tolua_S,"isEmpty",tolua_util_json_util_json_isEmpty00);
   tolua_function(tolua_S,"getFormatBuffer",tolua_util_json_util_json_getFormatBuffer00);
   tolua_function(tolua_S,"freeWriteBuffer",tolua_util_json_util_json_freeWriteBuffer00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_util_json (lua_State* tolua_S) {
 return tolua_util_json_open(tolua_S);
};
#endif

