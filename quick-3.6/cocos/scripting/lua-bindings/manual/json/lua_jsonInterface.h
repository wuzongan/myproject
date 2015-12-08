//
//  lua_luaCCBNode.h
//  miniDotaClient
//
//  Created by yock on 13-1-24.
//  Copyright (c) 2013年 __MyCompanyName__. All rights reserved.
//

#ifndef miniDotaClient_lua_JsonInterface_h
#define miniDotaClient_lua_JsonInterface_h

extern "C" {
#include "lua.h"
#include "tolua++.h"
#include "tolua_fix.h"
}

TOLUA_API int luaopen_util_json (lua_State* tolua_S) ;

#endif
