package com.kunlun.poker.game.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.canoe.core.server.CanoeServer;
import com.googlecode.canoe.scheduler.Scheduler;
import com.googlecode.canoe.signal.Signal;
import com.googlecode.canoe.signal.SignalMonitor;
import com.kunlun.poker.Config;
import com.kunlun.poker.game.service.DeskService;
import com.kunlun.poker.game.service.RoomService;

public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		Config config = Config.getInstance();
		try {
			config.load(new FileInputStream("config.properties"));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");

		RoomService roomService = (RoomService) applicationContext
				.getBean("roomService");
		roomService.initRoom();

		CanoeServer server = (CanoeServer) applicationContext.getBean("server");
		server.start();

		SignalMonitor signalMonitor = new SignalMonitor();
		signalMonitor.handleSignal(Signal.HUP);
		signalMonitor.handleSignal(Signal.TERM);
		signalMonitor.handleSignal(Signal.INT);
		signalMonitor.addObserver(new SignalObserver(server,
				(DeskService) applicationContext.getBean("deskService")));

		Scheduler scheduler = (Scheduler) applicationContext
				.getBean("scheduler");
		scheduler.submit(
				(Runnable) applicationContext.getBean("logRecordTimeTask"),
				"logRecord", 0, 5, TimeUnit.MINUTES);
		
		//TODO 机器人压测程序
//		RobotTestProcessor robotProcessor = new RobotTestProcessor();
//		robotProcessor.setDeskService((DeskService) applicationContext.getBean("deskService"));
//		robotProcessor.setRobotService((RobotService) applicationContext.getBean("robotService"));
//		robotProcessor.setRoomService((RoomService) applicationContext.getBean("roomService"));
//		robotProcessor.init();
	}
}

class SignalObserver implements Observer {
	private static final Logger logger = LoggerFactory
			.getLogger(SignalObserver.class);

	private CanoeServer server;
	private DeskService deskService;

	public SignalObserver(CanoeServer server, DeskService deskService) {
		this.server = server;
		this.deskService = deskService;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == Signal.TERM || arg == Signal.INT) {
			try {
				logger.debug("正在关闭服务器");
				server.close();
				deskService.disposeAll();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
