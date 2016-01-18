package com.kunlun.poker.center.system;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import com.googlecode.canoe.scheduler.Scheduler;

public class SchedulerUtil {
	public static void submitPersistenceTask(Runnable task, Scheduler scheduler, long consistencyCode)
	{
		scheduler.submit(task, null, "persistence", consistencyCode);
	}

	public static void submitRankTask(Runnable task, Scheduler scheduler)
	{
		scheduler.submit(task, null, "rank", 0);
	}
	public static <T> Future<T> submitRankTask(Callable<T> task, Scheduler scheduler)
	{
		return scheduler.submit(task, "rank", 0);
	}
}
