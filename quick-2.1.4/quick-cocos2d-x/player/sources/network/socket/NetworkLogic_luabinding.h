
#ifndef __NETWORKLOGIC_LUABINDING_H_
#define __NETWORKLOGIC_LUABINDING_H_

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}
#include "NetworkLogic.h"

TOLUA_API int luaopen_NetworkLogic_luabinding(lua_State* tolua_S);

#endif // __NETWORKLOGIC_LUABINDING_H_
