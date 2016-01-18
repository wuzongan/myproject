package com.kunlun.poker.front;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.session.IoSession;

public class SessionMap extends ConcurrentHashMap<Integer, IoSession> {
	private static final long serialVersionUID = 1L;
	private static final SessionMap instance = new SessionMap();
	public static SessionMap getInstance()
	{
		return instance;
	}
}
