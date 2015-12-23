
#ifndef __CCBANIMATIONMANAGER_LUABINDING_H_
#define __CCBANIMATIONMANAGER_LUABINDING_H_
#include "cocos-ext.h"
USING_NS_CC_EXT;

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}
TOLUA_API int luaopen_CCBAnimationManager_luabinding(lua_State* tolua_S);

#endif // __CCBANIMATIONMANAGER_LUABINDING_H_
