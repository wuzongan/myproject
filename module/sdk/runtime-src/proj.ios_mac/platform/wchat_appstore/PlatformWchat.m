#import "PlatformWchat.h"
#import "UserWrapper.h"
#import "ParseUtils.h"
#import "UserWchat.h"
#import "ShareWrapper.h"
#import "ShareWchat.h"

@interface PlatformWchat ()

@end
@implementation PlatformWchat


static PlatformWchat *s_sharedInstance = nil;
+ (PlatformWchat *) sharedInstance
{
    if (!s_sharedInstance)
    {
        s_sharedInstance = [[PlatformWchat alloc] init];
    }
    return s_sharedInstance;
}



- (void) initSdk
{
    [WXApi registerApp:@"wx2211f51ef5f57fb7"];
}

- (BOOL) openUrl:(NSURL *)url
{
    return [WXApi handleOpenURL:url delegate:self];
}


#pragma mark weixin
-(void) onReq:(BaseReq*)req
{
    if([req isKindOfClass:[GetMessageFromWXReq class]])
    {
        // 微信请求App提供内容， 需要app提供内容后使用sendRsp返回
        NSString *strTitle = [NSString stringWithFormat:@"微信请求App提供内容"];
        NSString *strMsg = @"微信请求App提供内容，App要调用sendResp:GetMessageFromWXResp返回给微信";
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:strTitle message:strMsg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        alert.tag = 1000;
        [alert show];
        [alert release];
    }
    else if([req isKindOfClass:[ShowMessageFromWXReq class]])
    {
        ShowMessageFromWXReq* temp = (ShowMessageFromWXReq*)req;
        WXMediaMessage *msg = temp.message;
        
        //显示微信传过来的内容
        WXAppExtendObject *obj = msg.mediaObject;
        
        NSString *strTitle = [NSString stringWithFormat:@"微信请求App显示内容"];
        NSString *strMsg = [NSString stringWithFormat:@"标题：%@ \n内容：%@ \n附带信息：%@ \n缩略图:%lu bytes\n\n", msg.title, msg.description, obj.extInfo, (unsigned long)msg.thumbData.length];
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:strTitle message:strMsg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
    }
    else if([req isKindOfClass:[LaunchFromWXReq class]])
    {
        //从微信启动App
        NSString *strTitle = [NSString stringWithFormat:@"从微信启动"];
        NSString *strMsg = @"这是从微信启动的消息";
        
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:strTitle message:strMsg delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
        [alert show];
        [alert release];
    }
}

-(void) onResp:(BaseResp*)resp
{
    if([resp isKindOfClass:[SendMessageToWXResp class]])
    {
        if(resp.errCode != 0){
            [ShareWrapper onShareResult:[ShareWchat getObj] withRet:kShareCancel withMsg:@"share failure!"];
        }
        else{
            [ShareWrapper onShareResult:[ShareWchat getObj] withRet:kShareSuccess withMsg:@"share success!"];
        }
    }
    else if([resp isKindOfClass:[SendAuthResp class]]){
        SendAuthResp *aresp = (SendAuthResp *)resp;
        if (aresp.errCode== 0) {
            [UserWrapper onActionResult:[UserWchat getObj] withRet:kLoginSucceed withMsg:aresp.code];
        }
    }
}
@end
