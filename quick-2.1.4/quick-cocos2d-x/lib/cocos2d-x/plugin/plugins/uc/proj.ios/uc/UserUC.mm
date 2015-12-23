//
//  UserUC.cpp
//  uc
//
//  Created by xiaolinfu on 14-3-8.
//  Copyright (c) 2014年 xiaolinfu. All rights reserved.
//

#include "UserUC.h"
#import <UCGameSdk/UCGameSdk.h>
#import "UserWrapper.h"


@implementation UserUC
    
@synthesize debug = __debug;
@synthesize isLoginIn=__isLoginIn;
    
- (void) configDeveloperInfo : (NSMutableDictionary*) cpInfo
{
    // 添加 Observer 来监听 SDK 初始化事件,以便在 SDK 初始化完成时进行后续处理。
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callback:) name:UCG_SDK_MSG_SDK_INIT_FIN object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callback:) name:UCG_SDK_MSG_LOGIN_FIN  object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callback:) name:UCG_SDK_MSG_EXIT_WITHOUT_LOGIN object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(callback:) name:UCG_SDK_MSG_LOGOUT object:nil];
    
    // 设置游戏相关参数
    UCGameSdk *sdk = [UCGameSdk defaultSDK];
    // 设置游戏相关参数
    //sdk.cpId = 20087;
    //sdk.gameId = 119474;
    //sdk.serverId= 1333;
    self.isLoginIn = NO;
    sdk.cpId = [(NSString*)[cpInfo objectForKey:@"UCCpID"] integerValue];
    sdk.gameId = [(NSString*)[cpInfo objectForKey:@"UCGameID"] integerValue];
    sdk.serverId= [(NSString*)[cpInfo objectForKey:@"UCServerID"] integerValue];
    sdk.isDebug = self.debug;
    // 设置游戏合作商编号,该编号在游戏接入时由 UC 分配
    // 设置游戏编号,该编号在游戏接入时由 UC 分配
    // 设置游戏服务器 ID,该编号由所在的游戏分区决定
    // 设置联测模式或正式生产模式:YES:联测模式,SDK 会自动连接到 SDK平台的联测环境;NO:正式生产模式,SDK 会自动连接到 SDK 平台的正式生产环境。
    sdk.logLevel = UCLOG_LEVEL_ERR;
    // 设置日志级别为,有 3 个日志级别:
    // UCGLOG_LEVEL_ERR:错误级
    // UCGLOG_LEVEL_WARNING:警告级,包含错误级 // UCGLOG_LEVEL_DEBUG:调试级,包含以上两级
    sdk.orientation = UC_LANDSCAPE;
    // 设置 SDK 界面屏幕方向,此处设为横屏(包括向左,向右) s
    sdk.allowGameUserLogin = YES; // 是否支持游戏老帐号登录,默认为不支持,此处设置为支持
    //sdk.gameUserName = @””;// 游戏老帐号帐号名称,在支持游戏老帐号登录时才需设置
    sdk.allowChangeAccount = YES; // 是否允许用户注销帐号
    //设置悬浮按钮是否显示及位置
    //默认不显示 //位置:x只能是0或100,0代表屏幕左边,100代表右边;y为0~100,0表示屏幕最上面,100表示最下面 //设置显示悬浮按钮时,默认是在屏幕右边的中间,即(100,50),如果设置的范围不对,也按此值显示
    sdk.isShowFloatButton = YES; // 是否显示悬浮按钮(必须显示)
    sdk.floatButtonPosition = CGPointMake(0, 100); // 设置悬浮按钮的位置
    [sdk initSDK]; // 调用 SDK 初始化方法
}
- (void) login
{
        [[UCGameSdk defaultSDK] login];
}
- (void) logout
    {
        [[UCGameSdk defaultSDK] logout];
    }
- (BOOL) isLogined
    {
        return self.isLoginIn;
    }
- (NSString*) getSessionID
    {
        return [[UCGameSdk defaultSDK] sid];
    }
- (void) setDebugMode: (BOOL) debug
    {
        self.debug =debug;
    }
- (NSString*) getSDKVersion
    {
        return @"2.0";
    }
- (NSString*) getPluginVersion
    {
        return @"1.0";
    }
    
-(void) callback: (NSNotification *) notification
    {
        if([notification.name  isEqual: UCG_SDK_MSG_SDK_INIT_FIN])
            [UserWrapper onActionResult:self withRet:kInitFinish withMsg:notification.name];
        else if([notification.name  isEqual: UCG_SDK_MSG_LOGIN_FIN])
        {
            [UserWrapper onActionResult:self withRet:kLoginSucceed withMsg:notification.name];
            self.isLoginIn =YES;
        }
        else if([notification.name  isEqual: UCG_SDK_MSG_EXIT_WITHOUT_LOGIN])
            [UserWrapper onActionResult:self withRet:kLoginFailed withMsg:notification.name];
        else if([notification.name  isEqual: UCG_SDK_MSG_LOGOUT])
        {
            [UserWrapper onActionResult:self withRet:kLogoutSucceed withMsg:notification.name];
            self.isLoginIn =NO;
        }
    }
- (void) showFloatButton
    {
        [[UCGameSdk defaultSDK] showFloatButton:YES];
    }

@end