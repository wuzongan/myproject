#import <UIKit/UIKit.h>
#import <Foundation/Foundation.h>
#import "InterfaceUser.h"
#import "InterfaceShare.h"
#import "WXApi.h"
@interface UserWchat : NSObject <InterfaceUser>{

}
- (void) login;
- (void) callback:(NSString*) msg;
+ (UserWchat *)getObj;
@end
