package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvAchieveFreeChipsLog;

public interface CsvAchieveFreeChipsService extends StatisticsService{
	Map<Integer, CsvAchieveFreeChipsLog> obtainLogDataInFile(int startTime,
			int endTime, int pageSize, int pageIndex) throws Exception;
}
