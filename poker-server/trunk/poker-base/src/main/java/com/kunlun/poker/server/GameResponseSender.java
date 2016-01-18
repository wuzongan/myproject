package com.kunlun.poker.server;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.message.MessageCodec;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.kunlun.poker.message.IntermediateMessage;

public class GameResponseSender implements ResponseSender {
	
	private static final Logger logger = LoggerFactory
			.getLogger(GameResponseSender.class);
	private MessageCodec messageCodec;
	private List<IoSession> ioSessions;

	public MessageCodec getMessageCodec() {
		return messageCodec;
	}

	public void setMessageCodec(MessageCodec messageCodec) {
		this.messageCodec = messageCodec;
	}

	public List<IoSession> getIoSessions() {
		return ioSessions;
	}
	
	public GameResponseSender()
	{
		ioSessions = new ArrayList<IoSession>();
	}

	public void send(Response response)
	{
		
		IntermediateMessage message = (IntermediateMessage)messageCodec.encodeResponse(response);
		for(IoSession ioSession : ioSessions)
		{
			ioSession.write(message);
		}
		logger.debug("发送消息" + response.getOpcode() + "给" + (response.getScope() == ResponseScope.SELF ? response.getSession().getRole() : response.getRecievers()));
	}
}
