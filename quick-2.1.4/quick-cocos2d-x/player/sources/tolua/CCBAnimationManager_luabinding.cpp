/*
** Lua binding: CCBAnimationManager_luabinding
** Generated automatically by tolua++-1.0.92 on Mon Mar 10 20:25:04 2014.
*/

#include "CCBAnimationManager_luabinding.h"
#include "CCLuaEngine.h"

using namespace cocos2d;





/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"CCBAnimationManager");
 tolua_usertype(tolua_S,"CCObject");
 tolua_usertype(tolua_S,"CCArray");
 tolua_usertype(tolua_S,"CCCallFunc");
}

/* method: setAnimationCompletedCallback of class  CCBAnimationManager */
#ifndef TOLUA_DISABLE_tolua_CCBAnimationManager_luabinding_CCBAnimationManager_setAnimationCompletedCallback00
static int tolua_CCBAnimationManager_luabinding_CCBAnimationManager_setAnimationCompletedCallback00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"CCBAnimationManager",0,&tolua_err) ||
     !tolua_isusertype(tolua_S,2,"CCObject",0,&tolua_err) ||
     !tolua_isusertype(tolua_S,3,"CCCallFunc",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  CCBAnimationManager* self = (CCBAnimationManager*)  tolua_tousertype(tolua_S,1,0);
  CCObject* target = ((CCObject*)  tolua_tousertype(tolua_S,2,0));
  CCCallFunc* callbackFunc = ((CCCallFunc*)  tolua_tousertype(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'setAnimationCompletedCallback'", NULL);
#endif
  {
   self->setAnimationCompletedCallback(target,callbackFunc);
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'setAnimationCompletedCallback'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: pauseAnimation of class  CCBAnimationManager */
#ifndef TOLUA_DISABLE_tolua_CCBAnimationManager_luabinding_CCBAnimationManager_pauseAnimation00
static int tolua_CCBAnimationManager_luabinding_CCBAnimationManager_pauseAnimation00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"CCBAnimationManager",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  CCBAnimationManager* self = (CCBAnimationManager*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'pauseAnimation'", NULL);
#endif
  {
   self->pauseAnimation();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'pauseAnimation'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: resumeAnimation of class  CCBAnimationManager */
#ifndef TOLUA_DISABLE_tolua_CCBAnimationManager_luabinding_CCBAnimationManager_resumeAnimation00
static int tolua_CCBAnimationManager_luabinding_CCBAnimationManager_resumeAnimation00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"CCBAnimationManager",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  CCBAnimationManager* self = (CCBAnimationManager*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'resumeAnimation'", NULL);
#endif
  {
   self->resumeAnimation();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'resumeAnimation'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getParticles of class  CCBAnimationManager */
#ifndef TOLUA_DISABLE_tolua_CCBAnimationManager_luabinding_CCBAnimationManager_getParticles00
static int tolua_CCBAnimationManager_luabinding_CCBAnimationManager_getParticles00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"CCBAnimationManager",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  CCBAnimationManager* self = (CCBAnimationManager*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getParticles'", NULL);
#endif
  {
   CCArray* tolua_ret = (CCArray*)  self->getParticles();
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"CCArray");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getParticles'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_CCBAnimationManager_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"CCBAnimationManager","CCBAnimationManager","CCObject",NULL);
  tolua_beginmodule(tolua_S,"CCBAnimationManager");
   tolua_function(tolua_S,"setAnimationCompletedCallback",tolua_CCBAnimationManager_luabinding_CCBAnimationManager_setAnimationCompletedCallback00);
   tolua_function(tolua_S,"pauseAnimation",tolua_CCBAnimationManager_luabinding_CCBAnimationManager_pauseAnimation00);
   tolua_function(tolua_S,"resumeAnimation",tolua_CCBAnimationManager_luabinding_CCBAnimationManager_resumeAnimation00);
   tolua_function(tolua_S,"getParticles",tolua_CCBAnimationManager_luabinding_CCBAnimationManager_getParticles00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_CCBAnimationManager_luabinding (lua_State* tolua_S) {
 return tolua_CCBAnimationManager_luabinding_open(tolua_S);
};
#endif

