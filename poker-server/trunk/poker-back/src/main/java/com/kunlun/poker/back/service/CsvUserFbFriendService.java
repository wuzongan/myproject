package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvUserFbFriendStatisticsLog;

public interface CsvUserFbFriendService extends StatisticsService{

    public Map<Integer, CsvUserFbFriendStatisticsLog> obtainLogDataInFile(int startTime, int endTime, int pageSize, int pageIndex) throws Exception;
}
