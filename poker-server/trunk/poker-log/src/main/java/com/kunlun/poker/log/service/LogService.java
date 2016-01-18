package com.kunlun.poker.log.service;

import java.util.Map;

public interface LogService {

    void createTables() throws Exception;
    
    void doLog(Map<String,String> map);
    
    void stop();
}
