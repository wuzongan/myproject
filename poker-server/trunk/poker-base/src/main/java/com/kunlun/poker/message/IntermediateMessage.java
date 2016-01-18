package com.kunlun.poker.message;

import java.net.InetSocketAddress;

public class IntermediateMessage {
	private int serverId;
	private int[] sessionIds;
	private InetSocketAddress source;
	private byte[] body;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	public int[] getSessionIds() {
		return sessionIds;
	}

	public void setSessionIds(int[] sessionIds) {
		this.sessionIds = sessionIds;
	}

	public InetSocketAddress getSource() {
		return source;
	}

	public void setSource(InetSocketAddress source) {
		this.source = source;
	}

}
