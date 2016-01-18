package com.kunlun.poker.login.system;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.googlecode.canoe.core.server.CanoeServer;
import com.googlecode.canoe.signal.Signal;
import com.googlecode.canoe.signal.SignalMonitor;
import com.kunlun.poker.Config;

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
		 
		CanoeServer server = (CanoeServer) applicationContext.getBean("server");
		server.start();

		SignalMonitor signalMonitor = new SignalMonitor();
		signalMonitor.handleSignal(Signal.HUP);
		signalMonitor.handleSignal(Signal.TERM);
		signalMonitor.handleSignal(Signal.INT);
		signalMonitor.addObserver(new SignalObserver(server));
	}
}

class SignalObserver implements Observer {
	private static final Logger logger = LoggerFactory
			.getLogger(SignalObserver.class);

	private CanoeServer server;

        public SignalObserver(CanoeServer server) {
		this.server = server;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == Signal.TERM || arg == Signal.INT) {
			try {
				logger.debug("正在关闭服务器");
				server.close();
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
