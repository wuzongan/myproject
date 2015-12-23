#ifndef __CCTEXTURE_LODER_H__
#define __CCTEXTURE_LODER_H__

#include "cocoa/CCObject.h"
#include "cocoa/CCArray.h"
#include "cocoa/CCDictionary.h"
#include <string>

NS_CC_BEGIN

#define MAX_REFID 2147483647

class CCTexturesLoader;
class CCTextureLoaderManager;

class CC_DLL CCTextureLoaderObject : public CCObject{	    
public:    
	CCTextureLoaderObject(CCTexturesLoader* myLoder,std::string filePath);
	virtual ~CCTextureLoaderObject();
	void onLoaderFinish(CCObject*);
	void startLoad();
	void cancel();
private:
	std::string m_filePath;
	CCTexturesLoader *m_loader;
};

class CC_DLL CCTexturesLoader : public CCObject{	    
public:
	CCTexturesLoader(CCTextureLoaderManager* mgr,int refid);
	virtual ~CCTexturesLoader();
	bool init(std::string allfiles,int callBackFunc);	
	void onLoadOne(CCObject *object);
	void cancel();
	void startLoad();
private:
	void addLoad(std::string filePath);	
public:
	std::string m_allFiles;
	int m_luaFunction;
	int m_refid;
private:
	CCArray* m_objectArray;	
	CCTextureLoaderManager* m_mgr;	
	
};

class CC_DLL CCTextureLoaderManager : public CCObject{	    
public:
	static CCTextureLoaderManager * sharedTextureLoaderManager();
	CCTextureLoaderManager();
	virtual ~CCTextureLoaderManager();
	//return refid
	int startLoad(std::string allfiles,int callBackFunc);	
	void onLoadFinish(CCTexturesLoader* loader);
	void cancelLoad(int refid);
	void cancelAll();
private:
	int m_refid;
	CCDictionary* m_loaders;
};

NS_CC_END

#endif