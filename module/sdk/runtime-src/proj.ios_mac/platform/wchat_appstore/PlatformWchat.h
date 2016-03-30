#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "WXApi.h"
#import "InterfaceUser.h"
#import "InterfaceShare.h"

@interface PlatformWchat : NSObject <UIApplicationDelegate, InterfaceUser,InterfaceShare, WXApiDelegate>{
    
}

+ (PlatformWchat *) sharedInstance;
- (void) initSdk;
- (BOOL) openUrl:(NSURL *)url;
@end
