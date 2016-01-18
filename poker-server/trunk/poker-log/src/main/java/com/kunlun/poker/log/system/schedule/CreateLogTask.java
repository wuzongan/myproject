package com.kunlun.poker.log.system.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.kunlun.poker.log.service.LogService;

public class CreateLogTask implements Runnable {
    private static final Logger logger = LoggerFactory
            .getLogger(CreateLogTask.class);
    private LogService logService;
    public CreateLogTask(LogService logService) {
        this.logService = logService;
    }
    
    @Override
    public void run() {
        try {
            logService.createTables();
        } catch (Throwable e){
            logger.error("createLogTask.run", e);
        }
    }

}
