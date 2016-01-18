package com.kunlun.poker.recharge.system;

import java.io.IOException;

import com.kunlun.poker.Config;


public class ConfigReader{
	public ConfigReader(){
		Config config = Config.getInstance();
		try {
			config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"));
			System.out.println(config.getProperty("uploadPath"));
		} catch (IOException e) {
		}
	}
}
