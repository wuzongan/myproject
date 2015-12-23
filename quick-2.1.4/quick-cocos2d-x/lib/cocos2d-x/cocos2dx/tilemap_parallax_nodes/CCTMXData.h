//
//  CCTMXData.h
//  Cocos2dx_V001
//
//  Created by admin on 12-12-21.
//
//

#ifndef __Cocos2dx_V001__CCTMXData__
#define __Cocos2dx_V001__CCTMXData__

#include <iostream>
#include "cocos2d.h"

USING_NS_CC;
using namespace std;

class CCTMXData {
	
	CC_SYNTHESIZE(CCTMXTiledMap *, map, Map);
	CC_SYNTHESIZE(CCDictionary *, allResourceDic, AllResourceDic);
    std::map<int, CCPoint> tileOffSetDic;
	CC_SYNTHESIZE(bool, floorFinished, FloorFinished);
	
	// debug
	CC_SYNTHESIZE(uint64_t, startTime, StartTime);
    static CCTMXData* sharedTMXData();
    static CCTMXData* s_sharedTMXData;
    
public:
	CCTMXData() : floorFinished(false), map(NULL) {
		allResourceDic	= new CCDictionary;
	}
	~CCTMXData() {
		allResourceDic->release();
	}

	void clean() {
		tileOffSetDic.clear();
		allResourceDic->removeAllObjects();
		floorFinished = false;
	}
    
    
};

#endif /* defined(__Cocos2dx_V001__CCTMXData__) */
