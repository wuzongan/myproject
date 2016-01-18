package com.kunlun.poker.back.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kunlun.poker.back.domain.CsvLoginFreeChipsLog;

@Service("csvLoginFreeChipsService")
public interface CsvLoginFreeChipsService extends StatisticsService{
	Map<Integer, CsvLoginFreeChipsLog> obtainLogDataInFile(int startTime,
			int endTime, int pageSize, int pageIndex) throws Exception;

}
