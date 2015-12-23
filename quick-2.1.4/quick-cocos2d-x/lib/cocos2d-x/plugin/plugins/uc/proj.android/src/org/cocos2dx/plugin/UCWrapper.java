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

import cn.uc.gamesdk.UCCallbackListener;
import cn.uc.gamesdk.UCGameSDK;
import cn.uc.gamesdk.UCGameSDKStatusCode;
import cn.uc.gamesdk.UCLogLevel;
import cn.uc.gamesdk.UCOrientation;
import cn.uc.gamesdk.UCLoginFaceType;
import cn.uc.gamesdk.info.GameParamInfo;
import cn.uc.gamesdk.UCCallbackListenerNullException;
import cn.uc.gamesdk.UCFloatButtonCreateException;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.app.Activity;
import android.util.Log;

public class UCWrapper {
	
	public static final int UCWRAPPER_STATUSCODE_INIS_SDK = 99999;
	
	private static boolean isInited = false;

	private static String TAG = "UCWrapper";
	protected static void LogD(String msg) {
		Log.d(TAG, msg);
	}

	public static void initSDK(Context ctx, Hashtable<String, String> cpInfo, boolean isDebug, UCCallbackListener<String> listener) {
		if (isInited) {
			return;
		}

		try {
			final UCCallbackListener<String> curListener = listener;
			UCGameSDK.defaultSDK().setLogoutNotifyListener(new UCCallbackListener<String>() {
				@Override
				public void callback(int statuscode, String data) {
					switch (statuscode) {
					case UCGameSDKStatusCode.SUCCESS:
						mLogined = false;
						break;
					default:
						break;
					}

					if (null != curListener) {
						curListener.callback(statuscode, data);
					}
				}
			});

			String strCpId = cpInfo.get("UCCpID");
			String strGameId = cpInfo.get("UCGameID");
			String strServerId = cpInfo.get("UCServerID");

			int cpId = Integer.parseInt(strCpId);
			int gameId = Integer.parseInt(strGameId);
			int serverId = Integer.parseInt(strServerId);

			GameParamInfo gpi = new GameParamInfo();
			gpi.setCpId(cpId);
			gpi.setGameId(gameId);
			gpi.setServerId(serverId);

			if (isLandscape(ctx)) {
				UCGameSDK.defaultSDK().setOrientation(UCOrientation.LANDSCAPE);
			}

			// 设置登录界面
			// USE_WIDGET - 简版登录界面
			// USE_STANDARD - 标准版登录界面
			UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_WIDGET);
			//UCGameSDK.defaultSDK().setLoginUISwitch(UCLoginFaceType.USE_STANDARD);

			UCGameSDK.defaultSDK().initSDK(ctx, UCLogLevel.ERROR, isDebug, gpi, new UCCallbackListener<String>() {
				@Override
				public void callback(int code, String msg) {
					int nStatus = 0;
					System.out.println("msg:" + msg); 
					switch (code) {
					case UCGameSDKStatusCode.SUCCESS:
						isInited = true;
						nStatus = UCWRAPPER_STATUSCODE_INIS_SDK;
						break;
					case UCGameSDKStatusCode.INIT_FAIL:
					default:
						isInited = false;
						break;
					}
					
					if (null != curListener) {
						switch(nStatus){
						case UCWRAPPER_STATUSCODE_INIS_SDK:
							curListener.callback(nStatus, msg);
							break;
						default:
							curListener.callback(code, msg);
							break;
						}
					}
				}
			});
		} catch (Exception e) {
			isInited = false;
		}
	}

	public static boolean SDKInited() {
		return isInited;
	}

	public static boolean isLandscape(Context ctx)
	{
		Configuration config = ctx.getResources().getConfiguration();
		int orientation = config.orientation;

		if (orientation != Configuration.ORIENTATION_LANDSCAPE &&
				orientation != Configuration.ORIENTATION_PORTRAIT)
		{
			orientation = Configuration.ORIENTATION_PORTRAIT;
		}

		return (orientation == Configuration.ORIENTATION_LANDSCAPE);
	}

	public static String getSDKVersion() {
		return "3.1.3";
	}

	private static boolean mLogined = false;
	private static boolean waitHandle = false;
	public static void userLogin(Activity ctx, UCCallbackListener<String> listener) {
		try {
			waitHandle = true;
			final UCCallbackListener<String> curListener = listener;
			UCGameSDK.defaultSDK().login(ctx, new UCCallbackListener<String>() {
				@Override
				public void callback(int code, String msg) {
					if (! waitHandle) {
						return;
					}

					switch(code) {
					case UCGameSDKStatusCode.SUCCESS:
						mLogined = true;
						break;
					default:
						mLogined = false;
						break;
					}
					waitHandle = false;
					curListener.callback(code, msg);
				}
			});
		} catch (Exception e) {
			mLogined = false;
			waitHandle = false;
			listener.callback(UCGameSDKStatusCode.FAIL, "Login Failed");
		}
	}

	public static void userLogout() {
		try {
			UCGameSDK.defaultSDK().logout();
			mLogined = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isLogined() {
		return mLogined;
	}


	public static void exitSDK(){
		try{
			UCGameSDK.defaultSDK().exitSDK();
			LogD("==== Call exitSDK Success! ======");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void createFloatButton(Activity ctx){
		try{
			UCGameSDK.defaultSDK().createFloatButton(ctx,
					new UCCallbackListener<String>() {
				@Override
				public void callback(int statuscode, String data){
					LogD("==== createFloatButton  Callback statuscode == " + statuscode
							+ "; data == " + data);
					switch(statuscode)
					{
					case UCGameSDKStatusCode.SDK_OPEN:
						// 将要打开SDK界面
						break;
					case UCGameSDKStatusCode.SDK_CLOSE:
						// 将要关闭SDK界面，返回游戏画面
						break;
					}
				}
			});
			LogD("==== Call createFloatButton Success! ==== ");
		} catch (UCCallbackListenerNullException e) {
			e.printStackTrace();
		} catch (UCFloatButtonCreateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void showFloatButton(Activity ctx, double x, double y, boolean visible) {
		try {
			UCGameSDK.defaultSDK().showFloatButton(ctx, x, y, visible);
			LogD("====  Call showFloatButton Success! ==== ");
		} catch (UCCallbackListenerNullException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void destoryFloatButton(Activity ctx) {
		 //悬浮按钮销毁功能
		UCGameSDK.defaultSDK().destoryFloatButton(ctx);
		LogD("====  Call destoryFloatButton Success! ==== ");
	}
}
