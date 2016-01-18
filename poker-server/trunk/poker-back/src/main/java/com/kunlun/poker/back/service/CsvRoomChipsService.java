package com.kunlun.poker.back.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.kunlun.poker.back.domain.CsvRoomChipsLog;



@Service("csvRoomChipsService")
public interface CsvRoomChipsService extends StatisticsService{
	Map<Long, CsvRoomChipsLog> obtainLogDataInFile(int startTime,
			int endTime, int pageSize, int pageIndex) throws Exception;
}
