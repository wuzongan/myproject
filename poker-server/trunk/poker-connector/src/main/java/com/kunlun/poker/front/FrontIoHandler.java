package com.kunlun.poker.front;

import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.Config;
import com.kunlun.poker.back.ServerManager;
import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.server.CommonProtocol;
import com.kunlun.poker.util.DataUtil;

/**
 *
 * @author Administrator
 */
public class FrontIoHandler extends IoHandlerAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(FrontIoHandler.class);
	private IdManager idManager;

	public FrontIoHandler() {
		idManager = new IdManager();
	}

	@Override
	public void sessionIdle(IoSession session,
			org.apache.mina.core.session.IdleStatus status) throws Exception {
		if (status == IdleStatus.READER_IDLE) {
			logger.debug("超时被关闭");
			session.close(true);
		}
	};

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);

		SocketSessionConfig cfg = (SocketSessionConfig) session.getConfig();
		cfg.setReceiveBufferSize(2 * 1024 * 1024);
		cfg.setReadBufferSize(2 * 1024 * 1024);
		cfg.setKeepAlive(true);
		cfg.setSoLinger(0);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		super.sessionOpened(session);
		int id = idManager.fetch();
		session.setAttribute("sessionId", id);
		SessionMap.getInstance().put(id, session);
	};

	public void messageReceived(IoSession session, Object message)
			throws Exception {
		IntermediateMessage im = (IntermediateMessage) message;
		int opcode = DataUtil.readShort(im.getBody());

		if (im.getServerId() != 0) {
			logger.debug("收到发往服务器" + im.getServerId() + "的消息" + opcode);
			im.setSource((InetSocketAddress)session.getRemoteAddress());
			ServerManager.getInstance().sendMessage(im);
		} else if (opcode == CommonProtocol.PING) {
			IntermediateMessage response = new IntermediateMessage();
			response.setServerId(0);
			response.setBody(DataUtil.writeShort(CommonProtocol.PONG));
			session.write(response);
		}
	};

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		Integer id = (Integer) session.getAttribute("sessionId");

		IntermediateMessage im = new IntermediateMessage();
		im.setSessionIds(new int[] { id });
		im.setBody(DataUtil.writeShort(CommonProtocol.DISCONNECTED));

		ServerManager.getInstance().sendToAll(im);

		SessionMap.getInstance().remove(id);
		idManager.recycle(id);
	}
}

class IdManager {
	private AtomicInteger idGenerator;
	private ConcurrentLinkedQueue<Integer> idleIds;

	public IdManager() {
		idGenerator = new AtomicInteger();
		idleIds = new ConcurrentLinkedQueue<Integer>();
	}

	public int fetch() {
		Integer id = idleIds.poll();

		if (id == null) {
			int serverId = Config.getInstance().getServerId();
			id = idGenerator.incrementAndGet() | (serverId << 24);
		}

		return id;
	}

	public void recycle(Integer id) {
		idleIds.add(id);
	}
}