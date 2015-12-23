
#ifndef __PLUGIN_LUABINDING_H_
#define __PLUGIN_LUABINDING_H_

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}

TOLUA_API int luaopen_Plugin_luabinding(lua_State* tolua_S);

#endif // __PLUGIN_LUABINDING_H_
