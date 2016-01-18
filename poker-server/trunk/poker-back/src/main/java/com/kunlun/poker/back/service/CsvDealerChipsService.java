package com.kunlun.poker.back.service;

import java.util.Map;

import com.kunlun.poker.back.domain.CsvDealerChipsLog;

public interface CsvDealerChipsService extends StatisticsService{

	Map<Integer ,CsvDealerChipsLog> obtainLogDataInFile(int startTime, 
			int endTime, int pageSize, int pageIndex) throws Exception;
}
