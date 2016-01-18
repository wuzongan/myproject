package com.kunlun.poker.message;

public interface MessageConsistencyKeeper {
	long getConsistencyCode(IntermediateMessage message);
}
