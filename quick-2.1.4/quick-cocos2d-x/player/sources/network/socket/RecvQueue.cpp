#include "RecvQueue.h"
#include "NetworkLogic.h"
#include <iostream>
RecvQueue::RecvQueue():cancel_recv(false), inited(false), curfd(-1)
{
	pthread_mutex_lock(&NetSock::net_mutex);
	NetSock::children++;
	pthread_mutex_unlock(&NetSock::net_mutex);
}

RecvQueue::~RecvQueue()
{
    if(inited)
    {
        destory();
    }
//	pthread_cancel(ptid);
}

bool RecvQueue::init()
{
    if(inited)
        return true;

    if(pthread_attr_init(&attr) < 0)
    {
        destory();
		return false;
	}
    if(pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_DETACHED) < 0)
    {
        pthread_attr_destroy(&attr);
        destory();
		return false;
    }
    if(pthread_mutex_init(&mutex, NULL) < 0)
    {
        pthread_attr_destroy(&attr);
        destory();
		return false;
    }
	if(pthread_mutex_init(&sock_mutex, NULL) < 0)
    {
        pthread_attr_destroy(&attr);
        pthread_mutex_destroy(&mutex);
        destory();
		return false;
    }
    
    inited = true;
    return true;
}

void RecvQueue::destory()
{
    pthread_mutex_lock(&NetSock::net_mutex);
    NetSock::children--;
    pthread_mutex_unlock(&NetSock::net_mutex);
	pthread_cond_broadcast(&NetSock::net_cond);
	reset();
    cancel_recv = true;
    if(inited)
    {
        pthread_mutex_destroy(&sock_mutex);
        pthread_attr_destroy(&attr);
        pthread_mutex_destroy(&mutex);
        inited = false;
    }
}

void RecvQueue::stop()
{
    destory();
}


void RecvQueue::recv()
{
    struct pollfd rfds;
    rfds.events = POLLIN;
      
    int ret = 0; 
    while(true)
    {
        if(cancel_recv)
        {
            break;
		}
		if(curfd != sockfd)
        {
//			reset();
			curfd = sockfd;
        }
        
        pthread_mutex_lock(&sock_mutex);
        while(!NetSock::is_connected)
        {
			if (cancel_recv )
			{
				pthread_mutex_unlock(&sock_mutex);
				break;
			}
            pthread_cond_wait(&NetSock::net_cond, &sock_mutex);
		}		
		if(cancel_recv )
        {
            break;
        }
		pthread_mutex_unlock(&sock_mutex);

		if(curfd != sockfd)
		{
            continue;
		}

        rfds.fd = curfd;
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32) 
        if((ret = WSAPoll((struct pollfd*)&rfds, 1, WAIT_RECV)) <= 0)
#else 
		if((ret = poll((struct pollfd*)&rfds, 1, WAIT_RECV)) <= 0)
#endif
        {
            continue;
        }
        else
        {
            uint16_t head;
            int recvn = recv_buf((char*)&head, sizeof(head));
            if(recvn != sizeof(head))
            {
				if(curfd == sockfd)
				{
                    pthread_mutex_lock(&NetSock::net_mutex);
					NetSock::is_connected = false;
                    NetSock::sockfd = -1;
                    pthread_mutex_unlock(&NetSock::net_mutex);
                }
				if (!cancel_recv)
				{
					reset();
				}

                continue;
            }
            
            uint16_t body_len = 0;
            memcpy(&body_len, &head, sizeof(head));
            body_len = htons(body_len);
            
            uint16_t pack_len = body_len + sizeof(head);
            char *buf = (char*)malloc(body_len + sizeof(head));
            if(buf == NULL)
			{
                return;
			}
            
            memcpy(buf, (char*)&head, sizeof(head));
            char *pbuf = buf;
            pbuf += sizeof(head);
            recvn += recv_buf(pbuf, body_len);
#ifdef DEBUG
            
//            printf("#######recv pack_len = %d, recv %d##########\n", pack_len, recvn);
//            for(int i = 0; i < recvn; i++)
//            {
//                printf("%02x ",  buf[i] & 0xff);
//            }
//            printf("\n###################recv##################\n");
//
            fflush(stdout);
#endif
            if(recvn != pack_len)
            {
#ifdef DEBUG
//                printf("#######package received incompleted!#####\n");
                fflush(stdout);
#endif
                if(curfd != sockfd)
                {
                    pthread_mutex_lock(&NetSock::net_mutex);
                    NetSock::is_connected = false;
                    NetSock::sockfd = -1;
                    pthread_mutex_unlock(&NetSock::net_mutex);
                }
				if (!cancel_recv)
				{
					reset();
				}

                free(buf);
                continue;
            }
            //            Message *msg = new Message(buf, recvn);
            BytesBufferU16 bb(buf,recvn);
            BytesBufferU16 * nbb;
            if(NetFilter::fromNet(&bb, &nbb))
            {
                //
#ifdef DEBUG
                //            printf("#######recv type = 0x%08x ##########\n",msg->getType());
                fflush(stdout);
#endif
				pthread_mutex_lock(&mutex);
                que.push(nbb);
                pthread_mutex_unlock(&mutex);
                //
                
            }
			if (buf)
				free(buf);
        }
    }
}

BytesBufferU16* RecvQueue::popData()
{   
	pthread_mutex_lock(&mutex);
    if(que.empty())
    {
		pthread_mutex_unlock(&mutex);
        return NULL;
    }
    BytesBufferU16* msg = que.front();
    que.pop();
    pthread_mutex_unlock(&mutex);
    return msg;
}

void RecvQueue::reset()
{
    if(!cancel_recv){
        pthread_mutex_lock(&mutex);
        while(!que.empty())
        {
            BytesBufferU16* msg = que.front();
            if(msg != NULL)
                delete msg;
            que.pop();
        }
        pthread_mutex_unlock(&mutex);
    }
}

void* RecvQueue::pth_recv(void *arg)
{
    RecvQueue *recv_que = (RecvQueue*)arg;
    recv_que->recv();
	return (void*)0;
}

bool RecvQueue::start()
{
    if(inited)
        return true;
	if(init())
	{
		int pthreads;
		pthreads = pthread_create(&ptid, &attr, pth_recv, (void*)this);
		if (pthreads !=0)
		{
			printf("errorConnet.....%d",pthreads);
			return false;
		}
		return true;
	}
}

