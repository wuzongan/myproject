package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvLevelUpFreeChipsLog;

public interface CsvLevelUpFreeChipsService extends StatisticsService{

	Map<Integer, CsvLevelUpFreeChipsLog> obtainLogDataInFile(int startTime,
			int endTime, int pageSize, int pageIndex) throws Exception;

}
