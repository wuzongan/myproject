#ifndef __Updater__
#define __Updater__

#include "cocos2d.h"
#include "ExtensionMacros.h"

#if (CC_TARGET_PLATFORM != CC_PLATFORM_WINRT) && (CC_TARGET_PLATFORM != CC_PLATFORM_WP8)
#include <string>
#include <curl/curl.h>
#include <pthread.h>

NS_CC_EXT_BEGIN

class UpdaterDelegateProtocol;

/*
 *  This class is used to auto update resources, such as pictures or scripts.
 *  The updated package should be a zip file. And there should be a file named
 *  version in the server, which contains version code.
 *  @js NA
 *  @lua NA
 */
class CC_DLL Updater
{
public:
    enum ErrorCode
    {
        // Error caused by creating a file to store downloaded data
        kCreateFile,
        /** Error caused by network
         -- network unavaivable
         -- timeout
         -- ...
         */
        kNetwork,
        /** There is not a new version
         */
        kNoNewVersion,
        /** Error caused in uncompressing stage
         -- can not open zip file
         -- can not read file global information
         -- can not read file information
         -- can not create a directory
         -- ...
         */
        kUncompress,
    };
    
    enum StateCode
    {
        kDownStart,
        kDownDone,
        kUncompressStart,
        kUncompressDone,
    };
    
    /* @brief Creates a Updater with new package url, version code url and storage path.
     *
     * @param packageUrl URL of new package, the package should be a zip file.
     * @param versionFileUrl URL of version file. It should contain version code of new package.
     * @param storagePath The path to store downloaded resources.
     */
    Updater();
    
    virtual ~Updater();
    
    /* @brief Download new package if there is a new version, and uncompress downloaded zip file.
     *        Ofcourse it will set search path that stores downloaded files.
     */
    virtual void update(const char* zipUrl, const char* zipFile, const char* unzipTmpDir, bool resetBeforeUnZip=true);
    
    /** @brief Sets delegate, the delegate will receive messages
     */
    void setDelegate(UpdaterDelegateProtocol *delegate);
    
    /** @brief Register script handler, the hander will receive messages
     */
    void registerScriptHandler(int handler);
    void unregisterScriptHandler(void);

    /** @brief Sets connection time out in seconds
     */
    void setConnectionTimeout(unsigned int timeout);
    
    /** @brief Gets connection time out in secondes
     */
    unsigned int getConnectionTimeout();
    bool createDirectory(const char *path);
    bool removeDirectory(const char* path);
    const char* getUpdateInfo(const char* url);
    
    /* downloadAndUncompress is the entry of a new thread 
     */
    friend void* assetsManagerDownloadAndUncompress1(void*);
    friend int assetsManagerProgressFunc1(void *, double, double, double, double);
    
protected:
    bool downLoad(const char* zipUrl, const char* zipFile);
    bool uncompress(const char* zipFile, const char* unzipTmpDir, bool resetBeforeUnZip);
    void sendErrorMessage(ErrorCode code);
    void sendStateMessage(StateCode code);
    void checkUnZipTmpDir();
    
    std::string _zipFile;
    std::string _zipUrl;
    std::string _unzipTmpDir;
    std::string _updateInfoString;
    bool _resetBeforeUnZip;
    
private:
    typedef struct _Message
    {
    public:
        _Message() : what(0), obj(NULL){}
        unsigned int what; // message type
        void* obj;
    } Message;
    
    class Helper : public cocos2d::CCObject
    {
    public:
        Helper();
        ~Helper();
        
        virtual void update(float dt);
        void sendMessage(Message *msg);
        
    private:
        void handleUpdateSucceed(Message *msg);
        void handlerState(Message* msg);
        void handlerError(Message* msg);
        void handlerProgress(Message* msg);
        
        std::list<Message*> *_messageQueue;
        pthread_mutex_t _messageQueueMutex;
    };
    
private:
    //! The path to store downloaded resources.
    std::string _storagePath;

    CURL *_curl;
    Helper *_schedule;
    pthread_t *_tid;
    unsigned int _connectionTimeout;
    
    UpdaterDelegateProtocol *_delegate; // weak reference
    int _scriptHandler; // script handler
};

class UpdaterDelegateProtocol
{
public:
    /* @brief Call back function for error
       @param errorCode Type of error
     */
    virtual void onError(Updater::ErrorCode errorCode) {};
    /** @brief Call back function for recording downloading percent
        @param percent How much percent downloaded
        @warn This call back function just for recording downloading percent.
              Updater will do some other thing after downloading, you should
              write code in onSuccess() after downloading. 
     */
    virtual void onProgress(int percent) {};
    /** @brief Call back function for success
     */
    virtual void onSuccess() {};
    virtual void onState(Updater::StateCode stateCode) {};
};

NS_CC_EXT_END;
#endif // CC_TARGET_PLATFORM != CC_PLATFORM_WINRT
#endif /* defined(__Updater__) */