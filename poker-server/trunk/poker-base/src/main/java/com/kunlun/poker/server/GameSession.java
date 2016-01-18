package com.kunlun.poker.server;

import java.net.SocketAddress;

import com.googlecode.canoe.core.session.support.AbstractSession;

/**
 *
 * @author panzd
 */
public class GameSession extends AbstractSession {
    private int id;
    private SocketAddress remoteAddress;

    public long getId() {
        return id;
    }
    
    public GameSession(int id, SocketAddress remoteAddress)
    {
    	this(id);
        this.remoteAddress = remoteAddress;
    }
    
    public GameSession(int id) {
    	this.id = id;
	}
    
	@Override
	public String toString() {
		return "GameSession[" + getId() + "]";
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setRemoteAddress(SocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}
