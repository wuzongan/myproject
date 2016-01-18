package com.kunlun.poker.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.canoe.core.server.support.AbstractCanoeServer;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.Config;
import com.kunlun.poker.domain.ServerNode;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.logClient.LogClientService;
import com.kunlun.poker.service.ServerNodeService;
import com.kunlun.poker.util.ServerLocker;

public class GameServer extends AbstractCanoeServer {

	private static final Logger log = LoggerFactory.getLogger(GameServer.class);
	private IoAcceptor acceptor;
	private Scheduler scheduler;
	@Autowired
	private LogClientService logClientService;

	public IoAcceptor getAcceptor() {
		return acceptor;
	}

	public void setAcceptor(IoAcceptor acceptor) {
		this.acceptor = acceptor;
	}
	
	private void startLogClient(){
	    if(Config.getInstance().isLogEnabled()){
	        this.logClientService.start();
	        if(!this.logClientService.isConnected()){
	            log.debug(Config.getInstance().getServerId()+"服务器未能成功连接日志服务器");
	        }
	        LogClient.setLogClientService(this.logClientService);
	    }
	}
	
	@Override
	public void start() {
		ServerNodeService serverNodeService = ServerNodeService.getInstance();
		ServerNode serverNode = serverNodeService.getOne(Config.getInstance()
				.getServerId());
		int port = serverNode.getPort();

		if (!ServerLocker.lock(Config.getInstance().getLocker(), port)) {
			log.error("请确认服务器已经正常关闭");
			return;
		}
		
		scheduler = ((GameIoHandler)acceptor.getHandler()).getScheduler();
		scheduler.start();
		DefaultIoFilterChainBuilder filterChain = getAcceptor()
				.getFilterChain();
		filterChain.addLast("codec", new ProtocolCodecFilter(
				new IntermediateCodecFactory()));

		LoggingFilter lf = new LoggingFilter();
		lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
		lf.setMessageSentLogLevel(LogLevel.DEBUG);
		filterChain.addLast("logger", lf);
		try {
			getAcceptor().bind(
					new InetSocketAddress(serverNode.getHost(), port));
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			try {
				close();
			} catch (IOException e1) {
				log.error(e.getMessage(), e1);
			}
		}
		this.startLogClient();
	}

	@Override
	public void close() throws IOException {
		getAcceptor().unbind();
		getAcceptor().dispose();
		scheduler.stop();
		if(logClientService != null){
		    logClientService.stop();
		}
	}
}