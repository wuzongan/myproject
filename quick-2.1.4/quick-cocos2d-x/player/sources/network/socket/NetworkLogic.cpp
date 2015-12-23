//
//  MacpLogic.cpp
//  Cocos2dx_V001
//
//  Created by admin on 11/9/12.
//
//

#include "NetworkLogic.h"

NetworkLogic::NetworkLogic():send_que(NULL), recv_que(NULL), inited(false)//,handler(0)
{
        
}

NetworkLogic::~NetworkLogic()
{
    if(send_que != NULL)
	    delete send_que;
		send_que = NULL;
    if(recv_que != NULL)
        delete recv_que;
		recv_que = NULL;
//    pthread_cancel(ptid);
//    pthread_kill(ptid, 0);
}

NetworkLogic* NetworkLogic::s_sharedInstance = NULL;
NetworkLogic* NetworkLogic::sharedInstance()
{
    if (s_sharedInstance == NULL)
    {
        s_sharedInstance = new NetworkLogic();
    }
    return s_sharedInstance;
}

bool NetworkLogic::start()
{
    if(init())
        return send_que->start() && recv_que->start();
    else
        return false;
}

bool NetworkLogic::init()
{
    if(inited)
        return true;
    
    if(send_que == NULL)
        send_que = new SendQueue();
    if(recv_que == NULL)
        recv_que = new RecvQueue();
    
    inited = true;
    return inited;
}

void NetworkLogic::_send_message(BytesBufferU16* pack)
{
    send_que->push(pack);
}

bool NetworkLogic::reconnect(string host, int port)
{
    // CCLog("reconnect--------%s,%s,%d,%d",NetSock::host.c_str(),host.c_str(),NetSock::port,port);
    if (NetSock::host == host && NetSock::port == port){
        return true;
    }
    else{
        NetSock::host = host;
        NetSock::port = port;
        return NetworkLogic::sharedInstance()->_reconnect();
    }

}

bool NetworkLogic::connect_server(string host, int port)
{
    NetSock::host = host;
    NetSock::port = port;
    return NetworkLogic::sharedInstance()->_connect_server();
}

bool NetworkLogic::clearSock()
{   NetSock::host = "";
    NetSock::port = 0;
	return NetworkLogic::sharedInstance()->_clearSock();
}

bool NetworkLogic::_connect_server()
{
    if(send_que->init() && recv_que->init())
        return send_que->choosSever(false);
    return false;
}

bool NetworkLogic::_reconnect()
{
    if(send_que->init() && recv_que->init())
	{
		send_que->reset();
		recv_que->reset();
        return send_que->choosSever(true);
	}
    return false;
}

bool NetworkLogic::_clearSock()
{
	if(send_que->init() && recv_que->init())
	{
        return send_que->close_socket();
	}
    return false;
}

void NetworkLogic::send_message(int length, char* pack)
{
    
    //
    BytesBufferU16 bb(pack,length);
    BytesBufferU16 * nbb;
    if(NetFilter::toNet(&bb,&nbb))
    {
        NetworkLogic::sharedInstance()->_send_message(nbb);
    }
}

bool NetworkLogic::is_connected()
{
    return NetSock::is_connected;
}

bool NetworkLogic::handle_message()
{
    BytesBufferU16* pack = recv_que->popData();
    if(pack == NULL)
        return false;
    try{
        callLuaFunction("scripts/proto/DealHandler.lua","deal",pack->data(), pack->size());
		if (pack)
			delete pack;
       /* if(handler)
        {
            CCLuaStack* stack=CCLuaEngine::defaultEngine()->getLuaStack();
            stack->clean();
            stack->pushString("unknown");
			char * str=new char[pack->size()+1];

			str[pack->size()]='\0';
            stack->pushString(pack->data());
			
            stack->executeFunctionByHandler(handler,1);
        }*/

    }catch(exception e){
        //在此处处理你之前没有捕获的一切异常
        e.what();
    }
    
    return true;
}


const char* NetworkLogic::callLuaFunction(const char* luaFileName, const char* functionName, const char * pack, int length){
    lua_State*  ls = CCLuaEngine::defaultEngine()->getLuaStack()->getLuaState();
    string str = CCFileUtils::sharedFileUtils()->fullPathForFilename(luaFileName);
	
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
    std::string code("require \"");
    code.append(str);
    code.append("\"");
    //CCLuaEngine::defaultEngine()->executeString(code.c_str());
    int isOpen = luaL_loadstring(ls,code.c_str());
    if(isOpen != 0){
            CCLOG("Open Lua Error: %i", isOpen);
            return NULL;
    }
#else
    int isOpen = luaL_dofile(ls, str.c_str());
    if(isOpen != 0){
        CCLOG("Open Lua Error: %i", isOpen);
        return NULL;
    }
#endif
    
    lua_getglobal(ls, functionName);
    
    lua_pushlstring(ls, pack, length);
   // if(lua_pcall(ls, 1, 1,handler)!=0)
	//	CCLOG("error");
	execute_lua_function(ls,1,false);
    const char* iResult = lua_tostring(ls, -1);
    return iResult;
}

string NetworkLogic::getSystemTime() {
	struct timeval tv;
    gettimeofday(&tv,NULL);
    int64_t time = (int64_t)tv.tv_sec * 1000 + (int64_t)tv.tv_usec / 1000;
    ostringstream oss;
    oss<<time;
    return oss.str();
}
