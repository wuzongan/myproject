#import "UserWchat.h"
#import "UserWrapper.h"
#import "ParseUtils.h"


@implementation UserWchat

static UserWchat *userWchatObj = nil;

+ (UserWchat*)getObj
{
    return userWchatObj;
}

- (void) login{
    userWchatObj = self;
    SendAuthReq* req =[[[SendAuthReq alloc ] init ] autorelease ];
    req.scope = @"snsapi_userinfo" ;
    req.state = @"123" ;
    //第三方向微信终端发送一个SendAuthReq消息结构
    [WXApi sendReq:req];
}

- (void) callback:(NSString*) msg
{
    [UserWrapper onActionResult:self withRet:kLoginSucceed withMsg:msg];
}

@end
