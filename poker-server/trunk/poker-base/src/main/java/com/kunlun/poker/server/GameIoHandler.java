package com.kunlun.poker.server;

import java.io.IOException;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;

import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.server.LifecycleHandler;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.message.MessageConsistencyKeeper;
import com.kunlun.poker.message.MessageTask;

/**
 *
 * @author panzd
 */
public class GameIoHandler extends IoHandlerAdapter {
	private Scheduler scheduler;
	private GameResponseSender responderSender;
	private MessageCodec messageCodec;
	private MessageConsistencyKeeper messageConsistencyKeeper;
	private LifecycleHandler lifecycleHandler;
	private SessionRoleManager sessionRoleManager;

	public void setMessageConsistencyKeeper(
			MessageConsistencyKeeper messageConsistencyKeeper) {
		this.messageConsistencyKeeper = messageConsistencyKeeper;
	}

	public void setMessageCodec(MessageCodec messageCodec) {
		this.messageCodec = messageCodec;
	}

	public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
		this.lifecycleHandler = lifecycleHandler;
	}

	public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
		this.sessionRoleManager = sessionRoleManager;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void sessionCreated(IoSession session) throws Exception
	{
		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();  
        cfg.setReceiveBufferSize(2 * 1024 * 1024);  
        cfg.setReadBufferSize(2 * 1024 * 1024);  
        cfg.setKeepAlive(true);
        cfg.setSoLinger(0);
	}
	
	public void sessionOpened(IoSession session) throws Exception {
		responderSender.getIoSessions().add(session);
	}

	public void sessionClosed(IoSession session) throws Exception {
		responderSender.getIoSessions().remove(session);
	};

	@Override
	public void messageReceived(IoSession ioSession, Object message)
			throws IOException {

		IntermediateMessage im = (IntermediateMessage) message;

		MessageTask messageTask = new MessageTask(im);
		messageTask.setLifecycleHandler(lifecycleHandler);
		messageTask.setMessageCodec(messageCodec);
		messageTask.setSessionRoleManager(sessionRoleManager);

		scheduler.submit(messageTask, im, messageConsistencyKeeper.getConsistencyCode(im));
	}

	public void setResponderSender(GameResponseSender responderSender) {
		this.responderSender = responderSender;
	}

}
