
#include "cocos2d.h"
#include "cocos-ext.h"
#include "AppDelegate.h"
#include "AppConfig.h"
#include "SimpleAudioEngine.h"
#include "support/CCNotificationCenter.h"
#include "CCLuaEngine.h"
#include <string>

// lua extensions
#include "lua_extensions.h"
// cocos2dx_extra luabinding
#include "luabinding/cocos2dx_extra_luabinding.h"
#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
#include "luabinding/cocos2dx_extra_ios_iap_luabinding.h"
#endif
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "Plugin_luabinding.h"
#endif
// thrid_party
#include "third_party_luabinding.h"

// CCBReader
#include "Lua_extensions_CCB.h"
// my_cpp
#include "tolua/GameMap_luabinding.h"
#include "tolua/Floater_luabinding.h"
#include "tolua/CCBAnimationManager_luabinding.h"
#include "tolua/CustomFont_luabinding.h"
#include "tag/NodeParser_luabinding.h"
#include "network/socket/NetworkLogic_luabinding.h"
#include "network/socket/MessageUpdate.h"
#include "tolua/DataCenter_luabinding.h"
#include <string>
using namespace std;
using namespace cocos2d;
using namespace CocosDenshion;
using namespace dfont;
AppDelegate::AppDelegate()
{
    // fixed me
    //_CrtSetDbgFlag(_CRTDBG_ALLOC_MEM_DF|_CRTDBG_LEAK_CHECK_DF);
}

AppDelegate::~AppDelegate()
{
    // end simple audio engine here, or it may crashed on win32
    SimpleAudioEngine::sharedEngine()->end();
}

bool AppDelegate::applicationDidFinishLaunching()
{
	//for fresh in win32,recreate fontfactory
	FontFactory::destroy_instance();
	FontFactory::instance();
    // initialize director
	CCDirector::sharedDirector()->setDisplayStats(false);
    CCDirector *pDirector = CCDirector::sharedDirector();
    pDirector->setOpenGLView(CCEGLView::sharedOpenGLView());
    pDirector->setProjection(kCCDirectorProjection2D);

    // set FPS. the default value is 1.0/60 if you don't call this
    pDirector->setAnimationInterval(1.0 / 60);
	

//	updateVersion();
#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID || CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
	checkVersion();
#endif

    // register lua engine
    CCLuaEngine *pEngine = CCLuaEngine::defaultEngine();
    CCScriptEngineManager::sharedManager()->setScriptEngine(pEngine);
    
    CCLuaStack *pStack = pEngine->getLuaStack();
    lua_State* L = pStack->getLuaState();
    
    // load lua extensions
    luaopen_lua_extensions(L);
    // load cocos2dx_extra luabinding
    luaopen_cocos2dx_extra_luabinding(L);
    
    // thrid_party
    luaopen_third_party_luabinding(L);
    
    // CCBReader
    tolua_extensions_ccb_open(L);
    
    //mycpp
    luaopen_GameMap_luabinding(L);
    luaopen_Floater_luabinding(L);
    luaopen_CCBAnimationManager_luabinding(L);
	luaopen_NodeParser_luabinding(L);
    luaopen_NetworkLogic_luabinding(L);
	luaopen_CustomFont_luabinding(L);
	luaopen_DataCenter_luabinding(L);
	#if (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
		luaopen_Plugin_luabinding(L);
	#endif
    MessageUpdate::create();
	
      
    // load framework
    if (m_projectConfig.isLoadPrecompiledFramework())
    {
        const string precompiledFrameworkPath = SimulatorConfig::sharedDefaults()->getPrecompiledFrameworkPath();
        pStack->loadChunksFromZip(precompiledFrameworkPath.c_str());
    }
	//设置纹理alpha 打包参数设置 by xiaolinfu
   // CCTexture2D::PVRImagesHavePremultipliedAlpha(true);
	//addSearchPaths
	vector<string> searchPahts = CCFileUtils::sharedFileUtils()->getSearchPaths();
	searchPahts.insert(searchPahts.begin(), CCFileUtils::sharedFileUtils()->getWritablePath());
	CCFileUtils::sharedFileUtils()->setSearchPaths(searchPahts);

    // load script
    string path = CCFileUtils::sharedFileUtils()->fullPathForFilename(m_projectConfig.getScriptFileRealPath().c_str());
    int pos;
    while ((pos = path.find_first_of("\\")) != std::string::npos)
    {
        path.replace(pos, 1, "/");
    }
    size_t p = path.find_last_of("/\\");
    if (p != path.npos)
    {
        const string dir = path.substr(0, p);
        pStack->addSearchPath(dir.c_str());
        
        p = dir.find_last_of("/\\");
        if (p != dir.npos)
        {
            pStack->addSearchPath(dir.substr(0, p).c_str());
        }
    }
    
    string env = "__LUA_STARTUP_FILE__=\"";
    env.append(path);
    env.append("\"");
    pEngine->executeString(env.c_str());
    
    CCLOG("------------------------------------------------");
    CCLOG("LOAD LUA FILE: %s", path.c_str());
    CCLOG("------------------------------------------------");
    pEngine->executeScriptFile(path.c_str());

    return true;
}

void AppDelegate::updateVersion()
{
	if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	{
		// update version
		versionControl.updateVersion(this);
	}
	//*/
	else if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	{
		// The development platform is not updated.
        CCLOG("updateVersion 32");
		runToLua();
	}
	//*/
}

void AppDelegate::runToLua()
{	
    // register lua engine
    CCLuaEngine *pEngine = CCLuaEngine::defaultEngine();
    CCScriptEngineManager::sharedManager()->setScriptEngine(pEngine);

    CCLuaStack *pStack = pEngine->getLuaStack();
    lua_State* L = pStack->getLuaState();

    // load lua extensions
    luaopen_lua_extensions(L);
    // load cocos2dx_extra luabinding
    luaopen_cocos2dx_extra_luabinding(L);
    #if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
        luaopen_cocos2dx_extra_ios_iap_luabinding(L);
    #endif

    // thrid_party
    luaopen_third_party_luabinding(L);

    // CCBReader
    tolua_extensions_ccb_open(L);
    
    //mycpp
    luaopen_GameMap_luabinding(L);
    luaopen_Floater_luabinding(L);
    luaopen_CCBAnimationManager_luabinding(L);
	luaopen_NodeParser_luabinding(L);
    luaopen_NetworkLogic_luabinding(L);
	luaopen_CustomFont_luabinding(L);
    MessageUpdate::create();

    // load framework
    if (m_projectConfig.isLoadPrecompiledFramework())
    {
        const string precompiledFrameworkPath = SimulatorConfig::sharedDefaults()->getPrecompiledFrameworkPath();
        pStack->loadChunksFromZip(precompiledFrameworkPath.c_str());
    }

    #if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
        string path = versionControl.getCCFileUtils()->fullPathForFilename("scripts/main.lua");
    #else
        string path = versionControl.getCCFileUtils()->fullPathForFilename(m_projectConfig.getScriptFileRealPath().c_str());
    #endif

    std::string strAddSearchPath = versionControl.getSaveFilePath() + "scripts/";
    CCLOG("####addSearchPath:%s", strAddSearchPath.c_str());
    pStack->addSearchPath(versionControl.getSaveFilePath().c_str());
    pStack->addSearchPath(strAddSearchPath.c_str());

    string env = "__LUA_STARTUP_FILE__=\"";
    env.append(path);
    env.append("\"");
    pEngine->executeString(env.c_str());

    CCLOG("------------------------------------------------");
    CCLOG("LOAD LUA FILE: %s", path.c_str());
    CCLOG("------------------------------------------------");

    pEngine->executeScriptFile(path.c_str());
}

// This function will be called when the app is inactive. When comes a phone call,it's be invoked too
void AppDelegate::applicationDidEnterBackground()
{
    CCDirector::sharedDirector()->stopAnimation();
    CCDirector::sharedDirector()->pause();
    SimpleAudioEngine::sharedEngine()->pauseBackgroundMusic();
    SimpleAudioEngine::sharedEngine()->pauseAllEffects();
    CCNotificationCenter::sharedNotificationCenter()->postNotification("APP_ENTER_BACKGROUND");
}

// this function will be called when the app is active again
void AppDelegate::applicationWillEnterForeground()
{
    CCDirector::sharedDirector()->startAnimation();
    CCDirector::sharedDirector()->resume();
    SimpleAudioEngine::sharedEngine()->resumeBackgroundMusic();
    SimpleAudioEngine::sharedEngine()->resumeAllEffects();
    CCNotificationCenter::sharedNotificationCenter()->postNotification("APP_ENTER_FOREGROUND");
}

void AppDelegate::setProjectConfig(const ProjectConfig& config)
{
    m_projectConfig = config;
}

void AppDelegate::checkVersion()
{
	//get local version
	int recordedVersion = CCUserDefault::sharedUserDefault()->getIntegerForKey("big-version");
	//compare local version and the apk version
	if (recordedVersion < BIG_VERSION)
	{
		string resPath = CCFileUtils::sharedFileUtils()->getWritablePath() + "res";
		string scriptsPath = CCFileUtils::sharedFileUtils()->getWritablePath() + "scripts";
		if (CCFileUtils::sharedFileUtils()->isDirectoryExist(resPath))
		{
			system(("rm -r " + resPath).c_str());//delete old res
		}
		if (CCFileUtils::sharedFileUtils()->isDirectoryExist(scriptsPath))
		{
			system(("rm -r " + scriptsPath).c_str());//delete old scripts
		}
		//set new apk version to local xml file
		CCUserDefault::sharedUserDefault()->setIntegerForKey("big-version", BIG_VERSION);
		char version[5];
		sprintf(version,"%ld",BIG_VERSION);
		CCUserDefault::sharedUserDefault()->setStringForKey("current-version-code", (version + string(".0.0")).c_str());
		
		CCUserDefault::sharedUserDefault()->flush();
	}
}
