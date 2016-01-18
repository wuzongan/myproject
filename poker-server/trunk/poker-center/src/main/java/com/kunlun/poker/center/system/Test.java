package com.kunlun.poker.center.system;

import java.io.FileInputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kunlun.poker.Config;
import com.kunlun.poker.center.service.RoomService;

public class Test {
    
    private static final Logger logger = LoggerFactory.getLogger(Test.class);
	public static void main(String[] args) {
        Config config = Config.getInstance();
        try {
            config.load(new FileInputStream("config.properties"));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
	    
		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
		
		RoomService roomService = (RoomService) applicationContext.getBean("roomService");
		roomService.getRoomById(1);
	}
}