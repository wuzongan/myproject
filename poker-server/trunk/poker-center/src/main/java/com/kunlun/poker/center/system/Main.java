package com.kunlun.poker.center.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.remoting.rmi.RmiServiceExporter;

import com.googlecode.canoe.core.server.CanoeServer;
import com.googlecode.canoe.signal.Signal;
import com.googlecode.canoe.signal.SignalMonitor;
import com.kunlun.poker.Config;
import com.kunlun.poker.center.service.AttainmentService;
import com.kunlun.poker.center.service.ConfigDataService;
import com.kunlun.poker.center.service.NoticeService;
import com.kunlun.poker.center.service.RankService;
import com.kunlun.poker.center.service.UserService;
import com.kunlun.poker.util.StringUtils;

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
        
        String rmiHostName = config.getProperty("rmi.server.hostname");
        if(!StringUtils.isEmpty(rmiHostName))
        	System.setProperty("java.rmi.server.hostname", rmiHostName);        

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
		UserService userService = (UserService)applicationContext.getBean("userService");
		userService.mergePlayers();
		
		RankService rankService = (RankService)applicationContext.getBean("rankService");
		rankService.loadAllRank();
		
		ConfigDataService configDataService = (ConfigDataService)applicationContext.getBean("configDataService");
		configDataService.loadAllConfig();
		
		CanoeServer server = (CanoeServer) applicationContext.getBean("server");
		server.start();
		
//		Scheduler scheduler = (Scheduler) applicationContext.getBean("scheduler");
//		RoleManager roleManager = (RoleManager)applicationContext.getBean("sessionRoleManager");
//		LogRecordTimeTask recordTotalBankrollTask = new LogRecordTimeTask();
//		recordTotalBankrollTask.setScheduler(scheduler);
//		recordTotalBankrollTask.setRoleManager(roleManager);
//		scheduler.submit(recordTotalBankrollTask, "logRecord", 0, 5, TimeUnit.MINUTES);
		
		NoticeService noticeService = (NoticeService) applicationContext.getBean("noticeService");
		noticeService.loadNotices(false);
		SignalMonitor signalMonitor = new SignalMonitor();
		signalMonitor.handleSignal(Signal.HUP);
		signalMonitor.handleSignal(Signal.TERM);
		signalMonitor.handleSignal(Signal.INT);
		signalMonitor.addObserver(new SignalObserver(applicationContext));
	}
}

class SignalObserver implements Observer {
	private static final Logger logger = LoggerFactory
			.getLogger(SignalObserver.class);

	private ApplicationContext applicationContext;

	public SignalObserver(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == Signal.TERM || arg == Signal.INT) {
			try {
				logger.debug("正在关闭服务器");
				((RmiServiceExporter)applicationContext.getBean("userRServiceExporter")).destroy();
				((RmiServiceExporter)applicationContext.getBean("gmServiceExporter")).destroy();
				((RmiServiceExporter)applicationContext.getBean("robotRServiceExporter")).destroy();
				((CanoeServer) applicationContext.getBean("server")).close();
				((AttainmentService)applicationContext.getBean("attainmentService")).stop();
				((NoticeService)applicationContext.getBean("noticeService")).stop();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
