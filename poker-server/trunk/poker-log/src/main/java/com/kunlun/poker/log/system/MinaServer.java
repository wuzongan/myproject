package com.kunlun.poker.log.system;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.buffer.SimpleBufferAllocator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.transport.socket.DefaultSocketSessionConfig;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.Config;
import com.kunlun.poker.domain.ServerNode;
import com.kunlun.poker.log.LogProtocolCodecFactory;
import com.kunlun.poker.service.ServerNodeService;
import com.kunlun.poker.util.ServerLocker;

public class MinaServer {
    private static final Logger logger = LoggerFactory.getLogger(MinaServer.class);
    
    private IoHandler ioHandler;
    private ThreadPoolExecutor threadPool;
    private SocketAcceptor acceptor;
    
    public void setIoHandler(IoHandler ioHandler){
        this.ioHandler = ioHandler;
    }
    
    public void setThreadPool(ThreadPoolExecutor pool){
        this.threadPool = pool;
    }
    
    public void start(){
        try {
            if(this.ioHandler == null){
                throw new NullPointerException("ioHandler is null...");
            }
            if(this.threadPool == null){
                throw new NullPointerException("threadPool is null...");
            }
            
            int serverPort = -1;
            ServerNodeService serverNodeService = ServerNodeService.getInstance();
            ServerNode serverNode = serverNodeService.getOne(Config.getInstance()
                    .getServerId());
            serverPort = serverNode.getPort();
            
            if(serverPort==-1){
                logger.error("服务器启动失败...端口获取异常");
                return;
            }
            
            if (!ServerLocker.lock(Config.getInstance().getLocker(), serverPort)) {
                logger.error("请确认服务器已经正常关闭");
                return;
            }
            
            IoBuffer.setUseDirectBuffer(false);
            IoBuffer.setAllocator(new SimpleBufferAllocator());
            this.acceptor = new NioSocketAcceptor();
            this.acceptor.setReuseAddress(true);
            DefaultIoFilterChainBuilder filterChain = this.acceptor
                    .getFilterChain();
            filterChain.addLast("codec", new ProtocolCodecFilter(new LogProtocolCodecFactory()));
            filterChain.addLast("threadPool", new ExecutorFilter(this.threadPool));
            this.acceptor.setHandler(this.ioHandler);
            InetSocketAddress address = new InetSocketAddress(serverNode.getHost().trim(),serverPort);
            this.acceptor.bind(address);
            logger.info("Listening on "+address.getHostName()+":"+ address.getPort());
        } catch (Throwable e) {
            logger.error("", e);
        }
    }
    
    public SocketSessionConfig getSessionConfig(){
        SocketSessionConfig sessionConfig = new DefaultSocketSessionConfig();
        sessionConfig.setSoLinger(0);
        sessionConfig.setKeepAlive(true);
        sessionConfig.setReuseAddress(true);
        sessionConfig.setTcpNoDelay(true);
        sessionConfig.setReadBufferSize(2048);
        sessionConfig.setIdleTime(IdleStatus.READER_IDLE, 60);
        return sessionConfig;
    }
    
    public void stop(){
        if(this.acceptor != null){
            this.acceptor.unbind();
            this.acceptor.dispose();
            this.acceptor = null;
        }
        
        if(this.threadPool != null){
            this.threadPool.shutdown();
            try {
                this.threadPool.awaitTermination(5000l, TimeUnit.MILLISECONDS);
            } catch (Throwable e) {
                logger.error("停服抛出了异常",e);
            }
        }
    }
    
    
}
