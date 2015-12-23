//
//  IAPWrapper.cpp
//  PluginProtocol
//
//  Created by xiaolinfu on 14-3-8.
//  Copyright (c) 2014年 zhangbin. All rights reserved.
//

#include "IAPWrapper.h"
#include "PluginUtilsIOS.h"
#include "ProtocolIAP.h"
extern "C" {
#include "lua.h"
}
#include "CCLuaEngine.h"
using namespace cocos2d::plugin;
using namespace cocos2d;
@implementation IAPWrapper

+ (void) onPayResult:(id) obj withRet:(UserPayResult) ret withMsg:(NSString*) msg
{
    PluginProtocol* pPlugin = PluginUtilsIOS::getPluginPtr(obj);
    ProtocolIAP* iap = dynamic_cast<ProtocolIAP*>(pPlugin);
    if (iap) {
        PayResultListener* listener = iap->getResultListener();
        int _luaListener = iap->getListener();
        if (NULL != listener)
        {
            const char* chMsg = [msg UTF8String];
            TProductInfo info;
            listener->onPayResult((PayResultCode)ret, chMsg,info);
        }
        else if(_luaListener != 0)
        {
            CCLuaStack* stack = CCLuaEngine::defaultEngine()->getLuaStack();
            stack->clean();
            PluginUtilsIOS::outputLog("lua=============","%d",_luaListener);
            stack->pushInt(ret);
            stack->pushString(msg.cString);
            stack->executeFunctionByHandler(_luaListener, 2);
        }
        else
        {
            PluginUtilsIOS::outputLog("Listener of plugin %s not set correctly", pPlugin->getPluginName());
        }
    } else {
        PluginUtilsIOS::outputLog("Can't find the C++ object of the User plugin");
    }
}

@end
