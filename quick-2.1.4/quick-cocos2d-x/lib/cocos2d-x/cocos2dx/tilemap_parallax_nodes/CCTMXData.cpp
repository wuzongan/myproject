//
//  CCTMXData.cpp
//  Cocos2dx_V001
//
//  Created by admin on 12-12-21.
//
//

#include "CCTMXData.h"
CCTMXData* CCTMXData::s_sharedTMXData = NULL;
CCTMXData* CCTMXData::sharedTMXData()
{
    if (s_sharedTMXData == NULL)
    {
        s_sharedTMXData = new CCTMXData();
    }
    return s_sharedTMXData;
}