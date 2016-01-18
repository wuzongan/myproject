package com.kunlun.poker.rmi;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.kunlun.poker.util.Callback;

public interface RMIExecutor {
	<T> Future<T> execute(Callable<T> task, long rmiConsistencyCode, Callback<T> onSuccess, Callback<Throwable> onFail, long callbackConsistencyCode);
	Future<Void> execute(Runnable task, long rmiConsistencyCode, Runnable onSuccess, Callback<Throwable> onFail, long callbackConsistencyCode);
}
