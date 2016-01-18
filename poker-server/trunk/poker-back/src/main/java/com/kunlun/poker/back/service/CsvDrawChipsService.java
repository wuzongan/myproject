package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvDrawChipsLog;

public interface CsvDrawChipsService extends StatisticsService{

	Map<Integer, CsvDrawChipsLog> obtainLogDataInFile(int startTime, 
			int endTime, int pageSize, int pageIndex) throws Exception;
}
