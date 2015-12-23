#include "CCTextureLoder.h"
#include "textures/CCTextureCache.h"
#include "CCLuaEngine.h"

NS_CC_BEGIN

///////////////////////////////////////CCTextureLoaderObject///////////////////////////////////////

CCTextureLoaderObject::CCTextureLoaderObject(CCTexturesLoader* myLoder,std::string filePath){
	m_loader = myLoder;
	m_filePath = filePath;
}

CCTextureLoaderObject::~CCTextureLoaderObject(){
	CCLog("~CCTextureLoaderObject");
}

void CCTextureLoaderObject::startLoad(){
	CCTextureCache::sharedTextureCache()->addImageAsync(m_filePath.c_str(),this,callfuncO_selector(CCTextureLoaderObject::onLoaderFinish));
}

void CCTextureLoaderObject::cancel(){
	m_loader = NULL;
}

void CCTextureLoaderObject::onLoaderFinish(CCObject* _tmp){
	CCLog("CCTextureLoaderObject onLoaderFinish %s\n",m_filePath.c_str());
	if(m_loader)
		m_loader->onLoadOne(this);
}
///////////////////////////////////////CCTexturesLoader///////////////////////////////////////

CCTexturesLoader::CCTexturesLoader(CCTextureLoaderManager* mgr,int refid)
{
	m_objectArray = new CCArray();
	std::string m_allFiles = "";
	m_luaFunction = 0;
	m_mgr = mgr;
	m_refid = refid;
}

CCTexturesLoader::~CCTexturesLoader(){		
	CCLog("~CCTexturesLoader");
	cancel();	
	CC_SAFE_RELEASE_NULL(m_objectArray);
}

bool CCTexturesLoader::init(std::string allfiles,int callBackFunc){
	int pos;
	std::string tmp_str;
	m_luaFunction = callBackFunc;
	do{
		pos = allfiles.find(";");
		if(pos == allfiles.npos){
			tmp_str = allfiles.substr(0,allfiles.length());
			addLoad(tmp_str);
			break;
		}
		tmp_str = allfiles.substr(0,pos);
		allfiles.erase(0,pos + 1);
		addLoad(tmp_str);
	}while(true);

	if(m_objectArray->count() != 0){
		return true;
	}
	return false;
}

void CCTexturesLoader::addLoad(std::string filePath){
	if(filePath != ""){
		CCTextureLoaderObject *tmp = new CCTextureLoaderObject(this,filePath);
		m_objectArray->addObject(tmp);
		tmp->autorelease();		
	}
}

void CCTexturesLoader::startLoad(){	
	CCObject *pObj;
	CCARRAY_FOREACH(m_objectArray, pObj)
	{		
		dynamic_cast<CCTextureLoaderObject*>(pObj)->startLoad();
	}	
}

void CCTexturesLoader::onLoadOne(CCObject *object){
	m_objectArray->removeObject(object);
	if(m_objectArray->count() == 0){
		m_mgr->onLoadFinish(this);
	}
}

void CCTexturesLoader::cancel(){
	CCObject *pObj;
	CCARRAY_FOREACH(m_objectArray, pObj)
	{		
		dynamic_cast<CCTextureLoaderObject*>(pObj)->cancel();
	}		
}

///////////////////////////////////////CCTextureLoaderManager///////////////////////////////////////
static CCTextureLoaderManager *g_sharedTextureLoaderManager = NULL;

CCTextureLoaderManager * CCTextureLoaderManager::sharedTextureLoaderManager()
{
    if (!g_sharedTextureLoaderManager)
    {
        g_sharedTextureLoaderManager = new CCTextureLoaderManager();
    }
    return g_sharedTextureLoaderManager;
}

CCTextureLoaderManager::CCTextureLoaderManager(){
	m_loaders = new CCDictionary();
	m_refid = 1;
}

CCTextureLoaderManager::~CCTextureLoaderManager(){
	m_loaders->release();
}

//return refid
int CCTextureLoaderManager::startLoad(std::string allfiles,int callBackFunc){
	if(m_refid >= MAX_REFID){
		cancelAll();
	}
	int newRefId = m_refid++;
	CCTexturesLoader* loader = new CCTexturesLoader(this,newRefId);	
	if(loader->init(allfiles,callBackFunc))
	{				
		m_loaders->setObject(loader,newRefId);			
		loader->startLoad();
		loader->release();
		return newRefId;
	}
	loader->release();
	return 0;	
}

void CCTextureLoaderManager::onLoadFinish(CCTexturesLoader* loader){	
	CCLog("CCTextureLoaderManager onLoaderFinish %s\n",loader->m_allFiles.c_str());
	CCLuaStack* stack = ((CCLuaEngine*)(CCScriptEngineManager::sharedManager()->getScriptEngine()))->getLuaStack();
	stack->executeFunctionByHandler(loader->m_luaFunction,0);
	CCTextureLoaderManager::cancelLoad(loader->m_refid);
}

void CCTextureLoaderManager::cancelLoad(int refid){
	CCTexturesLoader* loader = dynamic_cast<CCTexturesLoader*>(m_loaders->objectForKey(refid));
	if(loader){
		CCLog("CCTextureLoaderManager remove %d\n",refid);
		CCScriptEngineManager::sharedManager()->getScriptEngine()->removeScriptHandler(loader->m_luaFunction);
		m_loaders->removeObjectForKey(refid);		
	}	
}

void CCTextureLoaderManager::cancelAll(){
	CCDictElement* pElement = NULL;
	CCTexturesLoader* tmpLoader = NULL;
	CCLuaStack* stack = ((CCLuaEngine*)(CCScriptEngineManager::sharedManager()->getScriptEngine()))->getLuaStack();
    CCDICT_FOREACH(m_loaders, pElement)
    {
		//全部取消时,要触发当前队列内的完成函数
		tmpLoader = dynamic_cast<CCTexturesLoader*>(pElement->getObject());
		stack->executeFunctionByHandler(tmpLoader->m_luaFunction,0);
		CCScriptEngineManager::sharedManager()->getScriptEngine()->removeScriptHandler(tmpLoader->m_luaFunction);		        
    }
	m_loaders->removeAllObjects();
	m_refid = 1;	
}

NS_CC_END