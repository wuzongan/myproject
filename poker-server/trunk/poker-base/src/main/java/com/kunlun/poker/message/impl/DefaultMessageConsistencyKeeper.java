package com.kunlun.poker.message.impl;

import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.message.MessageConsistencyKeeper;

public class DefaultMessageConsistencyKeeper implements
		MessageConsistencyKeeper {

	@Override
	public long getConsistencyCode(IntermediateMessage message) {
		return message.getSessionIds()[0];
	}

}
