/****************************************************************************
Copyright (c) 2010-2012 cocos2d-x.org
Copyright (c) 2008-2010 Ricardo Quesada
Copyright (c) 2009      Jason Booth
Copyright (c) 2009      Robert J Payne
Copyright (c) 2011      Zynga Inc.

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

#include "cocoa/CCNS.h"
#include "ccMacros.h"
#include "textures/CCTextureCache.h"
#include "CCSpriteFrameCache.h"
#include "CCSpriteFrame.h"
#include "CCSprite.h"
#include "support/TransformUtils.h"
#include "platform/CCFileUtils.h"
#include "cocoa/CCString.h"
#include "cocoa/CCArray.h"
#include "cocoa/CCDictionary.h"
#include <vector>
using namespace std;

NS_CC_BEGIN

static CCSpriteFrameCache *pSharedSpriteFrameCache = NULL;

CCSpriteFrameCache* CCSpriteFrameCache::sharedSpriteFrameCache(void)
{
    if (! pSharedSpriteFrameCache)
    {
        pSharedSpriteFrameCache = new CCSpriteFrameCache();
        pSharedSpriteFrameCache->init();
    }

    return pSharedSpriteFrameCache;
}

void CCSpriteFrameCache::purgeSharedSpriteFrameCache(void)
{
    CC_SAFE_RELEASE_NULL(pSharedSpriteFrameCache);
}

bool CCSpriteFrameCache::init(void)
{
    m_pSpriteFrames= new CCDictionary();
    m_pSpriteFramesAliases = new CCDictionary();
	png2plist = new std::map<std::string,std::string>();
    m_pLoadedFileNames = new std::set<std::string>();
    return true;
}

CCSpriteFrameCache::~CCSpriteFrameCache(void)
{
    CC_SAFE_RELEASE(m_pSpriteFrames);
    CC_SAFE_RELEASE(m_pSpriteFramesAliases);
    CC_SAFE_DELETE(m_pLoadedFileNames);
	CC_SAFE_DELETE(png2plist);
}

void CCSpriteFrameCache::addSpriteFramesWithDictionary(CCDictionary* dictionary, CCTexture2D *pobTexture)
{
    /*
    Supported Zwoptex Formats:

    ZWTCoordinatesFormatOptionXMLLegacy = 0, // Flash Version
    ZWTCoordinatesFormatOptionXML1_0 = 1, // Desktop Version 0.0 - 0.4b
    ZWTCoordinatesFormatOptionXML1_1 = 2, // Desktop Version 1.0.0 - 1.0.1
    ZWTCoordinatesFormatOptionXML1_2 = 3, // Desktop Version 1.0.2+
    */

    CCDictionary *metadataDict = (CCDictionary*)dictionary->objectForKey("metadata");
    CCDictionary *framesDict = (CCDictionary*)dictionary->objectForKey("frames");
    int format = 0;

    // get the format
    if(metadataDict != NULL) 
    {
        format = metadataDict->valueForKey("format")->intValue();
    }

    // check the format
    CCAssert(format >=0 && format <= 3, "format is not supported for CCSpriteFrameCache addSpriteFramesWithDictionary:textureFilename:");

    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(framesDict, pElement)
    {
        CCDictionary* frameDict = (CCDictionary*)pElement->getObject();
        std::string spriteFrameName = pElement->getStrKey();
        CCSpriteFrame* spriteFrame = (CCSpriteFrame*)m_pSpriteFrames->objectForKey(spriteFrameName);
        if (spriteFrame)
        {
            continue;
        }
        

        if(format == 0) 
        {
            float x = frameDict->valueForKey("x")->floatValue();
            float y = frameDict->valueForKey("y")->floatValue();
            float w = frameDict->valueForKey("width")->floatValue();
            float h = frameDict->valueForKey("height")->floatValue();
            float ox = frameDict->valueForKey("offsetX")->floatValue();
            float oy = frameDict->valueForKey("offsetY")->floatValue();
            int ow = frameDict->valueForKey("originalWidth")->intValue();
            int oh = frameDict->valueForKey("originalHeight")->intValue();
            // check ow/oh
            if(!ow || !oh)
            {
                CCLOGWARN("cocos2d: WARNING: originalWidth/Height not found on the CCSpriteFrame. AnchorPoint won't work as expected. Regenrate the .plist");
            }
            // abs ow/oh
            ow = abs(ow);
            oh = abs(oh);
            // create frame
            spriteFrame = new CCSpriteFrame();
            spriteFrame->initWithTexture(pobTexture, 
                                        CCRectMake(x, y, w, h), 
                                        false,
                                        CCPointMake(ox, oy),
                                        CCSizeMake((float)ow, (float)oh)
                                        );
        } 
        else if(format == 1 || format == 2) 
        {
            CCRect frame = CCRectFromString(frameDict->valueForKey("frame")->getCString());
            bool rotated = false;

            // rotation
            if (format == 2)
            {
                rotated = frameDict->valueForKey("rotated")->boolValue();
            }

            CCPoint offset = CCPointFromString(frameDict->valueForKey("offset")->getCString());
            CCSize sourceSize = CCSizeFromString(frameDict->valueForKey("sourceSize")->getCString());

            // create frame
            spriteFrame = new CCSpriteFrame();
            spriteFrame->initWithTexture(pobTexture, 
                frame,
                rotated,
                offset,
                sourceSize
                );
        } 
        else if (format == 3)
        {
            // get values
            CCSize spriteSize = CCSizeFromString(frameDict->valueForKey("spriteSize")->getCString());
            CCPoint spriteOffset = CCPointFromString(frameDict->valueForKey("spriteOffset")->getCString());
            CCSize spriteSourceSize = CCSizeFromString(frameDict->valueForKey("spriteSourceSize")->getCString());
            CCRect textureRect = CCRectFromString(frameDict->valueForKey("textureRect")->getCString());
            bool textureRotated = frameDict->valueForKey("textureRotated")->boolValue();

            // get aliases
            CCArray* aliases = (CCArray*) (frameDict->objectForKey("aliases"));
            CCString * frameKey = new CCString(spriteFrameName);

            CCObject* pObj = NULL;
            CCARRAY_FOREACH(aliases, pObj)
            {
                std::string oneAlias = ((CCString*)pObj)->getCString();
                if (m_pSpriteFramesAliases->objectForKey(oneAlias.c_str()))
                {
                    CCLOGWARN("cocos2d: WARNING: an alias with name %s already exists", oneAlias.c_str());
                }

                m_pSpriteFramesAliases->setObject(frameKey, oneAlias.c_str());
            }
            frameKey->release();
            // create frame
            spriteFrame = new CCSpriteFrame();
            spriteFrame->initWithTexture(pobTexture,
                            CCRectMake(textureRect.origin.x, textureRect.origin.y, spriteSize.width, spriteSize.height),
                            textureRotated,
                            spriteOffset,
                            spriteSourceSize);
        }

        // add sprite frame
        m_pSpriteFrames->setObject(spriteFrame, spriteFrameName);
        spriteFrame->release();
    }
}

void CCSpriteFrameCache::addSpriteFramesWithFile(const char *pszPlist, CCTexture2D *pobTexture)
{
	//update xiaolinfu
	if (m_pLoadedFileNames->find(pszPlist) !=m_pLoadedFileNames->end())
	{
		return;
	}       
    std::string fullPath = CCFileUtils::sharedFileUtils()->fullPathForFilename(pszPlist);
    CCDictionary *dict = CCDictionary::createWithContentsOfFileThreadSafe(fullPath.c_str());

    addSpriteFramesWithDictionary(dict, pobTexture);
	m_pLoadedFileNames->insert(pszPlist);
    dict->release();
}

void CCSpriteFrameCache::addSpriteFramesWithFile(const char* plist, const char* textureFileName)
{
    CCAssert(textureFileName, "texture name should not be null");
    CCTexture2D *texture = CCTextureCache::sharedTextureCache()->addImage(textureFileName);

    if (texture)
    {
        addSpriteFramesWithFile(plist, texture);
    }
    else
    {
        CCLOG("cocos2d: CCSpriteFrameCache: couldn't load texture file. File not found %s", textureFileName);
    }
}

void CCSpriteFrameCache::addSpriteFramesWithFile(const char *pszPlist)
{
    CCAssert(pszPlist, "plist filename should not be NULL");

    if (m_pLoadedFileNames->find(pszPlist) == m_pLoadedFileNames->end())
    {
        std::string fullPath = CCFileUtils::sharedFileUtils()->fullPathForFilename(pszPlist);
        CCDictionary *dict = CCDictionary::createWithContentsOfFileThreadSafe(fullPath.c_str());

        string texturePath("");

        CCDictionary* metadataDict = (CCDictionary*)dict->objectForKey("metadata");
        if (metadataDict)
        {
            // try to read  texture file name from meta data
            texturePath = metadataDict->valueForKey("textureFileName")->getCString();
        }

        if (! texturePath.empty())
        {
            // build texture path relative to plist file
            texturePath = CCFileUtils::sharedFileUtils()->fullPathFromRelativeFile(texturePath.c_str(), pszPlist);
        }
        else
        {
            // build texture path by replacing file extension
            texturePath = pszPlist;

            // remove .xxx
            size_t startPos = texturePath.find_last_of("."); 
            texturePath = texturePath.erase(startPos);

            // append .png
            texturePath = texturePath.append(".png");

            CCLOG("cocos2d: CCSpriteFrameCache: Trying to use file %s as texture", texturePath.c_str());
        }

        CCTexture2D *pTexture = CCTextureCache::sharedTextureCache()->addImage(texturePath.c_str());

        if (pTexture)
        {
            addSpriteFramesWithDictionary(dict, pTexture);
            m_pLoadedFileNames->insert(pszPlist);
        }
        else
        {
            CCLOG("cocos2d: CCSpriteFrameCache: Couldn't load texture");
        }

        dict->release();
    }

}

void CCSpriteFrameCache::addSpriteFrame(CCSpriteFrame *pobFrame, const char *pszFrameName)
{
    m_pSpriteFrames->setObject(pobFrame, pszFrameName);
}

void CCSpriteFrameCache::removeSpriteFrames(void)
{
    m_pSpriteFrames->removeAllObjects();
    m_pSpriteFramesAliases->removeAllObjects();
    m_pLoadedFileNames->clear();
}

void CCSpriteFrameCache::removeUnusedSpriteFrames(void)
{
    bool bRemoved = false;
    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(m_pSpriteFrames, pElement)
    {
        CCSpriteFrame* spriteFrame = (CCSpriteFrame*)pElement->getObject();
        if( spriteFrame->retainCount() == 1 ) 
        {
            //CCLOG("cocos2d: CCSpriteFrameCache: removing unused frame: %s", pElement->getStrKey());
           m_pSpriteFrames->removeObjectForElememt(pElement);
           bRemoved = true;
        }
    }

    // XXX. Since we don't know the .plist file that originated the frame, we must remove all .plist from the cache
    if( bRemoved )
    {
        m_pLoadedFileNames->clear();
    }
}


void CCSpriteFrameCache::removeSpriteFrameByName(const char *pszName)
{
    // explicit nil handling
    if( ! pszName )
    {
        return;
    }

    // Is this an alias ?
    CCString* key = (CCString*)m_pSpriteFramesAliases->objectForKey(pszName);

    if (key)
    {
        m_pSpriteFrames->removeObjectForKey(key->getCString());
        m_pSpriteFramesAliases->removeObjectForKey(key->getCString());
    }
    else
    {
        m_pSpriteFrames->removeObjectForKey(pszName);
    }

    // XXX. Since we don't know the .plist file that originated the frame, we must remove all .plist from the cache
    m_pLoadedFileNames->clear();
}

void CCSpriteFrameCache::removeSpriteFramesFromFile(const char* plist)
{
    std::string fullPath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist);
    CCDictionary* dict = CCDictionary::createWithContentsOfFileThreadSafe(fullPath.c_str());

    removeSpriteFramesFromDictionary((CCDictionary*)dict);

    // remove it from the cache
    set<string>::iterator ret = m_pLoadedFileNames->find(plist);
    if (ret != m_pLoadedFileNames->end())
    {
        m_pLoadedFileNames->erase(ret);
    }

    dict->release();
}

void CCSpriteFrameCache::removeSpriteFramesFromDictionary(CCDictionary* dictionary)
{
    CCDictionary* framesDict = (CCDictionary*)dictionary->objectForKey("frames");
    CCArray* keysToRemove = CCArray::create();

    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(framesDict, pElement)
    {
        if (m_pSpriteFrames->objectForKey(pElement->getStrKey()))
        {
            keysToRemove->addObject(CCString::create(pElement->getStrKey()));
        }
    }

    m_pSpriteFrames->removeObjectsForKeys(keysToRemove);
}

void CCSpriteFrameCache::removeSpriteFramesFromTexture(CCTexture2D* texture)
{
    CCArray* keysToRemove = CCArray::create();

    CCDictElement* pElement = NULL;
    CCDICT_FOREACH(m_pSpriteFrames, pElement)
    {
        string key = pElement->getStrKey();
        CCSpriteFrame* frame = (CCSpriteFrame*)m_pSpriteFrames->objectForKey(key.c_str());
        if (frame && (frame->getTexture() == texture))
        {
            keysToRemove->addObject(CCString::create(pElement->getStrKey()));
        }
    }

    m_pSpriteFrames->removeObjectsForKeys(keysToRemove);
}

CCSpriteFrame* CCSpriteFrameCache::spriteFrameByName(const char *pszName)
{
	// add by xiaolinfu
	//CCLOG(pszName);
	string path_s = pszName;
	size_t start = path_s.find_last_of("/")+1;
	string filename = pszName;
	if(start>0)
	{
		filename = path_s.substr(start).c_str();
	}
	
    CCSpriteFrame* frame = (CCSpriteFrame*)m_pSpriteFrames->objectForKey(filename);

	if (frame == NULL)
	{
		string plist,fullpath;
		bool isExist = false;
		
		/*do{
			string fileNameWithoutExt = filename.substr(0,filename.length()-4);
			plist = path_s.substr(0,start) + fileNameWithoutExt + ".plist";  //和文件同名plist
			fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
			isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
			if(isExist) break;

			size_t start_=0;
			do{                  // 查找文件前缀pist _分割 例 button_ui_bk.png 则查找button_ui_all button_ui.plist button.plist
				start_ = fileNameWithoutExt.find_last_of("_");
				if(start_==-1) break;
				plist =path_s.substr(0,start) + fileNameWithoutExt.substr(0,start_) + ".plist";
				fileNameWithoutExt = fileNameWithoutExt.substr(0,start_);
				string all = plist.substr(0,plist.length()-6) + "_all.plist";
				fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(all.c_str());
				isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
				if (!isExist)
				{
					fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
					isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
				}
				else
				{
					plist = all;
				}
			}while(isExist==false);
			if(isExist) break;

			size_t dirstart = path_s.substr(0,start-1).find_last_of("/")+1; 
			plist = path_s.substr(0,start) + path_s.substr(dirstart,start-dirstart-1) + ".plist"; //图片所在父目录名字plist
			fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
			isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
		}
		while(false); */
		////图片所在父目录名字 0 ...plist
		if(png2plist->find(filename) == png2plist->end())
		{
			int start_2 = path_s.find("res/map/");
			if(start_2 >= 0 ) //地图
			{
				int end_ = filename.find_first_of("_");
				plist ="res/map/" + filename.substr(0,end_) + ".plist";
				fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
				isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
				if(!isExist)
				{
					plist ="res/map/" + filename.substr(0,filename.length()-4) + ".plist";
					fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
					isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
				}
				if(isExist){
				png2plist->insert(pair<std::string,std::string>(filename,plist));
				}
				else
				{
					CCLOG("%s  没有找到",path_s.c_str());
				}
			}
			else
			{
				CCDictionary *dict;
				CCDictionary *framesDict;
				CCDictElement* pElement = NULL;
				char buff[10];
				int i = 0;
				size_t dirstart = path_s.substr(0,start-1).find_last_of("/")+1; 
				do{
					sprintf(buff,"%ld",i);
					plist = path_s.substr(0,start) + path_s.substr(dirstart,start-dirstart-1) +buff+ ".plist"; 
					//CCLOG(plist.c_str());
					fullpath = CCFileUtils::sharedFileUtils()->fullPathForFilename(plist.c_str());
					isExist = CCFileUtils::sharedFileUtils()->isFileExist(fullpath);
					if(isExist){
						dict = CCDictionary::createWithContentsOfFile(fullpath.c_str());
						framesDict = (CCDictionary*)dict->objectForKey("frames");
						CCDICT_FOREACH(framesDict, pElement)
						{
							std::string spriteFrameName = pElement->getStrKey();
							if(png2plist->find(spriteFrameName) ==png2plist->end())
							{
								png2plist->insert(pair<std::string,std::string>(spriteFrameName,plist));
							}
						}

						//CC_SAFE_RELEASE(framesDict);
						i++;
					}
				}while(isExist);
			}
			

		}

		if(png2plist->find(filename) != png2plist->end())
		{
			plist = png2plist->at(filename);
			addSpriteFramesWithFile(plist.c_str());
		}
		frame =  (CCSpriteFrame*)m_pSpriteFrames->objectForKey(filename.c_str());
		if(frame == NULL)
		{
			//CCAssert(frame!=NULL,(path_s + "	load from plist failed ,you may forget pack it or not pack it in the correct way! by --xiaolinfu--").c_str());
			if(!strstr(path_s.c_str(), "img/par"))
			{
				//CCLOG("warming  %s  doesnot pack =======================",path_s.c_str());
			}
			CCTexture2D *pTexture = CCTextureCache::sharedTextureCache()->addImage(path_s.c_str());
			if(pTexture==NULL)
				return NULL;
			CCRect rect = CCRectZero;
			rect.size = pTexture->getContentSize();
			frame =  CCSpriteFrame::createWithTexture(pTexture,rect);
		}
	}
	//add end
	
	//CCSpriteFrame* frame = (CCSpriteFrame*)m_pSpriteFrames->objectForKey(pszName);
	
    if (!frame)
    {
        // try alias dictionary
        CCString *key = (CCString*)m_pSpriteFramesAliases->objectForKey(pszName);  
        if (key)
        {
            frame = (CCSpriteFrame*)m_pSpriteFrames->objectForKey(key->getCString());
            if (! frame)
            {
                CCLOG("cocos2d: CCSpriteFrameCache: Frame '%s' not found", pszName);
            }
        }
    }
	//frame->getTexture()->setAliasTexParameters();
    return frame;
}

NS_CC_END
