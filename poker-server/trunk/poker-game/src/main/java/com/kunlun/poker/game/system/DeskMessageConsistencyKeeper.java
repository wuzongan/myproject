package com.kunlun.poker.game.system;

import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.message.MessageConsistencyKeeper;

public class DeskMessageConsistencyKeeper implements MessageConsistencyKeeper {

	private SessionRoleManager sessionRoleManager;

	public void setSessionRoleManager(SessionRoleManager sessionRoleManager) {
		this.sessionRoleManager = sessionRoleManager;
	}

	@Override
	public long getConsistencyCode(IntermediateMessage message) {
		int sessionId = message.getSessionIds()[0];
		Session session = sessionRoleManager.getSession(sessionId);

		Player role;
		Desk desk;
		if (session == null || (role = (Player) session.getRole()) == null) {
			return sessionId;
		} else if ((desk = role.getDesk()) == null) {
			return role.getId();
		} else {
			return desk.getId();
		}
	}

}
