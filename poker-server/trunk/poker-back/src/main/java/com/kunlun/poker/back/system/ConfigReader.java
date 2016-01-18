package com.kunlun.poker.back.system;

import java.io.IOException;

import com.kunlun.poker.Config;

public class ConfigReader{
	public ConfigReader(){
		Config config = Config.getInstance();
		try {
			config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
		}
	}
	
	
}
