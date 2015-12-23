/*
** Lua binding: GameMap_luabinding
** Generated automatically by tolua++-1.0.92 on Mon Aug 19 17:10:04 2013.
*/

#include "GameMap_luabinding.h"
#include "CCLuaEngine.h"
#include "GameMap.h"

using namespace cocos2d;





/* function to release collected object via destructor */
#ifdef __cplusplus

static int tolua_collect_CCPoint (lua_State* tolua_S)
{
 CCPoint* self = (CCPoint*) tolua_tousertype(tolua_S,1,0);
    Mtolua_delete(self);
    return 0;
}
#endif


/* function to register type */
static void tolua_reg_types (lua_State* tolua_S)
{
 tolua_usertype(tolua_S,"CCPoint");
 tolua_usertype(tolua_S,"CCLayerColor");
 tolua_usertype(tolua_S,"CCTMXTiledMap");
 tolua_usertype(tolua_S,"GameMap");
 tolua_usertype(tolua_S,"CCSize");
 tolua_usertype(tolua_S,"CCArray");
}

/* get function: allCollisionPolygon of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_allCollisionPolygon_ptr
static int tolua_get_GameMap_allCollisionPolygon_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'allCollisionPolygon'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)self->allCollisionPolygon,"CCArray");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: allCollisionPolygon of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_allCollisionPolygon_ptr
static int tolua_set_GameMap_allCollisionPolygon_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'allCollisionPolygon'",NULL);
  if (!tolua_isusertype(tolua_S,2,"CCArray",0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->allCollisionPolygon = ((CCArray*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: allCollisionPolyline of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_allCollisionPolyline_ptr
static int tolua_get_GameMap_allCollisionPolyline_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'allCollisionPolyline'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)self->allCollisionPolyline,"CCArray");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: allCollisionPolyline of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_allCollisionPolyline_ptr
static int tolua_set_GameMap_allCollisionPolyline_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'allCollisionPolyline'",NULL);
  if (!tolua_isusertype(tolua_S,2,"CCArray",0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->allCollisionPolyline = ((CCArray*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: world of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_world_ptr
static int tolua_get_GameMap_world_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'world'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)self->world,"CCArray");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: world of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_world_ptr
static int tolua_set_GameMap_world_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'world'",NULL);
  if (!tolua_isusertype(tolua_S,2,"CCArray",0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->world = ((CCArray*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: forwardVec of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_forwardVec
static int tolua_get_GameMap_forwardVec(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'forwardVec'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)&self->forwardVec,"CCPoint");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: forwardVec of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_forwardVec
static int tolua_set_GameMap_forwardVec(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'forwardVec'",NULL);
  if ((tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->forwardVec = *((CCPoint*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: backwardVec of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_backwardVec
static int tolua_get_GameMap_backwardVec(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'backwardVec'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)&self->backwardVec,"CCPoint");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: backwardVec of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_backwardVec
static int tolua_set_GameMap_backwardVec(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'backwardVec'",NULL);
  if ((tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->backwardVec = *((CCPoint*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: intersectPoint of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_intersectPoint
static int tolua_get_GameMap_intersectPoint(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'intersectPoint'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)&self->intersectPoint,"CCPoint");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: intersectPoint of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_intersectPoint
static int tolua_set_GameMap_intersectPoint(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'intersectPoint'",NULL);
  if ((tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->intersectPoint = *((CCPoint*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* get function: layer of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_get_GameMap_layer_ptr
static int tolua_get_GameMap_layer_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'layer'",NULL);
#endif
   tolua_pushusertype(tolua_S,(void*)self->layer,"CCLayerColor");
 return 1;
}
#endif //#ifndef TOLUA_DISABLE

/* set function: layer of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_set_GameMap_layer_ptr
static int tolua_set_GameMap_layer_ptr(lua_State* tolua_S)
{
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  tolua_Error tolua_err;
  if (!self) tolua_error(tolua_S,"invalid 'self' in accessing variable 'layer'",NULL);
  if (!tolua_isusertype(tolua_S,2,"CCLayerColor",0,&tolua_err))
   tolua_error(tolua_S,"#vinvalid type in variable assignment.",&tolua_err);
#endif
  self->layer = ((CCLayerColor*)  tolua_tousertype(tolua_S,2,0))
;
 return 0;
}
#endif //#ifndef TOLUA_DISABLE

/* method: gameMapWithTMXFile of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_gameMapWithTMXFile00
static int tolua_GameMap_luabinding_GameMap_gameMapWithTMXFile00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertable(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  const char* tmxFile = ((const char*)  tolua_tostring(tolua_S,2,0));
  {
   GameMap* tolua_ret = (GameMap*)  GameMap::gameMapWithTMXFile(tmxFile);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"GameMap");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'gameMapWithTMXFile'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: isPointInCollisionRegion of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_isPointInCollisionRegion00
static int tolua_GameMap_luabinding_GameMap_isPointInCollisionRegion00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     (tolua_isvaluenil(tolua_S,3,&tolua_err) || !tolua_isusertype(tolua_S,3,"CCSize",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint p = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
  CCSize roleSize = *((CCSize*)  tolua_tousertype(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'isPointInCollisionRegion'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->isPointInCollisionRegion(p,roleSize);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'isPointInCollisionRegion'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getPointInCollisionRegion of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_getPointInCollisionRegion00
static int tolua_GameMap_luabinding_GameMap_getPointInCollisionRegion00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     (tolua_isvaluenil(tolua_S,3,&tolua_err) || !tolua_isusertype(tolua_S,3,"CCSize",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint p = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
  CCSize roleSize = *((CCSize*)  tolua_tousertype(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getPointInCollisionRegion'", NULL);
#endif
  {
   CCPoint tolua_ret = (CCPoint)  self->getPointInCollisionRegion(p,roleSize);
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((CCPoint)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(CCPoint));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getPointInCollisionRegion'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: isWaylineIntersectCollisionLine of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_isWaylineIntersectCollisionLine00
static int tolua_GameMap_luabinding_GameMap_isWaylineIntersectCollisionLine00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     (tolua_isvaluenil(tolua_S,3,&tolua_err) || !tolua_isusertype(tolua_S,3,"CCPoint",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,4,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint origionPos = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
  CCPoint destPos = *((CCPoint*)  tolua_tousertype(tolua_S,3,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'isWaylineIntersectCollisionLine'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->isWaylineIntersectCollisionLine(origionPos,destPos);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'isWaylineIntersectCollisionLine'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: stringToPoints of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_stringToPoints00
static int tolua_GameMap_luabinding_GameMap_stringToPoints00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  char* s = ((char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'stringToPoints'", NULL);
#endif
  {
   CCArray* tolua_ret = (CCArray*)  self->stringToPoints(s);
    tolua_pushusertype(tolua_S,(void*)tolua_ret,"CCArray");
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'stringToPoints'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: stringToPoint of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_stringToPoint00
static int tolua_GameMap_luabinding_GameMap_stringToPoint00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isstring(tolua_S,2,0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  char* s = ((char*)  tolua_tostring(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'stringToPoint'", NULL);
#endif
  {
   CCPoint tolua_ret = (CCPoint)  self->stringToPoint(s);
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((CCPoint)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(CCPoint));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'stringToPoint'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: adjustPosition of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_adjustPosition00
static int tolua_GameMap_luabinding_GameMap_adjustPosition00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint position = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'adjustPosition'", NULL);
#endif
  {
   CCPoint tolua_ret = (CCPoint)  self->adjustPosition(position);
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((CCPoint)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(CCPoint));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'adjustPosition'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: tileCoordForPosition of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_tileCoordForPosition00
static int tolua_GameMap_luabinding_GameMap_tileCoordForPosition00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint position = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'tileCoordForPosition'", NULL);
#endif
  {
   CCPoint tolua_ret = (CCPoint)  self->tileCoordForPosition(position);
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((CCPoint)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(CCPoint));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'tileCoordForPosition'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: positionForTileCoord of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_positionForTileCoord00
static int tolua_GameMap_luabinding_GameMap_positionForTileCoord00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint tileCoord = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'positionForTileCoord'", NULL);
#endif
  {
   CCPoint tolua_ret = (CCPoint)  self->positionForTileCoord(tileCoord);
   {
#ifdef __cplusplus
    void* tolua_obj = Mtolua_new((CCPoint)(tolua_ret));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#else
    void* tolua_obj = tolua_copy(tolua_S,(void*)&tolua_ret,sizeof(CCPoint));
     tolua_pushusertype(tolua_S,tolua_obj,"CCPoint");
    tolua_register_gc(tolua_S,lua_gettop(tolua_S));
#endif
   }
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'positionForTileCoord'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: vibrationScreen of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_vibrationScreen00
static int tolua_GameMap_luabinding_GameMap_vibrationScreen00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'vibrationScreen'", NULL);
#endif
  {
   self->vibrationScreen();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'vibrationScreen'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: screedEnd of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_screedEnd00
static int tolua_GameMap_luabinding_GameMap_screedEnd00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'screedEnd'", NULL);
#endif
  {
   self->screedEnd();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'screedEnd'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: getCollisions of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_getCollisions00
static int tolua_GameMap_luabinding_GameMap_getCollisions00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     !tolua_isnoobj(tolua_S,2,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'getCollisions'", NULL);
#endif
  {
   self->getCollisions();
  }
 }
 return 0;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'getCollisions'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* method: isPointInWorld of class  GameMap */
#ifndef TOLUA_DISABLE_tolua_GameMap_luabinding_GameMap_isPointInWorld00
static int tolua_GameMap_luabinding_GameMap_isPointInWorld00(lua_State* tolua_S)
{
#ifndef TOLUA_RELEASE
 tolua_Error tolua_err;
 if (
     !tolua_isusertype(tolua_S,1,"GameMap",0,&tolua_err) ||
     (tolua_isvaluenil(tolua_S,2,&tolua_err) || !tolua_isusertype(tolua_S,2,"CCPoint",0,&tolua_err)) ||
     !tolua_isnoobj(tolua_S,3,&tolua_err)
 )
  goto tolua_lerror;
 else
#endif
 {
  GameMap* self = (GameMap*)  tolua_tousertype(tolua_S,1,0);
  CCPoint p = *((CCPoint*)  tolua_tousertype(tolua_S,2,0));
#ifndef TOLUA_RELEASE
  if (!self) tolua_error(tolua_S,"invalid 'self' in function 'isPointInWorld'", NULL);
#endif
  {
   bool tolua_ret = (bool)  self->isPointInWorld(p);
   tolua_pushboolean(tolua_S,(bool)tolua_ret);
  }
 }
 return 1;
#ifndef TOLUA_RELEASE
 tolua_lerror:
 tolua_error(tolua_S,"#ferror in function 'isPointInWorld'.",&tolua_err);
 return 0;
#endif
}
#endif //#ifndef TOLUA_DISABLE

/* Open function */
TOLUA_API int tolua_GameMap_luabinding_open (lua_State* tolua_S)
{
 tolua_open(tolua_S);
 tolua_reg_types(tolua_S);
 tolua_module(tolua_S,NULL,0);
 tolua_beginmodule(tolua_S,NULL);
  tolua_cclass(tolua_S,"GameMap","GameMap","CCTMXTiledMap",NULL);
  tolua_beginmodule(tolua_S,"GameMap");
   tolua_variable(tolua_S,"allCollisionPolygon",tolua_get_GameMap_allCollisionPolygon_ptr,tolua_set_GameMap_allCollisionPolygon_ptr);
   tolua_variable(tolua_S,"allCollisionPolyline",tolua_get_GameMap_allCollisionPolyline_ptr,tolua_set_GameMap_allCollisionPolyline_ptr);
   tolua_variable(tolua_S,"world",tolua_get_GameMap_world_ptr,tolua_set_GameMap_world_ptr);
   tolua_variable(tolua_S,"forwardVec",tolua_get_GameMap_forwardVec,tolua_set_GameMap_forwardVec);
   tolua_variable(tolua_S,"backwardVec",tolua_get_GameMap_backwardVec,tolua_set_GameMap_backwardVec);
   tolua_variable(tolua_S,"intersectPoint",tolua_get_GameMap_intersectPoint,tolua_set_GameMap_intersectPoint);
   tolua_variable(tolua_S,"layer",tolua_get_GameMap_layer_ptr,tolua_set_GameMap_layer_ptr);
   tolua_function(tolua_S,"gameMapWithTMXFile",tolua_GameMap_luabinding_GameMap_gameMapWithTMXFile00);
   tolua_function(tolua_S,"isPointInCollisionRegion",tolua_GameMap_luabinding_GameMap_isPointInCollisionRegion00);
   tolua_function(tolua_S,"getPointInCollisionRegion",tolua_GameMap_luabinding_GameMap_getPointInCollisionRegion00);
   tolua_function(tolua_S,"isWaylineIntersectCollisionLine",tolua_GameMap_luabinding_GameMap_isWaylineIntersectCollisionLine00);
   tolua_function(tolua_S,"stringToPoints",tolua_GameMap_luabinding_GameMap_stringToPoints00);
   tolua_function(tolua_S,"stringToPoint",tolua_GameMap_luabinding_GameMap_stringToPoint00);
   tolua_function(tolua_S,"adjustPosition",tolua_GameMap_luabinding_GameMap_adjustPosition00);
   tolua_function(tolua_S,"tileCoordForPosition",tolua_GameMap_luabinding_GameMap_tileCoordForPosition00);
   tolua_function(tolua_S,"positionForTileCoord",tolua_GameMap_luabinding_GameMap_positionForTileCoord00);
   tolua_function(tolua_S,"vibrationScreen",tolua_GameMap_luabinding_GameMap_vibrationScreen00);
   tolua_function(tolua_S,"screedEnd",tolua_GameMap_luabinding_GameMap_screedEnd00);
   tolua_function(tolua_S,"getCollisions",tolua_GameMap_luabinding_GameMap_getCollisions00);
   tolua_function(tolua_S,"isPointInWorld",tolua_GameMap_luabinding_GameMap_isPointInWorld00);
  tolua_endmodule(tolua_S);
 tolua_endmodule(tolua_S);
 return 1;
}


#if defined(LUA_VERSION_NUM) && LUA_VERSION_NUM >= 501
 TOLUA_API int luaopen_GameMap_luabinding (lua_State* tolua_S) {
 return tolua_GameMap_luabinding_open(tolua_S);
};
#endif

