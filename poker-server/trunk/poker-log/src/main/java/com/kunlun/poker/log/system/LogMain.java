package com.kunlun.poker.log.system;

import java.io.FileInputStream;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.service.IoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.canoe.signal.Signal;
import com.googlecode.canoe.signal.SignalMonitor;
import com.kunlun.poker.Config;
import com.kunlun.poker.log.service.LogService;
import com.kunlun.poker.log.system.schedule.CreateLogTask;
import com.kunlun.poker.log.system.schedule.ScheduledExecutor;

public class LogMain {
	private static final Logger logger = LoggerFactory.getLogger(LogMain.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		try {
			long start = System.currentTimeMillis();
			Config config = Config.getInstance();
			config.load(new FileInputStream("config.properties"));

			ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
					"applicationContext.xml");

			ThreadPoolExecutor threadPool = new LogThreadPoolExecutor(
					config.getThreadNum(), config.getThreadNum() * 10);
			IoHandler ioHandler = (IoHandler) applicationContext
					.getBean("ioHandler");
			MinaServer minaServer = (MinaServer) applicationContext
					.getBean("minaServer");
			minaServer.setIoHandler(ioHandler);
			minaServer.setThreadPool(threadPool);
			minaServer.start();

			LogService logService = (LogService) applicationContext
					.getBean("logService");
			logService.createTables();

			ScheduledExecutor.getIntance().schedule(
					new CreateLogTask(logService));

			long end = System.currentTimeMillis();

			SignalMonitor signalMonitor = new SignalMonitor();
			signalMonitor.handleSignal(Signal.HUP);
			signalMonitor.handleSignal(Signal.TERM);
			signalMonitor.handleSignal(Signal.INT);
			signalMonitor.addObserver(new SignalObserver(applicationContext));

			logger.info("log服务器启动成功，启动耗时：" + (end - start) + "毫秒");
		} catch (Exception e) {
			logger.error("", e);
		}
	}
}

class SignalObserver implements Observer {
	private static final Logger logger = LoggerFactory.getLogger(LogMain.class);

	private ApplicationContext applicationContext;

	public SignalObserver(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == Signal.TERM || arg == Signal.INT) {
			try {
				logger.debug("正在关闭日志服务器.");
				((MinaServer) this.applicationContext.getBean("minaServer"))
						.stop();
				((LogService)this.applicationContext.getBean("logService")).stop();
				ScheduledExecutor.getIntance().stop();
			} catch (Throwable e) {
				logger.error(e.getMessage());
			}
		}
	}

}
