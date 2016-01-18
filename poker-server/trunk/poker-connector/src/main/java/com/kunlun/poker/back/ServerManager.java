package com.kunlun.poker.back;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.ServerNode;
import com.kunlun.poker.message.IntermediateMessage;
import com.kunlun.poker.service.ServerNodeService;

public class ServerManager {

	private static final Logger logger = LoggerFactory
			.getLogger(ServerManager.class);
	private static final ServerManager instance = new ServerManager();

	public static ServerManager getInstance() {
		return instance;
	}

	private final Map<Integer, ServerConnector> connectorMap;
	private List<ServerNode> servers;

	public ServerManager() {
		connectorMap = new HashMap<>();
	}

	public void connectAll() {
		List<ServerNode> servers = getAll();

		for (ServerNode server : servers) {
		    if(server.getConnectFlag() == 0){
		        continue;
		    }
			ServerConnector connector = new ServerConnector(server);
			connectorMap.put(server.getId(), connector);
			connector.start();
		}
	}

	public void closeAll() {
		for (ServerConnector connector : connectorMap.values()) {
			connector.interrupt();
		}
	}

	public void sendToAll(IntermediateMessage message) {
		for (ServerConnector connector : connectorMap.values()) {
			connector.sendMessage(message);
		}
	}

	public void sendMessage(IntermediateMessage message) {
		ServerConnector conn = connectorMap.get(message.getServerId());
		if (conn != null) {
			conn.sendMessage(message);
		} else {
			logger.error("无效的服务器Id");
		}
	}

	private List<ServerNode> getAll() {
		if (servers == null) {
			servers = ServerNodeService.getInstance().getAll();
		}

		return servers;
	}
}