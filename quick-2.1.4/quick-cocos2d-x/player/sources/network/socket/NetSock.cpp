#include "NetSock.h"

pthread_mutex_t  NetSock::net_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t NetSock::net_cond = PTHREAD_COND_INITIALIZER;

int NetSock::sockfd = -1;
bool NetSock::is_connected = false;
int NetSock::nclient = 0;
string NetSock::host = "";
int NetSock::port = -1;
int NetSock::children = 0;

void NetSock::set_conn_timeout(int timeout)
{
    conn_timeout = timeout;
}

int NetSock::get_conn_timeout()
{
    return conn_timeout;
}

void NetSock::set_recv_timeout(int timeout)
{
    recv_timeout = timeout;
}

int NetSock::get_recv_timeout()
{
    return recv_timeout;
}

void NetSock::set_send_timeout(int timeout)
{
    send_timeout = timeout;
}

int NetSock::get_send_timeout()
{
    return send_timeout;
}

int NetSock::get_sockfd()
{
    return sockfd;
}

string NetSock::get_host()
{
    return host;
}

int NetSock::get_port()
{
    return port;
}

NetSock::~NetSock()
{
    pthread_mutex_lock(&NetSock::net_mutex);
	if (NetSock::children <= 0)
	{
		NetSock::children = 0;
		NetSock::nclient--;
		if (NetSock::nclient <= 0){		
			close_socket();
			NetSock::nclient = 0;
			NetSock::is_connected = false;
		}
	}
    pthread_mutex_unlock(&NetSock::net_mutex);
}

#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
bool NetSock::do_connect()
{
    if(sockfd > 0){
		goto err;
	}

    WORD myVersionRequest;

    WSADATA wsaData;

    myVersionRequest = MAKEWORD(1,1);

    int err;

    err = WSAStartup(myVersionRequest,&wsaData);

    if (err){
		return false;
    }
    sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);//IPPROTO_TCP
    if(sockfd == SOCKET_ERROR){
        printf("create socket error: %s(errno: %d)/n", strerror(errno),errno);
		WSACleanup();
        return false;
    }

    struct pollfd wfds;
    int ret = 0;

	hostent *host_entry = gethostbyname(host.c_str());
	char theHost[64] = {0};
	sprintf(theHost,"%d.%d.%d.%d",(host_entry->h_addr_list[0][0]&0x00ff),
							(host_entry->h_addr_list[0][1]&0x00ff),
							(host_entry->h_addr_list[0][2]&0x00ff),
							(host_entry->h_addr_list[0][3]&0x00ff));
	/*printf(theHost);*/

    sockaddr_in servAddr;
    memset(&servAddr, 0, sizeof(servAddr));
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(port);
	servAddr.sin_addr.S_un.S_addr=inet_addr(theHost);
    wfds.fd = sockfd;
    wfds.events = POLLOUT;

    if((ret = connect(sockfd, (struct sockaddr*)&servAddr, sizeof(servAddr))) == 0)
        return true;
    else if(ret < 0)
    {
        if(errno == EISCONN)
            return true;
        else if(errno != EINPROGRESS){
            goto err;
        }
    }

    if((ret = WSAPoll((struct pollfd*)&wfds, 1, conn_timeout)) < 0)
        goto err;
    else if(ret == 0)
        goto err;

    

err:    
	close_socket();
	return false;
}
#else
bool NetSock::do_connect()
{
    if(sockfd > 0)
        close(sockfd);
    
//    if (WSAStartup(0x202,&wsaData) == SOCKET_ERROR) {
//        AfxMessageBox("WSAStartup failed with error %d\n",WSAGetLastError());
//        WSACleanup();
//        return -1;
//    }
//    
//    sockfd = socket(AF_INET,SOCK_DGRAM,0); /* Open a socket */
//    
//    if (sockfd < 0 ) {
//        AfxMessageBox("Client: Error Opening socket: Error %d\n",
//                      WSAGetLastError());
//        WSACleanup();
//        return -1;
//    }
    sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);//IPPROTO_TCP
    if(sockfd < 0){
        printf("create socket error: %s(errno: %d)/n", strerror(errno),errno);

        return false;
    }

    struct pollfd wfds;
    int ret = 0;

	hostent *host_entry = gethostbyname(host.c_str());
	char theHost[64] = {0};
	sprintf(theHost,"%d.%d.%d.%d",(host_entry->h_addr_list[0][0]&0x00ff),
							(host_entry->h_addr_list[0][1]&0x00ff),
							(host_entry->h_addr_list[0][2]&0x00ff),
							(host_entry->h_addr_list[0][3]&0x00ff));

    long old_mode = fcntl(sockfd, F_GETFL);
    long new_mode = old_mode | O_NONBLOCK;
    if(fcntl(sockfd, F_SETFL, new_mode) < 0)
        goto err;

    struct sockaddr_in servAddr;
    memset(&servAddr, 0, sizeof(servAddr));
    servAddr.sin_family = AF_INET;
    servAddr.sin_port = htons(port);
    if(inet_pton(AF_INET, theHost, &servAddr.sin_addr) <= 0)
        return false;

    wfds.fd = sockfd;
    wfds.events = POLLOUT;

    if((ret = connect(sockfd, (struct sockaddr*)&servAddr, sizeof(servAddr))) == 0)
        return true;
    else if(ret < 0)
    {
        if(errno == EISCONN)
            return true;
        else
            
            
        {
            if(errno != EINPROGRESS)
                goto err;
        }
    }

    if((ret = poll((struct pollfd*)&wfds, 1, conn_timeout)) < 0)
        goto err;
    else if(ret == 0)
        goto err;
    else
    {
        int so_err;
        socklen_t err_len = sizeof(so_err);
        if(getsockopt(sockfd, SOL_SOCKET, SO_ERROR, &so_err, &err_len) < 0)
            goto err;
        if(so_err == 0)
        {
            return true;
        }
        else
            goto err;
    }

err:
    close_socket();
    return false;
}
#endif

bool NetSock::conn_serv()
{
    pthread_mutex_lock(&net_mutex);	
    if(do_connect())
    {
		NetSock::nclient ++;
        NetSock::is_connected = true;
        pthread_cond_broadcast(&net_cond);
    }
    pthread_mutex_unlock(&net_mutex);
    return is_connected;
}

bool NetSock::close_socket()
{
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
	if (sockfd != SOCKET_ERROR)
	{
		shutdown(sockfd,2);
		closesocket(sockfd);
		WSACleanup();
	}
#else
	if (sockfd != -1)
	{
		close(sockfd);
	}
#endif
	nclient--;
	sockfd = -1;
	NetSock::is_connected = false;
	return true;
}
bool NetSock::reconnect()
{
    pthread_mutex_lock(&net_mutex);
	if (NetSock::is_connected)
	{
		close_socket();
	}
    if(do_connect())
    {
		if (NetSock::nclient<=0)
		{
			NetSock::nclient++;
		}
        NetSock::is_connected = true;
        pthread_cond_broadcast(&NetSock::net_cond);
    }
    pthread_mutex_unlock(&net_mutex);
    return is_connected;
}

bool NetSock::retry_connect(int retryTimes)
{
    pthread_mutex_lock(&net_mutex);
    if (NetSock::is_connected)
	{
		close_socket();
		NetSock::is_connected = false;
	}
    int ntimes = 0;
    while(ntimes < retryTimes)
    {
        if(do_connect())
            break;
        ntimes++;
    }
    
    if(ntimes < retryTimes)
    {
        is_connected = true;
        pthread_cond_broadcast(&net_cond);
    }
    else
	{
        is_connected = false;
	}
    pthread_mutex_unlock(&net_mutex);
    return is_connected;
}

int NetSock::recv_buf(char *buf, int buf_len)
{
    if(sockfd < 0 || buf == NULL)
        return -1;

    struct pollfd rfds;
    int ntimes = 0;
    int ret = 0;

    rfds.fd = sockfd;
    rfds.events = POLLIN;

    int left_bytes = buf_len;
    while(left_bytes && ntimes < timeout_times)
    {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
        if((ret = WSAPoll((struct pollfd*)&rfds, 1, recv_timeout)) < 0)
#else
		if((ret = poll((struct pollfd*)&rfds, 1, recv_timeout)) < 0)
#endif
            return buf_len - left_bytes;
        else if(ret == 0)
        {
            ntimes++;
            continue;
        }
        else
        {
            int recvn = recv(sockfd, buf, left_bytes, 0);
            if(recvn < 0)
                {
                    if(errno == EAGAIN)
                        continue;
                    else
                        return buf_len - left_bytes;
            }

            left_bytes -= recvn;
            buf += recvn;
        }
    }
    return buf_len - left_bytes;
}


int NetSock::send_buf(char *buf, int buf_len)
{
    if(sockfd < 0 || buf == NULL)
        return -1;

    struct pollfd wfds;
    int ntimes = 0;
    int ret = 0;

    wfds.fd = sockfd;
    wfds.events = POLLOUT;

    int left_bytes = buf_len;
    while(left_bytes && ntimes < timeout_times)
    {
#if (CC_TARGET_PLATFORM == CC_PLATFORM_WIN32)
         if((ret = WSAPoll((struct pollfd*)&wfds, 1, send_timeout)) < 0)
#else
		 if((ret = poll((struct pollfd*)&wfds, 1, send_timeout)) < 0)
#endif
            return buf_len - left_bytes;
        else if(ret == 0)
        {
            ntimes++;
            continue;
        }
        else
        {
            int sendn = send(sockfd, buf, left_bytes, 0);
            if(sendn < 0)
            {
                if(errno == EAGAIN)
					continue;
                else
                    return buf_len - left_bytes;
            }
            left_bytes -= sendn;
            buf += sendn;
        }
    }
    return buf_len - left_bytes;
}


