#ifndef SENDQUEUE_H
#define SENDQUEUE_H

#include "NetSock.h"
class EnterLock
{
private:
    EnterLock(EnterLock&);
    EnterLock();
public:
    pthread_mutex_t & _sync;
    EnterLock(pthread_mutex_t& sync):_sync(sync)
    {
        pthread_mutex_lock(&_sync);
    }
    ~EnterLock()
    {
        pthread_mutex_unlock(&_sync);
    }
    
};



class SendQueue : public NetSock
{
public:
    SendQueue();
	virtual ~SendQueue();
    void push(BytesBufferU16* pack);
	bool choosSever(bool a);

    bool start();                     
    void stop();                     
    bool init();                    
    void destory();                 
    void reset();
    
   

private:
    void send();
    static void* pth_send(void *arg);
    
    pthread_t ptid;
    queue<BytesBufferU16*> que;
    pthread_mutex_t mutex;
    pthread_attr_t attr;
    pthread_cond_t cond;
    bool cancel_send;
    int curfd;
    bool inited;
};
#endif // SENDQUEUE_H


