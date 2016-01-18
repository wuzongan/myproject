package com.kunlun.poker.logClient;

import org.apache.mina.core.session.IoSession;

import com.kunlun.poker.log.LogMessage;

public interface LogClientService {

    void sendLogMessage(LogMessage logMessage);
    
    void setIoSession(IoSession ioSession);
    
    void start();
    
    void stop();
    
    boolean isConnected();
}
