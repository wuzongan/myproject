/*
** Lua binding: CustomFont_luabinding
** Generated automatically by tolua++-1.0.92 on Tue Dec 17 20:13:14 2013.
*/

#include "CustomFont_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;





/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"CustomFont");
}

/* method: add_font_style of class  CustomFont */
#ifndef TOLUA_DISABLE_tolua_CustomFont_luabinding_CustomFont_add_font_style00
static int tolua_CustomFont_luabinding_CustomFont_add_font_style00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"CustomFont",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isstring(tolua_S,3,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,4,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,5,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,6,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,7,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,8,0,&tolua_err) ||
     !tolua_isstring(tolua_S,9,0,&tolua_err) ||
     !tolua_isnumber(tolua_S,10,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,11,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  const char* alias = ((const char*)  tolua_tostring(tolua_S,2,0));
  const char* font_name = ((const char*)  tolua_tostring(tolua_S,3,0));
  int color = ((int)  tolua_tonumber(tolua_S,4,0));
  int size_pt = ((int)  tolua_tonumber(tolua_S,5,0));
  int style = ((int)  tolua_tonumber(tolua_S,6,0));
  float strength = ((float)  tolua_tonumber(tolua_S,7,0));
  int secondary_colorconst = ((int)  tolua_tonumber(tolua_S,8,0));
  char* hack_fontname = ((char*)  tolua_tostring(tolua_S,9,0));
  int hack_shift_y = ((int)  tolua_tonumber(tolua_S,10,0));
  {
   CustomFont::add_font_style(alias,font_name,color,size_pt,style,strength,secondary_colorconst,hack_fontname,hack_shift_y);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'add_font_style'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_CustomFont_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"CustomFont","CustomFont","",NULL);
  tolua_beginmodule(tolua_S,"CustomFont");
   tolua_function(tolua_S,"add_font_style",tolua_CustomFont_luabinding_CustomFont_add_font_style00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_CustomFont_luabinding (lua_State* tolua_S) {
 return tolua_CustomFont_luabinding_open(tolua_S);
};
#endif

