
#ifndef __DATACENTER_LUABINDING_H_
#define __DATACENTER_LUABINDING_H_

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}
#include "DataCenter.h"
TOLUA_API int luaopen_DataCenter_luabinding(lua_State* tolua_S);

#endif // __DATACENTER_LUABINDING_H_
