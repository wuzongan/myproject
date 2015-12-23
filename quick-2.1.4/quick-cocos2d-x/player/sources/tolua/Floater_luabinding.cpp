/*
** Lua binding: Floater_luabinding
** Generated automatically by tolua++-1.0.92 on Mon Oct 14 11:45:51 2013.
*/

#include "Floater_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;





/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"CCPoint");
 tolua_usertype(tolua_S,"CCRect");
 tolua_usertype(tolua_S,"CCSprite");
 tolua_usertype(tolua_S,"Floater");
}

/* get function: m_oRect of class  Floater */
#ifndef TOLUA_DISABLE_tolua_get_Floater_m_oRect
static int tolua_get_Floater_m_oRect(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'm_oRect'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)&self->m_oRect,"CCRect");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: m_oRect of class  Floater */
#ifndef TOLUA_DISABLE_tolua_set_Floater_m_oRect
static int tolua_set_Floater_m_oRect(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'm_oRect'",NULL);
  if ((tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCRect",0,&tolua_err)))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->m_oRect = *((CCRect*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: m_nType of class  Floater */
#ifndef TOLUA_DISABLE_tolua_get_Floater_m_nType
static int tolua_get_Floater_m_nType(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'm_nType'",NULL);
#endif
  tolua_pushnumber(tolua_S,(lua_Number)self->m_nType);
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: m_nType of class  Floater */
#ifndef TOLUA_DISABLE_tolua_set_Floater_m_nType
static int tolua_set_Floater_m_nType(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'm_nType'",NULL);
  if (!tolua_isnumber(tolua_S,2,0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->m_nType = ((int)  tolua_tonumber(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: dir of class  Floater */
#ifndef TOLUA_DISABLE_tolua_get_Floater_dir
static int tolua_get_Floater_dir(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'dir'",NULL);
#endif
  tolua_pushnumber(tolua_S,(lua_Number)self->dir);
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: dir of class  Floater */
#ifndef TOLUA_DISABLE_tolua_set_Floater_dir
static int tolua_set_Floater_dir(lua_State* tolua_S)
{
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'dir'",NULL);
  if (!tolua_isnumber(tolua_S,2,0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->dir = ((int)  tolua_tonumber(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* method: create of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_create00
static int tolua_Floater_luabinding_Floater_create00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,4,&tolua_err) || !tolua_isusertype(tolua_S,4,"const CCRect",0,&tolua_err)) ||
     !tolua_isnumber(tolua_S,5,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,6,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  int num = ((int)  tolua_tonumber(tolua_S,2,0));
  int type = ((int)  tolua_tonumber(tolua_S,3,0));
  const CCRect* rect = ((const CCRect*)  tolua_tousertype(tolua_S,4,0));
  int dir = ((int)  tolua_tonumber(tolua_S,5,0));
  {
   Floater* tolua_ret = (Floater*)  Floater::create(num,type,*rect,dir);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"Floater");
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

/* method: init of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_init00
static int tolua_Floater_luabinding_Floater_init00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnumber(tolua_S,2,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,3,0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,4,&tolua_err) || !tolua_isusertype(tolua_S,4,"const CCRect",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,5,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
  int num = ((int)  tolua_tonumber(tolua_S,2,0));
  int type = ((int)  tolua_tonumber(tolua_S,3,0));
  const CCRect* rect = ((const CCRect*)  tolua_tousertype(tolua_S,4,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'init'", NULL);
#endif
  {
   self->init(num,type,*rect);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'init'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: show of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_show00
static int tolua_Floater_luabinding_Floater_show00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'show'", NULL);
#endif
  {
   self->show();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'show'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: attackNormal of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_attackNormal00
static int tolua_Floater_luabinding_Floater_attackNormal00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'attackNormal'", NULL);
#endif
  {
   self->attackNormal();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'attackNormal'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: attackSkill of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_attackSkill00
static int tolua_Floater_luabinding_Floater_attackSkill00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'attackSkill'", NULL);
#endif
  {
   self->attackSkill();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'attackSkill'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: attackCritical of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_attackCritical00
static int tolua_Floater_luabinding_Floater_attackCritical00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'attackCritical'", NULL);
#endif
  {
   self->attackCritical();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'attackCritical'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: add of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_add00
static int tolua_Floater_luabinding_Floater_add00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'add'", NULL);
#endif
  {
   self->add();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'add'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: miss of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_miss00
static int tolua_Floater_luabinding_Floater_miss00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'miss'", NULL);
#endif
  {
   self->miss();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'miss'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: moveWithParabola of class  Floater */
#ifndef TOLUA_DISABLE_tolua_Floater_luabinding_Floater_moveWithParabola00
static int tolua_Floater_luabinding_Floater_moveWithParabola00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"Floater",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     (tolua_isvaluenil(tolua_S,3,&tolua_err) || !tolua_isusertype(tolua_S,3,"CCPoint",0,&tolua_err)) ||
     !tolua_isnumber(tolua_S,4,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,5,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,6,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  Floater* self = (Floater*)  tolua_tousertype(tolua_S,1,0);
  CCPoint startPoint = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
  CCPoint endPoint = *((CCPoint*)  tolua_tousertype(tolua_S,3,0));
  float originPoint = ((float)  tolua_tonumber(tolua_S,4,0));
  float time = ((float)  tolua_tonumber(tolua_S,5,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'moveWithParabola'", NULL);
#endif
  {
   self->moveWithParabola(startPoint,endPoint,originPoint,time);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'moveWithParabola'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_Floater_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"Floater","Floater","CCSprite",NULL);
  tolua_beginmodule(tolua_S,"Floater");
   tolua_variable(tolua_S,"m_oRect",tolua_get_Floater_m_oRect,tolua_set_Floater_m_oRect);
   tolua_variable(tolua_S,"m_nType",tolua_get_Floater_m_nType,tolua_set_Floater_m_nType);
   tolua_variable(tolua_S,"dir",tolua_get_Floater_dir,tolua_set_Floater_dir);
   tolua_function(tolua_S,"create",tolua_Floater_luabinding_Floater_create00);
   tolua_function(tolua_S,"init",tolua_Floater_luabinding_Floater_init00);
   tolua_function(tolua_S,"show",tolua_Floater_luabinding_Floater_show00);
   tolua_function(tolua_S,"attackNormal",tolua_Floater_luabinding_Floater_attackNormal00);
   tolua_function(tolua_S,"attackSkill",tolua_Floater_luabinding_Floater_attackSkill00);
   tolua_function(tolua_S,"attackCritical",tolua_Floater_luabinding_Floater_attackCritical00);
   tolua_function(tolua_S,"add",tolua_Floater_luabinding_Floater_add00);
   tolua_function(tolua_S,"miss",tolua_Floater_luabinding_Floater_miss00);
   tolua_function(tolua_S,"moveWithParabola",tolua_Floater_luabinding_Floater_moveWithParabola00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_Floater_luabinding (lua_State* tolua_S) {
 return tolua_Floater_luabinding_open(tolua_S);
};
#endif

