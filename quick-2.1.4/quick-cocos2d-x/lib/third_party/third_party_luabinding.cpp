
#include "third_party_luabinding.h"
#include "chipmunk/luabinding/CCPhysicsWorld_luabinding.h"
#include "CSArmature/luabinding/CSArmature_luabinding.h"
#include "luaproxy/tolua/tolua_CC_Extension.h"

TOLUA_API int luaopen_third_party_luabinding(lua_State* tolua_S)
{
    luaopen_CCPhysicsWorld_luabinding(tolua_S);
    luaopen_CSArmature_luabinding(tolua_S);
    tolua_CC_Extension_open(tolua_S);
    return 1;
};
