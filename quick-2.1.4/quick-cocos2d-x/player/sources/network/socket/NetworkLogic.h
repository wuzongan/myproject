//
//  MacpLogic.h
//  Cocos2dx_V001
//
//  Created by admin on 11/9/12.
//
//

#ifndef __Cocos2dx_V001__NetworkLogic__
#define __Cocos2dx_V001__NetworkLogic__

#include <string>
#include "SendQueue.h"
#include "RecvQueue.h"
#include "BytesBuffer.h"
#include "NetFilter.h"

extern "C" {
#include "lua.h"
#include "lualib.h"
#include "lauxlib.h"
}
#include "CCLuaEngine.h"


using namespace std;


class NetworkLogic
{
public:
    NetworkLogic();
    virtual ~NetworkLogic();
    bool init();
    bool start();     
    bool handle_message();
    
    static bool reconnect(string host, int port);
    static bool connect_server(string host, int port);
	static bool clearSock();
    
    static void send_message(int length, char* pack);
  
    static NetworkLogic* sharedInstance();
    static NetworkLogic* s_sharedInstance;
    
    bool is_connected();

    string getSystemTime();
private:    
    void _send_message(BytesBufferU16* pack);
    bool _reconnect();
    bool _connect_server();
	bool _clearSock();

    
    SendQueue *send_que;
    RecvQueue *recv_que;

    bool inited;
    
    const char* callLuaFunction(const char* luaFileName,const char* functionName, const char * pack, int length);

};

#endif /* defined(__Cocos2dx_V001__MacpLogic__) */


