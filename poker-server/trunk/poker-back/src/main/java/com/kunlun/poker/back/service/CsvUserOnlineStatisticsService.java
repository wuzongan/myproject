package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvUserOnlineStatisticsLog;

public interface CsvUserOnlineStatisticsService extends StatisticsService{

    public Map<Integer, CsvUserOnlineStatisticsLog> obtainLogDataInFile(int specifiedTime, int pageSize, int pageIndex) throws Exception;
}
