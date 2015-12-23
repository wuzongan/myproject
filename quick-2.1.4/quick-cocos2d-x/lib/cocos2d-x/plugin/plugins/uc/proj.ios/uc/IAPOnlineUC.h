//
//  uc.h
//  uc
//
//  Created by xiaolinfu on 14-3-7.
//  Copyright (c) 2014年 xiaolinfu. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "InterfaceIAP.h"

@interface IAPOnlineUC : NSObject<InterfaceIAP>

@property BOOL debug;

/**
 methods of protocol : InterfaceIAP
 */
- (void) configDeveloperInfo: (NSMutableDictionary*) cpInfo;
- (void) payForProduct: (NSMutableDictionary*) profuctInfo;
- (void) setDebugMode: (BOOL) debug;
- (NSString*) getSDKVersion;
- (NSString*) getPluginVersion;

-(void) callback: (NSNotification *) notification;
@end
