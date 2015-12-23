
#include "native/CCNative.h"
#include <jni/JniHelper.h>
#include <jni.h>

NS_CC_EXTRA_BEGIN
#define  CLASS_NAME   "org/cocos2dx/lib/Cocos2dxHelper"

//  activity indicator

void CCNative::showActivityIndicator(void)
{
    CCLOG("CCNative::showActivityIndicator() - not support this platform.");
}

void CCNative::hideActivityIndicator(void)
{
    CCLOG("CCNative::hideActivityIndicator() - not support this platform.");
}


//  alert view

void CCNative::createAlert(const char* title,
                           const char* message,
                           const char* cancelButtonTitle)
{
   // CCLOG("CCNative::createAlert() - not support this platform.");
   JniMethodInfo methodInfo;
    if (JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "showAlertDialog", "(Ljava/lang/String;Ljava/lang/String;)V"))
    {
		jstring jtitle = methodInfo.env->NewStringUTF(title);
		jstring jmessage = methodInfo.env->NewStringUTF(message);
        methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID,jtitle,jmessage);
		methodInfo.env->DeleteLocalRef(jtitle);
		methodInfo.env->DeleteLocalRef(jmessage);
    }
    methodInfo.env->DeleteLocalRef(methodInfo.classID);
}

int CCNative::addAlertButton(const char* buttonTitle)
{
    CCLOG("CCNative::addAlertButton() - not support this platform.");
}

#if CC_LUA_ENGINE_ENABLED > 0
int CCNative::addAlertButtonLua(const char* buttonTitle)
{
    CCLOG("CCNative::addAlertButtonLua() - not support this platform.");
}
#endif

void CCNative::showAlert(CCAlertViewDelegate* delegate)
{
    CCLOG("CCNative::showAlert() - not support this platform.");
}

#if CC_LUA_ENGINE_ENABLED > 0
void CCNative::showAlertLua(LUA_FUNCTION listener)
{
    CCLOG("CCNative::showAlertLua() - not support this platform.");
	
}
#endif

void CCNative::cancelAlert(void)
{
    CCLOG("CCNative::cancelAlert() - not support this platform.");
}


//  misc

void CCNative::openURL(const char* url)
{
    //CCLOG("CCNative::openURL() - not support this platform.");
	JniMethodInfo methodInfo;
    if (JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "openURL", "(Ljava/lang/String;)V"))
    {
		jstring jstr = methodInfo.env->NewStringUTF(url);
        methodInfo.env->CallStaticVoidMethod(methodInfo.classID, methodInfo.methodID,jstr);
		methodInfo.env->DeleteLocalRef(jstr);
    }
    methodInfo.env->DeleteLocalRef(methodInfo.classID);
}

const string CCNative::getInputText(const char* title, const char* message, const char* defaultValue)
{
    CCLOG("CCNative::getInputText() - not support this platform.");
    return string("");
}


//  OpenUDID

const string CCNative::getOpenUDID(void)
{
    //CCLOG("CCNative::getOpenUDID() - not support this platform.");
   // return string("");
   JniMethodInfo methodInfo;
    jstring jstr;
    if (JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "getDeviceId", "()Ljava/lang/String;"))
    {
        jstr = (jstring)methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
    }
    methodInfo.env->DeleteLocalRef(methodInfo.classID);
    
    string deviceModel(methodInfo.env->GetStringUTFChars(jstr, NULL));
    return deviceModel;
}

const string CCNative::getDeviceName(void)
{
    // CCLOG("CCNative::getDeviceName() - not support this platform.");
    // return string("");
    JniMethodInfo methodInfo;
    jstring jstr;
    if (JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "getDeviceModel", "()Ljava/lang/String;"))
    {
        jstr = (jstring)methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
    }
    methodInfo.env->DeleteLocalRef(methodInfo.classID);
    
    string deviceModel(methodInfo.env->GetStringUTFChars(jstr, NULL));
    return deviceModel;
}

const string CCNative::getSystemVersion(void)
{
    //CCLOG("CCNative::getSystemVersion() - not support this platform.");
    //return string("");
	JniMethodInfo methodInfo;
    jstring jstr;
    if (JniHelper::getStaticMethodInfo(methodInfo, CLASS_NAME, "getSystemVersion", "()Ljava/lang/String;"))
    {
        jstr = (jstring)methodInfo.env->CallStaticObjectMethod(methodInfo.classID, methodInfo.methodID);
    }
    methodInfo.env->DeleteLocalRef(methodInfo.classID);
    
    string deviceModel(methodInfo.env->GetStringUTFChars(jstr, NULL));
    return deviceModel;
    
}

void CCNative::vibrate()
{
    CCLog("CCNative::vibrate() not support on this platform.");
}

NS_CC_EXTRA_END
