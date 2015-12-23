#include "SendQueue.h"
#include "cocos2d.h"
USING_NS_CC;
SendQueue::SendQueue():cancel_send(false), inited(false), curfd(-1)
{
	pthread_mutex_lock(&NetSock::net_mutex);
	NetSock::children++;
	pthread_mutex_unlock(&NetSock::net_mutex);
}

SendQueue::~SendQueue()
{
    if(inited)
    {
		destory();
    }
//	pthread_cancel(ptid);
}

bool SendQueue::choosSever(bool a)
{
	if(a)
	{
		if(!NetSock::reconnect())
		{
			return false;
		}
	}
	else
	{
		if(!NetSock::conn_serv())
		{
			return false;
		}
	}
	curfd = sockfd;
    return true;
}
bool SendQueue::init()
{
    if(inited)
        return true;
    
    if(pthread_cond_init(&cond, NULL) < 0)
	{
        destory();
		return false;
	}
    if(pthread_attr_init(&attr) < 0)
    {
        pthread_cond_destroy(&cond);
        destory();
		return false;
    }
    if(pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED) < 0)
    {
        pthread_cond_destroy(&cond);
        pthread_attr_destroy(&attr);
        destory();
		return false;
    }
    if(pthread_mutex_init(&mutex, NULL) < 0)
    {
        pthread_cond_destroy(&cond);
        pthread_attr_destroy(&attr);
        destory();
		return false;
    }
    
    inited = true;
    return true;
}

void SendQueue::destory()
{
    pthread_mutex_lock(&NetSock::net_mutex);
	NetSock::children--;
    pthread_mutex_unlock(&NetSock::net_mutex);
	cancel_send = true;
	pthread_cond_broadcast(&cond);
	pthread_cond_broadcast(&NetSock::net_cond);
    reset();
    if(inited)
    {
        pthread_cond_destroy(&cond);
        pthread_attr_destroy(&attr);
        pthread_mutex_destroy(&mutex);
        inited = false;
    }
}


void SendQueue::stop()
{
    destory();
}

void SendQueue::send()
{
    while(true)
    {
		if(cancel_send)
            break;
        if(curfd != sockfd)
        {
            curfd = sockfd;
            reset();
        }
        
        pthread_mutex_lock(&mutex);
        while(que.empty() || cancel_send)
            pthread_cond_wait(&cond, &mutex);
        
        if(cancel_send)
        {
            pthread_mutex_unlock(&mutex);
            break;
        }
        
        BytesBufferU16* msg = que.front();
		que.pop();
        if(!msg){
			pthread_mutex_unlock(&mutex);
            break;
        }
        char *buf = msg->data();
        int length = msg->size();
        
        while(!NetSock::is_connected || cancel_send)
            pthread_cond_wait(&NetSock::net_cond, &mutex);
        
        if(cancel_send)
        {
            pthread_mutex_unlock(&mutex);
            break;
        }
        pthread_mutex_unlock(&mutex);
        
        if(curfd != sockfd)
            continue;
        
        int sendn = send_buf(buf, length);
        
#ifdef  DEBUG
        //        printf("#########type = 0x%08x,send %d##########\n", msg->pack(), sendn);
        
//        for(int i = 0; i < sendn; ++i)
//        {
//            printf("%02x ", buf[i] & 0xff);
//        }
//        printf("\n################send###################\n");
        
        fflush(stdout);
#endif
		
        if(sendn != length)
        {
            pthread_mutex_lock(&NetSock::net_mutex);
            NetSock::is_connected = false;
            pthread_mutex_unlock(&NetSock::net_mutex);
            reset();
        }
		delete msg;
    }//while(true)
}

void SendQueue::reset()
{
    EnterLock enl(mutex);
    while(!que.empty())
    {
        BytesBufferU16* msg = que.front();
		que.pop();
        if(msg != NULL)
		{
            delete msg;
		}
    }
}

void* SendQueue::pth_send(void *arg)
{
    SendQueue *send_que  = (SendQueue*)arg;
    send_que->send();
	return (void*)0;

}

void SendQueue::push(BytesBufferU16* msg)
{
    if(!is_connected || cancel_send)
    {
        delete msg;
        return;
    }
    
    EnterLock enl(mutex);
    if(msg->size() != 0)
    {
            que.push(msg);
			if(que.size()==1)
        {
            pthread_cond_broadcast(&cond);
        }
    }
    else
    {
        delete msg;
    }
}

bool SendQueue::start()
{
    if(inited)
        return true;
	if(init())
	{
		int pthreads;
		pthreads = pthread_create(&ptid, &attr, pth_send, (void*)this);
		if (pthreads !=0)
		{
			printf("errorConnet.....%d",pthreads);
			return false;
		}
		return true;
	}
}

