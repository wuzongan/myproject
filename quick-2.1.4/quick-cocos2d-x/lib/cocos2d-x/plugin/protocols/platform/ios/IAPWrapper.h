//
//  IAPWrapper.h
//  PluginProtocol
//
//  Created by xiaolinfu on 14-3-8.
//  Copyright (c) 2014年 zhangbin. All rights reserved.
//

#ifndef PluginProtocol_IAPWrapper_h
#define PluginProtocol_IAPWrapper_h
#import <Foundation/Foundation.h>

typedef enum {
    PAYRESULT_SUCCESS = 0,
	PAYRESULT_FAIL    = 1,
	PAYRESULT_CANCEL  = 2,
	PAYRESULT_TIMEOUT = 3,
} UserPayResult;

@interface IAPWrapper : NSObject
    {
        
    }
    
+ (void) onPayResult:(id) obj withRet:(UserPayResult) ret withMsg:(NSString*) msg;
    
    @end
#endif
