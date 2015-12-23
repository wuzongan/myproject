//
//  MessageUpdate.h
//  gc_2dx_v001
//
//  Created by admin on 1/11/13.
//
//

#ifndef __gc_2dx_v001__MessageUpdate__
#define __gc_2dx_v001__MessageUpdate__

#include <iostream>
#include "cocos2d.h"
#include "NetworkLogic.h"
USING_NS_CC;

class MessageUpdate : public CCNode{
public:

	MessageUpdate(){};
	virtual ~MessageUpdate();
    void update(float t);
	virtual bool init(void);
    CREATE_FUNC(MessageUpdate);
    
    
};
#endif /* defined(__gc_2dx_v001__MessageUpdate__) */
