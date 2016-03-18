/****************************************************************************
 Copyright (c) 2014 Chukong Technologies Inc.
 
 http://www.cocos2d-x.org
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#import "ShareWchat.h"
#import "ShareWrapper.h"
#define OUTPUT_LOG(...)     if (self.debug) NSLog(__VA_ARGS__);

@implementation ShareWchat
#define BUFFER_SIZE 1024 * 100
static ShareWchat *shareWchatObj = nil;

+ (ShareWchat*)getObj
{
    return shareWchatObj;
}

- (void)share:(NSMutableDictionary*) params{
    shareWchatObj = self;
    
    NSString *RoomNumber = [params objectForKey:@"RoomNumber"];
    NSString *Invitetime = [params objectForKey:@"Invitetime"];
    NSString *RoomName = [params objectForKey:@"RoomName"];
    NSString *RoomType = [params objectForKey:@"RoomType"];
    
    WXMediaMessage *message = [WXMediaMessage message];
    message.title = @"PP扑克组局, 点击即可入局";
    
    NSString *content = [NSString stringWithFormat:@"%@\n%@\n%@\n%@",
                         RoomName,
                         [@"牌局类型:" stringByAppendingString:RoomType],
                         [@"房间号:" stringByAppendingString:RoomNumber],
                         [@"时间:" stringByAppendingString:Invitetime]];
    
    message.description = content;
    [message setThumbImage:[UIImage imageNamed:@"notification_postImage_appicon"]];
    
    WXAppExtendObject *ext = [WXAppExtendObject object];
    ext.extInfo = RoomNumber;
    ext.url = @"PPPokerGame://com.lein.PPPokerGame";
    
    Byte* pBuffer = (Byte *)malloc(BUFFER_SIZE);
    memset(pBuffer, 0, BUFFER_SIZE);
    NSData* data = [NSData dataWithBytes:pBuffer length:BUFFER_SIZE];
    free(pBuffer);
    
    ext.fileData = data;
    
    message.mediaObject = ext;
    
    SendMessageToWXReq* req = [[[SendMessageToWXReq alloc] init]autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;
    
    [WXApi sendReq:req];
}

@end