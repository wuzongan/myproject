//
//  MessageUpdate.cpp
//  gc_2dx_v001
//
//  Created by admin on 1/11/13.
//
//
//#define SOCKET_IP "192.168.1.133"
//#define SOCKET_PORT 8080

#include "MessageUpdate.h"
bool MessageUpdate::init(void){
//    NetSock::host = SOCKET_IP;
//    NetSock::port = SOCKET_PORT;
    while (true) {
        if(NetworkLogic::sharedInstance()->start()){
            this->onEnter();
            schedule(schedule_selector(MessageUpdate::update));
            break;
        }
    }
    
    return true;
}

void MessageUpdate::update(float t){
    NetworkLogic::sharedInstance()->handle_message();
}

MessageUpdate::~MessageUpdate()
{
	unschedule(schedule_selector(MessageUpdate::update));
	if(NetworkLogic::sharedInstance())
	{
		delete NetworkLogic::sharedInstance();
		NetworkLogic::s_sharedInstance = NULL;
	}
}