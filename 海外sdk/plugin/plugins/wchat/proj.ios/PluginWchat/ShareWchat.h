#import <UIKit/UIKit.h>
#import "InterfaceShare.h"
#include "WXApi.h"

@interface ShareWchat : NSObject <InterfaceShare>
{
    
}

- (void) share: (NSMutableDictionary*) params;
+ (ShareWchat *)getObj;
@end
