package com.kunlun.poker.util;

@FunctionalInterface
public interface Callback<T> {
	void invoke(T param);
}
