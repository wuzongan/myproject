#ifndef RECVQUEUE_H
#define RECVQUEUE_H

#include "NetSock.h"

class RecvQueue : public NetSock
{
public:
    RecvQueue();

    BytesBufferU16* popData();
    bool start();            
    void stop();              
    
    bool init();             
    void destory();        
    void reset();

    virtual ~RecvQueue();
private:
    void recv();
    static void *pth_recv(void *arg);

    pthread_t ptid;
    queue<BytesBufferU16*> que;
    pthread_mutex_t mutex;
    pthread_attr_t attr;
	pthread_mutex_t sock_mutex;
    bool cancel_recv;
    int skip_bytes;
    int curfd;
    bool inited;
};


#endif // RECVQUEUE_H
