package com.kunlun.poker.message;

import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.server.LifecycleHandler;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.kunlun.poker.server.CommonProtocol;
import com.kunlun.poker.server.GameSession;

public class MessageTask implements Runnable {
	private final IntermediateMessage message;
	private MessageCodec messageCodec;
	private LifecycleHandler lifecycleHandler;
	private SessionRoleManager sessionRoleManager;

	public void setMessageCodec(MessageCodec messageCodec) {
		this.messageCodec = messageCodec;
	}

	public void setLifecycleHandler(LifecycleHandler lifecycleHandler) {
		this.lifecycleHandler = lifecycleHandler;
	}

	public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
		this.sessionRoleManager = sessionRoleManager;
	}

	public SessionRoleManager getSessionRoleManager() {
		return sessionRoleManager;
	}

	public MessageTask(IntermediateMessage message) {
		super();
		this.message = message;
	}

	@Override
	public void run() {
		if (message == null)
			return;

		Request request = messageCodec.decodeRequest(message);
		int sessionId = message.getSessionIds()[0];
		GameSession session;
		boolean newSession;
		synchronized (sessionRoleManager) {
			session = (GameSession) sessionRoleManager
					.getSession((long) sessionId);
			if (session == null) {
				newSession = true;
				session = new GameSession(sessionId, message.getSource());
				sessionRoleManager.addSession(session);
			} else {
				session.setRemoteAddress(message.getSource());
				newSession = false;
			}
		}

		request.setSession(session);

		try {
			if (request.getOpcode() == CommonProtocol.DISCONNECTED) {
				lifecycleHandler.onDisconnected(session);
				sessionRoleManager.removeSession(session);
			} else {
				if (newSession) {
					lifecycleHandler.onConnected(session);
				}
				lifecycleHandler.onRequest(request);
			}
		} catch (Exception e) {
			lifecycleHandler.onError(session, e);
		}
	}

}
