#include "VersionControl.h"
#include "AppDelegate.h"
#include "AppConfig.h"

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
#include <direct.h>
#else
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <errno.h>
#endif


VersionControl::VersionControl()
	:m_nRetryTotalNum(5),
	m_nRetryReqNum(0),
	m_pAppDelegate(NULL),
	m_strVersionURL(CONFIG_OF_COMPARE_VERSION_URL)
{

}


VersionControl::~VersionControl()
{

}


CCFileUtils* VersionControl::getCCFileUtils()
{

	CCFileUtils* pCCFileUtils = CCFileUtils::sharedFileUtils();

	std::string strSaveFilePath = getSaveFilePath() + "res/";

	std::vector<std::string> vecString = pCCFileUtils->getSearchPaths();

	vecString.clear();
	vecString.push_back(strSaveFilePath);
	vecString.push_back(getSaveFilePath());
	//vecString.insert(vecString.begin(), strSaveFilePath);
	
	pCCFileUtils->setSearchPaths(vecString);

	CCLOG("----------getCCFileUtils:setWritablePath:%s", pCCFileUtils->getWritablePath().c_str());
	CCLOG("----------getSaveFilePath():%s", getSaveFilePath().c_str());
	pCCFileUtils->setWritablePath(getSaveFilePath().c_str());
	CCLOG("----------getCCFileUtils:setWritablePath:%s", pCCFileUtils->getWritablePath().c_str());

	//CCFileUtils::sharedFileUtils()->setSearchRootPath();
	//CCFileUtils::sharedFileUtils()->setSearchPaths();

	return pCCFileUtils;
}

std::string VersionControl::getSaveFilePath()
{

#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS || CC_TARGET_PLATFORM == CC_PLATFORM_MAC)
	std::string path = CCFileUtils::sharedFileUtils()->getWritablePath();

#elif (CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
	std::string path = CONFIG_OF_PLATFORM_ANDROID_SAVE_PATH;
	//std::string path = CCFileUtils::sharedFileUtils()->getWritablePath();

#elif (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	std::string path = CCFileUtils::sharedFileUtils()->getWritablePath();
    int pos;
    while ((pos = path.find_first_of("\\")) != std::string::npos)
    {
        path.replace(pos, 1, "/");
    }
#endif

	return path;
}


void VersionControl::updateVersion(void* ptrAppDelegate)
{
	CCLOG("######VersionControl::updateVersion:############");

	m_pAppDelegate = ptrAppDelegate;
	downloadVersionFile();
}

void VersionControl::downloadVersionFile()
{
	onRetryReqVersionFile();
}


void VersionControl::retryRequestVersionFile()
{
	CCLOG("####retryRequestVersionFile#####");
	if (m_nRetryTotalNum > m_nRetryReqNum)
	{
		m_nRetryReqNum ++;
		CCLOG("#####m_nRetryRespNum:%d#######", m_nRetryReqNum);
		onRetryReqVersionFile();
	}
	else
	{
		onDownloadVersionFileFail();
	}
}


std::string VersionControl::getPacketVersionFile()
{
	CCFileUtils *pFileUtils = CCFileUtils::sharedFileUtils();
	std::string strVersionFilesPath = CONFIG_OF_VERSION_TXT_FILES_PATH;

	unsigned long nPacketSize = 0;
	unsigned char* pszPacketVFContent = pFileUtils->getFileData(strVersionFilesPath.c_str(), "rb" , &nPacketSize);

	std::string strPacketVF(reinterpret_cast<const char*>(pszPacketVFContent), nPacketSize);
	CC_SAFE_DELETE_ARRAY(pszPacketVFContent);

	return strPacketVF;
}


void VersionControl::compVersionFile(const std::string& refStrVerFileContent)
{
	std::string strPacketVF = getPacketVersionFile();

	CCLOG("####VersionControl:compVersionFile#####");
	// 对比版本
	if (refStrVerFileContent > strPacketVF)
	{
		//进入商店更新游戏版本
		CCLOG("#######VersionControl:compVersionFile:update new version in shop #######");
		//todo
	}
	else
	{
		CCLOG("#######VersionControl:compVersionFile:version compare success#######");
		unzipPacket();
	}

}

void VersionControl::unzipPacket()
{
	std::string strSaveFilePath = getSaveFilePath();
	CCFileUtils *pFileUtils = CCFileUtils::sharedFileUtils();
	std::string strVersionFilesPath = CONFIG_OF_VERSION_TXT_FILES_PATH;
	std::string strSDVersionFilesPath = strSaveFilePath + strVersionFilesPath;

	std::string strPacketVF = getPacketVersionFile(); 

	// SD卡上是否存在
	if (pFileUtils->isFileExist(strSDVersionFilesPath))
	{
		unsigned long nSDSize = 0;
		unsigned char* pszSDVFContent = pFileUtils->getFileData(strSDVersionFilesPath.c_str(), "rb" , &nSDSize);
		
		std::string strSDVF(reinterpret_cast<const char*>(pszSDVFContent), nSDSize);
		CC_SAFE_DELETE_ARRAY(pszSDVFContent);

		//对比程序包中version和SD卡上的version
		if (strPacketVF > strSDVF)
		{
			CCLOG("###########unzipPacket:strPacketVF > strSDVF;strPacketVF:%s;strSDVF:%s;###########", strPacketVF.c_str(), strSDVF.c_str());
			//SD卡上内容是旧版本
			//删除SD卡上已经有的内容
			if (CC_TARGET_PLATFORM != CC_PLATFORM_WIN32)
			{
				delSDData(strSaveFilePath);
			}
			//解压程序包中内容
			copyFile();
			//最后将version.txt文件写入SD卡的相应目录下，用于下次更新使用
			writeVersionFile(strPacketVF);
		}
		else
		{
			//SD卡上的版本比较新
			CCLOG("###########unzipPacket:SD card data already is new version###########");
		}
	}
	else
	{
		CCLOG("###########unzipPacket:version.txt is not Exist###########");
		//不存在解压
		copyFile();
		//最后将version.txt文件写入SD卡的相应目录下，用于下次更新使用
		writeVersionFile(strPacketVF);
	}
	runFollowUpFunction();
}

void VersionControl::runAppDelegate()
{
	if (m_pAppDelegate)
	{
		(static_cast<AppDelegate*>(m_pAppDelegate))->runToLua();
	}
}

//
void VersionControl::runFollowUpFunction()
{
	runAppDelegate();
}


void VersionControl::copyFile()
{
	std::string strSaveFilePath = getSaveFilePath();
	std::string strRelativeFilesPath = CONFIG_OF_LIST_FILES_TXT_PATH;

	CCFileUtils *pFileutils = CCFileUtils::sharedFileUtils();

	// std::string strSDFilesPath = strSaveFilePath + strRelativeFilesPath;
	// if (!pFileutils->isFileExist(strSDFilesPath))
	{

		unsigned long nSize = 0;
		unsigned char* pszFilesContent = pFileutils->getFileData(strRelativeFilesPath.c_str(), "rb" , &nSize);


		if (pszFilesContent != NULL && nSize > 0)
		{

			std::string strFileContent (reinterpret_cast<const char*>(pszFilesContent), nSize);
			std::string strPattern = "\n";

			CCLOG("###########strFileCont:%s\n#######pattern:%s",strFileContent.c_str(), strPattern.c_str());

			std::vector<std::string> vecFileLines = strSplit(strFileContent, strPattern);
			CCLOG("###########vecFileLines:%d#######",vecFileLines.size());

			for (std::vector<std::string>::iterator iter = vecFileLines.begin(); iter != vecFileLines.end(); iter++)
			{
				CCLOG("############%s###########", (*iter).c_str());
				std::string& refStringName = *iter;
				std::string strFullFilePath = strSaveFilePath + refStringName;

				int pos;
				while ((pos = strFullFilePath.find_first_of("\\")) != std::string::npos)
				{
					strFullFilePath.replace(pos, 1, "/");
				}

				size_t p = strFullFilePath.find_last_of("/\\");
				if (p != strFullFilePath.npos)
				{
					string strDir = strFullFilePath.substr(0, p);
					CCLOG("#####strDir:%s####p:%d#######StrDir.npos:%d", strDir.c_str(), p, strDir.npos);

					if (creatDir(strDir))
					{
						CCLOG("#####error:creatDir:%s",strDir.c_str());
						//break;
					}
				}
				



				if (isFileNameString(refStringName))
				{
					nSize = 0;
					unsigned char* pszSaveContent = pFileutils->getFileData(refStringName.c_str(), "rb", &nSize);

					CCLOG("##########strSaveFilePath:%s###", strFullFilePath.c_str());

					bool bResult = pFileutils->saveToFile(strFullFilePath, pszSaveContent, nSize );

					CC_SAFE_DELETE_ARRAY(pszSaveContent);

					if (!bResult)
					{
						CCLOG("############%s;bResult:%d######",strFullFilePath.c_str(),bResult);
						break;
					}

				} 

			}

		}

		CC_SAFE_DELETE_ARRAY(pszFilesContent);

	}

};

int VersionControl::delSDData(const std::string& delPath)
{
	CCLOG("############delSDData:del:%s;begin######",delPath.c_str());
	int fail;

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	//fail = rmdir (strTemp.c_str());
	std::string strCMD = "rd /s /q ";
	strCMD += "\"" + delPath + "\"";
	fail = system(strCMD.c_str());
#else
	//fail = rmdir (delPath.c_str());
	std::string strCMD = "rm -r ";
	strCMD += "\"" + delPath + "\"";
	fail = system(strCMD.c_str());
#endif

	CCLOG("############delSDData:del:%s;fail:%d######",delPath.c_str(),fail);

	return fail;
}


void VersionControl::writeVersionFile(std::string& refStrPacketVF)
{
	//
	//本功能使用另外一种方法，在files.txt文件列表中将version.txt文件排列在最后一行，用以保证所有文件已经全部解压完毕
	//
	std::string strSaveFilePath = getSaveFilePath();
	CCFileUtils *pFileUtils = CCFileUtils::sharedFileUtils();
	std::string strVersionFilesPath = CONFIG_OF_VERSION_TXT_FILES_PATH;
	std::string strSDVersionFilesPath = strSaveFilePath + strVersionFilesPath;
	bool bResult = pFileUtils->saveToFile(strSDVersionFilesPath, reinterpret_cast<const unsigned char *>(refStrPacketVF.c_str()), refStrPacketVF.size());
	if (!bResult)
	{
		CCLOG("########VersionControl::writeVersionFile:Error:Path:%s####", strSDVersionFilesPath.c_str());
	}
}


bool VersionControl::isFileNameString(std::string& strFileName)
{
	bool retResult = false;
	int pos = strFileName.find_last_of(".");
	if (0 < pos)
	{
		retResult = true;
	}
	
	return retResult;
};



std::vector<std::string> VersionControl::strSplit(std::string str,std::string pattern)
{
    std::string::size_type pos;
    std::vector<std::string> result;
    str+=pattern;//扩展字符串以方便操作
    int size=str.size();
    for(int i=0; i<size; i++)
    {
        pos=str.find(pattern,i);
        if(pos<size)
        {
            std::string s=str.substr(i,pos-i);

            result.push_back(s);
            i=pos+pattern.size()-1;
        }
    }
    return result;
};


int VersionControl::creatDir(std::string& refDirStr)  
{  
	std::string strDirTmp = refDirStr;

	std::string::size_type i = 0;  
	std::string::size_type iLen = refDirStr.size();

	//在末尾加/ 
	if (iLen > refDirStr.find_last_of("\\/"))  
	{  
		refDirStr += '/';
	}  

	// 创建目录  
	for (i = 0;i <= iLen;i ++)  
	{  
		if (0 != i && (refDirStr[i] == '\\' || refDirStr[i] == '/'))
		{   
			strDirTmp = refDirStr.substr(0, i);  
			if(!createDirectory(strDirTmp.c_str()))
			{
				return -1;
			}
		}   
	}

	return 0;  
};


/*
 * Create a direcotry is platform depended.
 */
bool VersionControl::createDirectory(const char *path)
{
#if (CC_TARGET_PLATFORM != CC_PLATFORM_WIN32)
    mode_t processMask = umask(0);
    int ret = mkdir(path, S_IRWXU | S_IRWXG | S_IRWXO);
    umask(processMask);
    if (ret != 0 && (errno != EEXIST))
    {
        return false;
    }
    
    return true;
#else
    BOOL ret = CreateDirectoryA(path, NULL);
	if (!ret && ERROR_ALREADY_EXISTS != GetLastError())
	{
		return false;
	}
    return true;
#endif
};



//////////////////////////////////////////////////////////////////////////////////////
void VersionControl::onDownloadVersionFileResult(std::string& refStrResponse, CCHTTPRequest* ptrRequest)
{
	CCLOG("#####VersionControl::onDownloadVersionFileResult#######");
	compVersionFile(refStrResponse);
}


void VersionControl::onRetryReqVersionFile()
{
	CCLOG("#####VersionControl::onRetryReqVersionFile#######");
	CCHTTPRequest* ptrHTTPRequest = CCNetwork::createHTTPRequest( this, m_strVersionURL.c_str());
}


void VersionControl::onDownloadVersionFileFail()
{
	CCLOG("######VersionControl::onDownloadVersionFileFail####");
	unzipPacket();
}


///////OnEvent////////////////////////////////////////////////////////////////////
void VersionControl::requestFinished(CCHTTPRequest* request)
{
	CCLOG("####VersionControl::requestFinished#####");

	int nStatusCode = request->getResponseStatusCode();

	CCLOG("##########VersionControl:requestFinished:getResponseStatusCode:%d;getState:%d", nStatusCode, request->getState());
	if (200 != nStatusCode)
	{
		CCLOG("##########VersionControl:requestFinished:getResponseStatusCode:%d;ErrorMessage:%s", nStatusCode, request->getErrorMessage());
		// 重试
		retryRequestVersionFile();
		return;
	}

	std::string strResponse = request->getResponseString();

	onDownloadVersionFileResult(strResponse, request);
}

void VersionControl::requestFailed(CCHTTPRequest* request)
{
	CCLOG("####VersionControl::requestFailed#####");
	int nErrCode = request->getErrorCode();

	CCLOG("##########VersionControl:requestFailed:getErrorCode:%d;ErrorMessage:%s", nErrCode, request->getErrorMessage());
	// 重试
	retryRequestVersionFile();
}
////////////////////////////////////////////////////////////////////////////////////