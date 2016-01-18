package com.kunlun.poker.scheduler;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.googlecode.canoe.scheduler.SchedulerImpl;
import com.googlecode.canoe.scheduler.WorkerGroup;

public class Test {
	public static void main(String[] args) {
		SchedulerImpl scheduler = new SchedulerImpl(
				new WorkerGroup[] { new WorkerGroup(1) });
		scheduler.start();

		try (Scanner sc = new Scanner(System.in)) {
			while (true) {
				System.out.print("> ");
				String[] cmds = sc.nextLine().trim().split("\\s+");
				if(cmds.length == 1)
				{
					scheduler.submit(new Runnable() {
	
						@Override
						public void run() {
							System.out.println("实时: " + cmds[0]);
						}
					}, null, 0);
				}
				else
				{
					scheduler.submit(new Runnable() {
	
						@Override
						public void run() {
							System.out.println("定时: " + cmds[0]);
						}
					}, null, 0, Long.parseLong(cmds[1]), TimeUnit.SECONDS);
				}
			}
		}
	}
}
