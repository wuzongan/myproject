
#ifndef __CUSTOMFONT_LUABINDING_H_
#define __CUSTOMFONT_LUABINDING_H_

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}
#include "CustomFont.h"
TOLUA_API int luaopen_CustomFont_luabinding(lua_State* tolua_S);

#endif // __CUSTOMFONT_LUABINDING_H_
