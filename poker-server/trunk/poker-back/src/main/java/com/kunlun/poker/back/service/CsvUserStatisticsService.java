package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvUserStatisticsLog;

public interface CsvUserStatisticsService extends StatisticsService{

    public  Map<Integer, CsvUserStatisticsLog> obtainLogDataInFile(int startTime, int endTime, int pageSize, int pageIndex) throws Exception;
    
}
