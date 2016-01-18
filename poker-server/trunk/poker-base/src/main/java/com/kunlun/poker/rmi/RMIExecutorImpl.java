package com.kunlun.poker.rmi;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.util.Callback;

public class RMIExecutorImpl implements RMIExecutor {
	private static final Logger logger = LoggerFactory
			.getLogger(RMIExecutorImpl.class);
	private static final String WORKGROUP = "rmi";
	private Scheduler scheduler;

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public <T> Future<T> execute(Callable<T> task, long rmiConsistencyCode,
			Callback<T> onSuccess, Callback<Throwable> onFail,
			long callbackConsistencyCode) {
		return scheduler.submit(() -> {
			try {
				T result = task.call();
				if (onSuccess != null) {
					scheduler.submit(() -> {
						onSuccess.invoke(result);
					}, null, callbackConsistencyCode);
				}
				return result;
			} catch (Throwable e) {
				if (onFail != null) {
					scheduler.submit(() -> {
						onFail.invoke(e);
					}, null, callbackConsistencyCode);
				} else {
					logger.error(e.getMessage(), e);
				}
			}

			return null;
		}, WORKGROUP, rmiConsistencyCode);
	}

	@Override
	public Future<Void> execute(Runnable task, long rmiConsistencyCode,
			Runnable onSuccess, Callback<Throwable> onFail,
			long callbackConsistencyCode) {

		return scheduler.submit(() -> {
			try {
				task.run();
				if (onSuccess != null) {
					scheduler.submit(() -> {
						onSuccess.run();
					}, null, callbackConsistencyCode);
				}
			} catch (Throwable e) {
				if (onFail != null) {
					scheduler.submit(() -> {
						onFail.invoke(e);
					}, null, callbackConsistencyCode);
				}
			}
		}, null, WORKGROUP, rmiConsistencyCode);
	}
}
