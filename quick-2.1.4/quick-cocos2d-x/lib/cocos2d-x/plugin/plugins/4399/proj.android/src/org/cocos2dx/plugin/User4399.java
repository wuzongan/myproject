package org.cocos2dx.plugin;

import java.util.Hashtable;
import android.os.Bundle;
import android.content.Context;
import android.util.Log;
import android.app.Activity;

import com.ssjjsy.net.DialogError;
import com.ssjjsy.net.Ssjjsy;
import com.ssjjsy.net.SsjjsyPluginListener;
import com.ssjjsy.net.Plugin;
import com.ssjjsy.sdk.SdkListener;
import com.ssjjsy.net.SsjjsyDialogListener;
import com.ssjjsy.net.SsjjsyException;
import com.ssjjsy.net.SsjjsyVersionUpdateListener;
import android.widget.Toast;
public class User4399 implements InterfaceUser {

    private static Activity mContext = null;
    protected static String TAG = "User4399";
    private static InterfaceUser mAdapter = null;
	private static String timestamp;
	private static String signStr;
	private static String suid;
	private static String targetServerId;
	private static String username;
	private static String verifyToken;
    protected static void LogE(String msg, Exception e) {
        Log.e(TAG, msg, e);
        e.printStackTrace();
    }

    private static boolean isDebug = false;
    protected static void LogD(String msg) {
        if (isDebug) {
            Log.d(TAG, msg);
        }
    }

    public User4399(Context context) {
        mContext = (Activity)context;
        mAdapter = this;
    }

    @Override
    public void configDeveloperInfo(Hashtable<String, String> cpInfo) {
        final Hashtable<String, String> curInfo = cpInfo;
        PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
               Sj4399Wrapper.initSDK(mContext,curInfo);
			   UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_INIT_SUCCEED, "SDK init failed");
            }
        });
    }

    @Override
    public void login() {
        if (! Sj4399Wrapper.SDKInited()) {
            UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_FAILED, "SDK init failed");
            return;
        }
        /*if (isLogined()){
			UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_SUCCEED, "already logined");
			return;
		}*/
        PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
				Ssjjsy.getInstance().activityBeforeLoginLog(mContext);
                Ssjjsy.getInstance().authorize(mContext, new SsjjsyDialogListener() {
					@Override
					public void onComplete(Bundle values) {
						
						timestamp = values.getString("timestamp");
						signStr = values.getString("signStr");
						suid = values.getString("suid");
						targetServerId = values.getString("targetServerId");
						username = values.getString("username");
						verifyToken = values.getString("verifyToken");
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_SUCCEED, "success");
					}

					@Override
					public void onError(DialogError e) {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_FAILED, "error");
					}

					@Override
					public void onCancel() {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_FAILED, "cancel");
					}

					@Override
					public void onSsjjsyException(SsjjsyException e) {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_LOGIN_FAILED, "Exception");
					}
				});
            }
        });
    }

    @Override
    public void logout() {
        LogD("plantform do not have  logout!");
    }

    @Override
    public boolean isLogined() {
        return Ssjjsy.getInstance().isLogin();
    }

    @Override
    public String getSessionID() {
        LogD("getSessionID() invoked!");
		return suid;
    }

    @Override
    public void setDebugMode(boolean debug) {
        isDebug = debug;
    }

    @Override
    public String getPluginVersion() {
        return "0.2.0";
    }

    @Override
    public String getSDKVersion() {
        return Sj4399Wrapper.getSDKVersion();
    }
	public String getTimestamp() {
        return timestamp;
    }
	public String getSignStr() {
        return signStr;
    }
	public String getTargetServerId() {
        return targetServerId;
    }
	public String getUsername() {
        return username;
    }
	public String getVerifyToken() {
        return verifyToken;
    }
	public String checkUpdate() {
		PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
				Ssjjsy.getInstance().versionUpdate(mContext, new SsjjsyVersionUpdateListener() {

					@Override
					public void onNotNewVersion() {
						
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_NO_UPDATE, "already lastest version");
						Toast.makeText(mContext,"已是最新版本", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onNotSDCard() {
						
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_NO_UPDATE, "onNotSDCard");
						Toast.makeText(mContext,"美有检测到SD卡", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCancelForceUpdate() {
						
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_UPDATE_NEW, "onCancelForceUpdate");
						Toast.makeText(mContext,"取消强制更新", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCancelNormalUpdate() {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_UPDATE_NEW, "onCancelNormalUpdate");
						Toast.makeText(mContext,"取消一般更新", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onCheckVersionFailure() {
						
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_NO_UPDATE, "already latest version");
						Toast.makeText(mContext,"版本检测失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onForceUpdateLoading() {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_UPDATE_NEW, "onForceUpdateLoading");
						Toast.makeText(mContext,"强制更新中", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onNormalUpdateLoading() {
						
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_UPDATE_NEW, "onNormalUpdateLoading");
						Toast.makeText(mContext,"一般更新中", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onNetWorkError() {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_NO_UPDATE, "NetworkError");
						Toast.makeText(mContext,"网络异常", Toast.LENGTH_LONG).show();
						
					}

					@Override
					public void onSsjjsyException(SsjjsyException e) {
						UserWrapper.onActionResult(mAdapter, UserWrapper.ACTION_RET_NO_UPDATE, "onSsjjsyException");
						Toast.makeText(mContext,"异常", Toast.LENGTH_LONG).show();
						
					}
				});
			}
		});
		return "";
    }
	public void setServerId(String id){
		LogD("setServerId");
		Ssjjsy.getInstance().setServerId(id);
		LogD("setServerId(id);");
		Ssjjsy.getInstance().loginServerLog(mContext);
		LogD("plantfologinServerLog(mContext);!");
	}
	private static native boolean nativeScreenShot(String saveName, int startX, int startY, int width, int height);
	
	public String showFloatButton(){
		PluginWrapper.runOnMainThread(new Runnable() {
            @Override
            public void run() {
					Ssjjsy.getInstance().setPluginListener(new SsjjsyPluginListener() {
						@Override
						public void onSuccess() {
							Log.i(TAG, "SsjjsyPluginListener.onSuccess()");
							Plugin.getInstance().setSdkListener(new SdkListener() {
								@Override
								public boolean onScreenShot(final String saveName, final int startX, final int startY, final int width,
										final int height) {
									Log.i(TAG, "mSdkListener.onScreenShot()");
									PluginWrapper.runOnGLThread(new Thread() {
										@Override
										public void run() {
											boolean isSuccessed = nativeScreenShot(saveName, startX, startY, width, height);
											Log.i(TAG, "Plugin.getInstance().postResult(), " + isSuccessed);
											Plugin.getInstance().postResult(isSuccessed, saveName);
										}
									});
									return false;
								}
							});
							Plugin.getInstance().show(mContext);
						}
					});
			}
		});
		return "";
	}
}
