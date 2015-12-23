
#ifndef  _VERSION_CONTROL_H_
#define  _VERSION_CONTROL_H_

#include "cocos2d.h"
#include "network/CCNetwork.h"

using namespace cocos2d;
using namespace cocos2d::extra;



class VersionControl:public CCHTTPRequestDelegate
{
public:
	VersionControl();
	virtual ~VersionControl();

	// 进行更新操作
	void	updateVersion(void* ptrAppDelegate);

	std::string	getSaveFilePath();

	std::vector<std::string>	strSplit(std::string str,std::string pattern);
	bool	isFileNameString(std::string& strFileName);
	int		creatDir(std::string& refDirStr);

	CCFileUtils* getCCFileUtils();
	
	///////OnEvent////////////////////////////////////////////////////////////////////
	void	requestFinished(CCHTTPRequest* request);
	void	requestFailed(CCHTTPRequest* request);
	//////////////////////////////////////////////////////////////////////////////////

private:
	void	copyFile();
	int		delSDData(const std::string& delPath);
	bool	createDirectory(const char *path);
	void	writeVersionFile(std::string& strPacketVF);

	void	unzipPacket();
	void	downloadVersionFile();
	void	compVersionFile(const std::string& refStrVerFileContent);

	std::string getPacketVersionFile();

	void	runAppDelegate();
	void	runFollowUpFunction();

	//////////////////////////////////////////////////////////////////////////////////
	void	retryRequestVersionFile();
	void	onDownloadVersionFileResult(std::string& strResponse, CCHTTPRequest* request);
	void	onRetryReqVersionFile();
	void	onDownloadVersionFileFail();
	/////////////////////////////////////////////////////////////////////////////////

	int			m_nRetryTotalNum;
	int			m_nRetryReqNum;
	std::string	m_strVersionURL;

	void*		m_pAppDelegate;
};


#endif // _VERSION_CONTROL_H_