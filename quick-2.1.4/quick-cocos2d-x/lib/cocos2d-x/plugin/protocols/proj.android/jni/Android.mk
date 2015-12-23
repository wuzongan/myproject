LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := PluginProtocolStatic

LOCAL_MODULE_FILENAME := libPluginProtocolStatic

LOCAL_SRC_FILES :=\
$(addprefix ../../platform/android/, \
	PluginFactory.cpp \
    PluginJniHelper.cpp \
    PluginUtils.cpp \
    PluginProtocol.cpp \
    ProtocolAnalytics.cpp \
    ProtocolIAP.cpp \
    ProtocolAds.cpp \
    ProtocolShare.cpp \
    ProtocolUser.cpp \
    ProtocolSocial.cpp \
) \
../../PluginManager.cpp \
../../PluginParam.cpp \
../../Plugin_luabinding.cpp

LOCAL_CFLAGS := -Wno-psabi
LOCAL_EXPORT_CFLAGS := -Wno-psabi
LIBPATH := $(LOCAL_PATH)/../../../../../
LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../include  \
					$(LIBPATH)/cocos2d-x/scripting/lua/cocos2dx_support \
					$(LIBPATH)/cocos2d-x/scripting/lua/luajit/include \
					$(LIBPATH)/cocos2d-x/cocos2dx/include \
					$(LIBPATH)/cocos2d-x/cocos2dx \
					$(LIBPATH)/cocos2d-x/cocos2dx/platform/android \
					$(LIBPATH)/cocos2d-x/cocos2dx/kazmath/include  \
					$(LIBPATH)/cocos2d-x/scripting/lua/tolua 
LOCAL_EXPORT_C_INCLUDES := $(LOCAL_PATH)/../../include $(LOCAL_PATH)/../../platform/android 

LOCAL_LDLIBS := -landroid
LOCAL_LDLIBS += -llog

include $(BUILD_STATIC_LIBRARY)

