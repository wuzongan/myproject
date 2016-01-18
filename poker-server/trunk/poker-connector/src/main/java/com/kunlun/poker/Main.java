package com.kunlun.poker;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Observable;
import java.util.Observer;

import org.apache.log4j.PropertyConfigurator;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.signal.Signal;
import com.googlecode.canoe.signal.SignalMonitor;
import com.kunlun.poker.back.ServerManager;
import com.kunlun.poker.front.FrontCodecFactory;
import com.kunlun.poker.front.FrontIoHandler;
import com.kunlun.poker.util.ServerLocker;

public class Main {
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	private static final int DEFAULT_IDLE_TIME = 30;

	public static void main(String[] args) throws IOException {
		PropertyConfigurator.configure("log4j.properties");
		Config config = Config.getInstance();
		config.load(new FileInputStream("config.properties"));

		ServerManager.getInstance().connectAll();

		String hostname = config.getProperty("server.host");
		int port = Integer.parseInt(config.getProperty("server.port"));

		startServer(hostname, port);
	}

	private static void startServer(String hostname, int port)
			throws IOException {
		if (!ServerLocker.lock(Config.getInstance().getLocker(), port)) {
			logger.error("请确认服务器已经正常关闭");
			return;
		}

		int idelTime;
		try {
			idelTime = Integer.parseInt(Config.getInstance().getProperty(
					"server.idelTime"));
		} catch (Exception e) {
			idelTime = DEFAULT_IDLE_TIME;
		}

		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setReaderIdleTime(idelTime);
		acceptor.setHandler(new FrontIoHandler());

		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
		filterChain.addLast("codec", new ProtocolCodecFilter(
				new FrontCodecFactory()));

		LoggingFilter lf = new LoggingFilter();
		lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
		lf.setMessageSentLogLevel(LogLevel.DEBUG);
		filterChain.addLast("logger", lf);

		InetSocketAddress isa = (hostname == null) ? new InetSocketAddress(port)
				: new InetSocketAddress(hostname, port);

		acceptor.bind(isa);

		SignalMonitor signalMonitor = new SignalMonitor();
		signalMonitor.handleSignal(Signal.HUP);
		signalMonitor.handleSignal(Signal.TERM);
		signalMonitor.handleSignal(Signal.INT);
		signalMonitor.addObserver(new SignalObserver(acceptor));
	}
}

class SignalObserver implements Observer {
	private static final Logger logger = LoggerFactory
			.getLogger(SignalObserver.class);

	private IoAcceptor acceptor;

	public SignalObserver(IoAcceptor acceptor) {
		this.acceptor = acceptor;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg == Signal.TERM || arg == Signal.INT) {
			logger.info("正在关闭服务器,请稍侯...");
			ServerManager.getInstance().closeAll();
			acceptor.unbind();
			;
			acceptor.dispose();
		}
	}
}
