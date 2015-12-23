#ifndef _APP_CONFIG_H_
#define _APP_CONFIG_H_

#define BIG_VERSION 1
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
#define CONFIG_OF_COMPARE_VERSION_URL	"http://10.10.10.104/version.txt"
#define CONFIG_OF_DOWNLOAD_URL	"http://10.10.10.104/download.html"
#else
#define CONFIG_OF_COMPARE_VERSION_URL	"http://10.10.10.104/version.txt"
//#define CONFIG_OF_COMPARE_VERSION_URL	"http://pay.sj.chunbaigame.com/edition_client/version.txt"
#endif


#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
// ANDROID save path
#define CONFIG_OF_PLATFORM_ANDROID_SAVE_PATH	"/mnt/sdcard/zasy/"
#endif
#define PROJECT_DIR "E:/workspace/svn/client/cb_lua_v001"

#define CONFIG_OF_VERSION_TXT_FILES_PATH	"res/version.txt"

#define CONFIG_OF_LIST_FILES_TXT_PATH		"res/files.txt"

#endif