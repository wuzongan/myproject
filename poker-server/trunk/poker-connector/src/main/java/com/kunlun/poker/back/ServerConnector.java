package com.kunlun.poker.back;

import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.ServerNode;
import com.kunlun.poker.front.SessionMap;
import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.server.CommonProtocol;
import com.kunlun.poker.server.IntermediateCodecFactory;
import com.kunlun.poker.util.DataUtil;

public class ServerConnector extends Thread {
	private static final Logger logger = LoggerFactory
			.getLogger(ServerConnector.class);
	private ServerNode server;
	private static final int QUEUE_POOL_TIMEOUT_NUNITES = 1;
	private final LinkedBlockingQueue<IntermediateMessage> messageQueue;
	private static final IoHandler roomIoHandler = new ServerIoHandler();
	private IoSession session;
	private NioSocketConnector connector;

	public ServerConnector(ServerNode server) {
		super("ServerConnector-" + server.getId());

		this.server = server;
		messageQueue = new LinkedBlockingQueue<IntermediateMessage>();
		connector = new NioSocketConnector();
		connector.setHandler(roomIoHandler);
		connector.setConnectTimeoutMillis(10000);
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new IntermediateCodecFactory()));
	}

	public void sendMessage(IntermediateMessage message) {
		if (connector.isActive())
			try {
				messageQueue.put(message);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
	}

	@Override
	public void run() {
		super.run();

		try {
			for (;;) {
				if (!connector.isActive()) {
					try {
						ConnectFuture future = connector
								.connect(new InetSocketAddress(
										server.getHost(), server.getPort()));
						future.awaitUninterruptibly();
						session = future.getSession();
						session.setAttribute("serverId", server.getId());
					} catch (RuntimeIoException e) {
						logger.error(e.getMessage() + " - server["
								+ server.getId() + "]");
						Thread.sleep(5000);
						continue;
					}
				}

				IntermediateMessage message = messageQueue.poll(
						QUEUE_POOL_TIMEOUT_NUNITES, TimeUnit.MINUTES);

				if (message != null) {
					session.write(message);
				}

				yield();
			}

		} catch (InterruptedException e) {
			interrupt();
			connector.dispose();
		}
	}
}

class ServerIoHandler extends IoHandlerAdapter {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerIoHandler.class);
	private SessionMap sessionMap = SessionMap.getInstance();

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		IntermediateMessage im = (IntermediateMessage) message;
		int opcode = DataUtil.readShort(im.getBody());

		im.setServerId((Integer) session.getAttribute("serverId"));
		for (int sessionId : im.getSessionIds()) {
			IoSession frontSession = sessionMap.get(sessionId);
			if (frontSession != null && frontSession.isConnected()) {
				if (opcode == CommonProtocol.DISCONNECT) {
					frontSession.close(true);
				} else {
					frontSession.write(im);
				}
			}
		}
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage(), cause);
	}
}
