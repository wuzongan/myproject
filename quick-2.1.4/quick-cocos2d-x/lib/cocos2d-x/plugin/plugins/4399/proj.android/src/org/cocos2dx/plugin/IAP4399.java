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

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ssjjsy.net.DialogError;
import com.ssjjsy.net.Ssjjsy;
import com.ssjjsy.net.SsjjsyDialogListener;
import com.ssjjsy.net.SsjjsyException;
import com.ssjjsy.net.SsjjsyVersionUpdateListener;

public class IAP4399 implements InterfaceIAP {

    private static final String LOG_TAG = "IAP4399";
    private static Activity mContext = null;
    private static IAP4399 mAdapter = null;
    private static boolean bDebug = false;

    protected static void LogE(String msg, Exception e) {
        Log.e(LOG_TAG, msg, e);
        e.printStackTrace();
    }

    protected static void LogD(String msg) {
        if (bDebug) {
            Log.d(LOG_TAG, msg);
        }
    }

    public IAP4399(Context context) {
        mContext = (Activity) context;
        mAdapter = this;
    }

    @Override
    public void configDeveloperInfo(Hashtable<String, String> cpInfo) {
        final Hashtable<String, String> curInfo = cpInfo;
        PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                Sj4399Wrapper.initSDK(mContext,curInfo);
            }
        });
    }
    @Override
    public void payForProduct(Hashtable<String, String> info) {
        LogD("payForProduct invoked " + info.toString());
    
		String strServerID = info.get("ServerID");
		if (null != strServerID && ! TextUtils.isEmpty(strServerID)) {
			Ssjjsy.getInstance().setServerId(strServerID);
			LogD("payForProduct  setServerId");
		}
		int money = 0;
		try {
			String strMoney = info.get("money");
			if (null != strMoney && ! TextUtils.isEmpty(strMoney)) {
				money = (int)((float)Float.valueOf(strMoney));
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	
		final int payAmount = money;
		final String descript = info.get("productDesc");
        PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
				if(!Ssjjsy.getInstance().isLogin()){
					IAPWrapper.onPayResult(mAdapter, IAPWrapper.PAYRESULT_FAIL, "not login");
					return;
				}
				Ssjjsy.getInstance().pay(mContext,payAmount,descript);
				IAPWrapper.onPayResult(mAdapter, IAPWrapper.PAYRESULT_SUCCESS, "enter Pay Succeed");
            }
        });
    }

    @Override
    public void setDebugMode(boolean debug) {
        bDebug = debug;
    }

    @Override
    public String getSDKVersion() {
        return Sj4399Wrapper.getSDKVersion();
    }

  

    @Override
    public String getPluginVersion() {
        return "0.2.0";
    }

}
