package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvSaleChipsLog;

public interface CsvSaleChipsService extends StatisticsService{
	Map<Integer, CsvSaleChipsLog> obtainLogDataInFile(int startTime, 
			int endTime, int pageSize, int pageIndex) throws Exception;
}
