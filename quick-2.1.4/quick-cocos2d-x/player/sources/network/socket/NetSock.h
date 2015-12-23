#ifndef NETSOCK_H
#define NETSOCK_H
#include "cocos2d.h"
USING_NS_CC;
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
#include <stdio.h>
#include <queue>
#include <fcntl.h>
#include <pthread\pthread.h>
#include <errno.h>
#include <vector>
#include <stdlib.h>
#include <string>
#include <signal.h>
#include <WinSock2.h>
#include<iostream>
#else
#include <stdio.h>
#include <queue>
#include <unistd.h>
#include <fcntl.h>
#include <netinet/in.h>
#include <sys/socket.h>
#include <sys/select.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <errno.h>
#include <poll.h>
#include <vector>
#include <stdlib.h>
#include <string>
#include <signal.h>
#include <netdb.h>

#endif
#include "BytesBuffer.h"
#include "NetFilter.h"
using namespace std;
#define CONN_TIMES 2
#define TIMEOUT_TIMES 5
#define CONN_TIMEOUT 5000 //ms
#define WAIT_RECV 1000//500 //ms
#define RECV_TIMEOUT 200 //ms
#define SEND_TIMEOUT 200 //ms
//#define BUF_SIZE 256 //bytess

class NetSock
{
public:
    NetSock(int conn_timeout = CONN_TIMEOUT, int recv_timeout = RECV_TIMEOUT, int send_timeout = SEND_TIMEOUT, int timeout_times = TIMEOUT_TIMES):
        conn_timeout(conn_timeout), recv_timeout(recv_timeout), send_timeout(send_timeout), timeout_times(timeout_times){}
	virtual ~NetSock();
    
    bool conn_serv();                                                      
    bool retry_connect(int retryTimes = CONN_TIMES);                        
    bool reconnect(); 
	bool close_socket();
    int recv_buf(char *buf, int buf_len);
    int send_buf(char *buf, int buf_len);

    void set_conn_timeout(int timeout);
    int get_conn_timeout();

    void set_recv_timeout(int timeout);
    int get_recv_timeout();

    void set_send_timeout(int timeout);
    int get_send_timeout();

    void set_timeout_times(int times);
    int get_timeout_times();

    string get_host();
    int get_port();
    int get_sockfd();
    
    
    

    static bool is_connected;
    static string host;
    static int port;
    
private:
    bool do_connect();       
    
protected:
    int conn_timeout;
    int recv_timeout;
    int send_timeout;
    int timeout_times;

    static pthread_mutex_t net_mutex;
    static pthread_cond_t net_cond;
	static int nclient;
    static int sockfd;
	static int children;
	

};

#endif // NETSOCK_H


