package com.kunlun.poker.logClient.impl;

import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.domain.ServerNode;
import com.kunlun.poker.log.LogMessage;
import com.kunlun.poker.log.LogProtocolCodecFactory;
import com.kunlun.poker.logClient.LogClientService;
import com.kunlun.poker.service.ServerNodeService;

public class LogClientServiceImpl implements LogClientService{
    private static final String GROUPNAME = "logClient";
    @Autowired
    private Scheduler scheduler;
    private IoSession ioSession;
    private NioSocketConnector connector;
    
    public void sendLogMessage(LogMessage message){
        if(ioSession != null && ioSession.isConnected() && message != null){
            scheduler.submit(new Runnable() {
                @Override
                public void run() {
                    ioSession.write(message);
                }
            }, null, GROUPNAME, 0);
        }
    }
    
    public void setIoSession(IoSession ioSession){
        this.ioSession = ioSession;
    }

    @Override
    public void start() {
        ServerNodeService serverNodeService = ServerNodeService.getInstance();
        ServerNode serverNode = serverNodeService.getOne(ServerNode.ID_LOG);
        int port = serverNode.getPort();
        
        connector = new NioSocketConnector();
        DefaultIoFilterChainBuilder chain=connector.getFilterChain();  
        ProtocolCodecFilter filter= new ProtocolCodecFilter(new LogProtocolCodecFactory());   
        chain.addLast("codec",filter);  
        connector.setHandler(new DefalutHandler());
        connector.setConnectTimeoutCheckInterval(30);  
        ConnectFuture cf = connector.connect(new InetSocketAddress(serverNode.getHost(),port));  
        cf.awaitUninterruptibly();  
        if(cf.isConnected()){
            ioSession = cf.getSession();
        }
    }

    @Override
    public void stop() {
        if(connector!=null){
            connector.dispose();  
        }
    }
    
    class DefalutHandler extends IoHandlerAdapter{
        
    }

    @Override
    public boolean isConnected() {
        return this.ioSession!=null;
    }
    
}
