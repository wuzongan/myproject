package com.kunlun.poker.recharge.service.impl;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.springframework.stereotype.Service;

import com.googlecode.canoe.fcgi.FCGIConnection;
import com.kunlun.poker.Config;
import com.kunlun.poker.recharge.service.FCGIService;

@Service("fcgiService")
public class FCGIServiceImpl implements FCGIService{

	@Override
	public boolean validateToken(String token){
		try(FCGIConnection connection = FCGIConnection.open())
        {
			String ipAddress = Config.getInstance().getProperty("rmi.FCGI.IP");
			String port = Config.getInstance().getProperty("rmi.FCGI.port");
			String fileName = Config.getInstance().getProperty("rmi.FCGI.fileName");
			String filePath = Config.getInstance().getProperty("rmi.FCGI.filePath");
			
            connection.connect(new InetSocketAddress(ipAddress, Integer.parseInt(port)));
            connection.beginRequest(fileName);
            connection.setRequestMethod("GET");
            connection.setQueryString(token);
            connection.addParams("DOCUMENT_ROOT", filePath);
    
            ByteBuffer buffer = ByteBuffer.allocate(1);
            connection.read(buffer);
            buffer.flip(); 
            return buffer.get() == '1';
        } catch (IOException e) {
        	return false;
		}
	}
}
