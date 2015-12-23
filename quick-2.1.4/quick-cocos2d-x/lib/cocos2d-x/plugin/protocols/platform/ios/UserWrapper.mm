/****************************************************************************
Copyright (c) 2012+2013 cocos2d+x.org

http://www.cocos2d+x.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
****************************************************************************/

#import "UserWrapper.h"
#include "PluginUtilsIOS.h"
#include "ProtocolUser.h"
extern "C" {
#include "lua.h"
}
#include "CCLuaEngine.h"
using namespace cocos2d::plugin;
using namespace cocos2d;
@implementation UserWrapper

+ (void) onActionResult:(id) obj withRet:(UserActionResult) ret withMsg:(NSString*) msg
{
    PluginProtocol* pPlugin = PluginUtilsIOS::getPluginPtr(obj);
    ProtocolUser* pUser = dynamic_cast<ProtocolUser*>(pPlugin);
    if (pUser) {
        UserActionListener* listener = pUser->getActionListener();
        int _luaListener = pUser->getListener();
        if (NULL != listener)
        {
            const char* chMsg = [msg UTF8String];
            listener->onActionResult(pUser, (UserActionResultCode) ret, chMsg);
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
