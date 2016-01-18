package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvRoomBankrollStatisticsLog;

public interface CsvRoomBankrollService extends StatisticsService{

    public Map<Integer, CsvRoomBankrollStatisticsLog> obtainLogDataInFile(int startTime, 
    		int endTime, int pageSize, int pageIndex) throws Exception;
}
