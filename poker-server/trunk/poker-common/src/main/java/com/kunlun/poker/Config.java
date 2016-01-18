package com.kunlun.poker;

import java.util.Properties;

import com.kunlun.poker.util.StringUtils;

public class Config extends Properties{
	private static final long serialVersionUID = 1L;
    
	private static final Config instance = new Config();

	public static Config getInstance() {
		return instance;
	}
    
	public int getServerId()
	{
	    return Integer.parseInt(getProperty("server.id"));
	}
	
	public String getLocker()
	{
		return getProperty("server.locker");
	}
	
	public int getThreadNum(){
	    return Integer.valueOf(getProperty("server.threadNum"));
	}
	
	public String getLogDirName(){
	    return getProperty("log.dir");
	}
	
	public boolean isLogEnabled(){
	    String param = getProperty("log.enabled");
	    if(StringUtils.isEmpty(param)){
	        return false;
	    }
	    return !param.equals("0");
	}
	
}
