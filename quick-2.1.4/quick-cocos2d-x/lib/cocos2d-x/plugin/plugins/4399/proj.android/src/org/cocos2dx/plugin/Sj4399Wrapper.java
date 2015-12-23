/****************************************************************************
Copyright (c) 2012-2013 cocos2d-x.org

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
package org.cocos2dx.plugin;

import java.util.Hashtable;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;

import com.ssjjsy.net.DialogError;
import com.ssjjsy.net.Ssjjsy;
import com.ssjjsy.net.SsjjsyDialogListener;
import com.ssjjsy.net.SsjjsyException;
import com.ssjjsy.net.SsjjsyVersionUpdateListener;

public class Sj4399Wrapper {

    private static boolean isInited = false;

    public static void initSDK(Context ctx,Hashtable<String, String> cpInfo) {
	try{
        if (isInited) {
            return;
        }
		String client_id = cpInfo.get("CLIENT_ID");
		String client_key = cpInfo.get("CLIENT_KEY");
		Ssjjsy.init(ctx, client_id, client_key);
		Ssjjsy.getInstance().activityOpenLog(ctx);
		isInited = true;
        } catch (Exception e) {
            isInited = false;
        }
    }

    public static boolean SDKInited() {
        return isInited;
    }

 
    public static String getSDKVersion() {
        return "3.4.2";
    }

}
