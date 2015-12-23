
#ifndef __FLOATER_LUABINDING_H_
#define __FLOATER_LUABINDING_H_

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}
#include "Floater.h"

TOLUA_API int luaopen_Floater_luabinding(lua_State* tolua_S);

#endif // __FLOATER_LUABINDING_H_
