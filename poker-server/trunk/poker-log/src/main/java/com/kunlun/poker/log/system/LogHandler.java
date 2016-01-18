package com.kunlun.poker.log.system;

import java.util.Map;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kunlun.poker.log.LogMessage;
import com.kunlun.poker.log.service.LogService;

public class LogHandler extends IoHandlerAdapter {
    private static final Logger logger = LoggerFactory
            .getLogger(LogHandler.class);
    
    @Autowired
    private LogService logService;
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	super.sessionCreated(session);
    	
//		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();  
//        cfg.setReceiveBufferSize(2 * 1024 * 1024);  
//        cfg.setReadBufferSize(2 * 1024 * 1024);  
//        cfg.setKeepAlive(true);
//        cfg.setSoLinger(0);
    }
    
    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        if(message instanceof LogMessage){
            LogMessage logMessage = (LogMessage) message;
            @SuppressWarnings("unchecked")
            Map<String,String> value = (Map<String, String>) logMessage.getValue();
            logService.doLog(value);
            logger.debug(""+logMessage.getValue());
        }
    }
    
    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        super.exceptionCaught(session, cause);
        logger.error(cause.getMessage(), cause);
    }

    
    
}
