//
//  UserUC.h
//  uc
//
//  Created by xiaolinfu on 14-3-8.
//  Copyright (c) 2014年 xiaolinfu. All rights reserved.
//

#ifndef uc_UserUC_h
#define uc_UserUC_h
#import <Foundation/Foundation.h>
#include "InterfaceUser.h"
@interface UserUC : NSObject<InterfaceUser>
    
@property BOOL debug;
@property BOOL  isLoginIn;
    /**
     methods of protocol : InterfaceIAP
     */
- (void) configDeveloperInfo : (NSMutableDictionary*) cpInfo;
- (void) login;
- (void) logout;
- (BOOL) isLogined;
- (NSString*) getSessionID;
- (void) setDebugMode: (BOOL) debug;
- (NSString*) getSDKVersion;
- (NSString*) getPluginVersion;
    
- (void) callback: (NSNotification *) notification;
- (void) showFloatButton;

@end


#endif
