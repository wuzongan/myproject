package com.kunlun.poker.back.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.kunlun.poker.Config;
import com.kunlun.poker.back.domain.AbstactLog;
import com.kunlun.poker.back.domain.CsvRoomBankrollStatisticsLog;
import com.kunlun.poker.back.service.CsvRoomBankrollService;
import com.kunlun.poker.back.system.Constants;
import com.kunlun.poker.back.system.FileUtil;
import com.kunlun.poker.back.system.PageUtil;
import com.kunlun.poker.back.system.ServiceUtil;
import com.kunlun.poker.log.LogConstants;

@Service("csvRoomBankrollService")
public class CsvRoomBankrollServiceImpl implements CsvRoomBankrollService {
    private static final int CLOUMN_COUNT = 4;
    private static final int LINE_BYTE_LENGTH = 2 + 1 + 10 + 1 + 10 + 1 + 10 + Constants.LINE_SEPARATOR_LENGTH;
    
    private int startTime;
    private int endTime;
    private volatile Map<Integer, CsvRoomBankrollStatisticsLog> dataMap;
    private volatile boolean reading;
    private volatile int currentLine;
    private int totalLine;
    
    public Map<Integer, CsvRoomBankrollStatisticsLog> obtainLogDataInFile(int startTime, int endTime, int pageSize, int pageIndex) throws Exception{
        reading = true;
        if(this.startTime == 0 && this.endTime == 0 && dataMap == null){
            this.startTime = startTime;
            this.endTime = endTime;
        }else if(this.startTime != startTime && this.endTime != endTime && dataMap != null){
            this.startTime = startTime;
            this.endTime = endTime;
            this.dataMap = null;
        }else if(this.startTime == startTime && this.endTime == endTime && this.dataMap != null){
            Map<Integer, CsvRoomBankrollStatisticsLog> sourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
        }
        int diffValue = (int) (TimeUnit.SECONDS.toDays(endTime) - TimeUnit.SECONDS.toDays(startTime));
        String startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
        String startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                LogConstants.TL_CSV_ROOM_BANKROLL_STATIC, startTimeStr+".csv");
        if(diffValue==0){
             if(FileUtil.isExist(startTimePath)){
                 try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");){
                      FileChannel fileChannel = raf.getChannel();
                      MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                      int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                      this.totalLine = fileLineCount;
                      Map<Integer, CsvRoomBankrollStatisticsLog> sourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
                      this.dataMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 0);
                      this.clear();
                      return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
                 }
             }
//            if(FileUtil.isExist(startTimePath)){
//                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");){
//                    FileChannel fileChannel = raf.getChannel();
//                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
//                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
//                    int endLine = FileUtil.binarySearch(mbb, endTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvRoomBankrollStatisticsLog> sourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
//                            int size = totalLine % Constants.READ_LINE_COUNT==0 ? totalLine/Constants.READ_LINE_COUNT : totalLine/Constants.READ_LINE_COUNT + 1;
//                            int tempEndLine = startLine;
//                            for(int idx=0; idx< size; idx++){
//                                tempEndLine += Constants.READ_LINE_COUNT;
//                                if(tempEndLine > endLine){
//                                    tempEndLine = endLine;
//                                }
//                                String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
//                                startLine += Constants.READ_LINE_COUNT;
//                                this.merge(sourceMap, this.transform(dataStr));
//                            }
//                            return this.dataMap = sourceMap;
//                        }
//                        String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine, LINE_BYTE_LENGTH);
//                        return this.dataMap = this.transform(dataStr);
//                    }
//                }
//            }
        }else{
            this.totalLine = ServiceUtil.getTotalLine(diffValue, startTime, startTimePath, CLOUMN_COUNT, LINE_BYTE_LENGTH);
            Map<Integer, CsvRoomBankrollStatisticsLog> sourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
            for(int idx=0; idx<diffValue; idx++){
                if(!FileUtil.isExist(startTimePath)){
                    break;
                }
                MappedByteBuffer mbb;
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");) {
                    FileChannel fileChannel = raf.getChannel();
                    mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    Map<Integer, CsvRoomBankrollStatisticsLog> targetMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 1);
                    this.merge(sourceMap, targetMap);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH,8);
//                    int endLine = FileUtil.binarySearch(mbb, FileUtil.getMiddleTime(mbb, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH), fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvRoomBankrollStatisticsLog> oneHSourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
//                            int size = totalLine % Constants.READ_LINE_COUNT==0 ? totalLine/Constants.READ_LINE_COUNT : totalLine/Constants.READ_LINE_COUNT + 1;
//                            int tempEndLine = startLine;
//                            for(int jdx=0; jdx< size; jdx++){
//                                tempEndLine += Constants.READ_LINE_COUNT;
//                                if(tempEndLine > endLine){
//                                    tempEndLine = endLine;
//                                }
//                                String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
//                                startLine += Constants.READ_LINE_COUNT;
//                                this.merge(oneHSourceMap, this.transform(dataStr));
//                            }
//                            this.merge(sourceMap, oneHSourceMap);
//                        }
//                        String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine, LINE_BYTE_LENGTH);
//                        this.merge(sourceMap, this.transform(dataStr));
//                    }
                }
                startTime = startTime + 86400;
                startTimeStr = Constants.DATAFORMAT.format(startTime);
                startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                        LogConstants.TL_CSV_USER_STATIC, startTimeStr+".csv");
                //startTime = FileUtil.getMiddleTime(mbb, 1, CLOUMN_COUNT, LINE_BYTE_LENGTH);
            }
            this.dataMap = sourceMap;
            Map<Integer, CsvRoomBankrollStatisticsLog> newSourceMap = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, newSourceMap, pageSize, pageIndex);
        }
        return null;
    }
    
//    @Override
//    private Map<Integer, CsvRoomBankrollStatisticsLog> transform(String str){
//        Map<Integer, CsvRoomBankrollStatisticsLog> map = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
//        String[] arr = str.split("\n");
//        for(String o : arr){
//            String[] log = o.split(",");
//            int roomId = Integer.valueOf(log[0].trim());
//            int totalBankroll = Integer.valueOf(log[1].trim());
//            int serviceBankroll = Integer.valueOf(log[2].trim());
//            CsvRoomBankrollStatisticsLog logModel = map.get(roomId);
//            if(logModel == null){
//                logModel = new CsvRoomBankrollStatisticsLog();
//                map.put(roomId, logModel);
//            }
//            logModel.setTotalBankroll(logModel.getTotalBankroll() + totalBankroll);
//            logModel.setServiceBankroll(logModel.getServiceBankroll() + serviceBankroll);
//        }
//        return map;
//    }
//    
//    @Override
//    public Map<Integer, CsvRoomBankrollStatisticsLog> merge(Map<Integer,CsvRoomBankrollStatisticsLog> sourceMap, Map<Integer, CsvRoomBankrollStatisticsLog> targetMap){
//        for(Map.Entry<Integer, CsvRoomBankrollStatisticsLog> entry : targetMap.entrySet()){
//            int roomId = entry.getKey();
//            CsvRoomBankrollStatisticsLog sourceLog = sourceMap.get(roomId);
//            CsvRoomBankrollStatisticsLog targetLog = entry.getValue();
//            if(sourceLog == null){
//                sourceLog = new CsvRoomBankrollStatisticsLog();
//                sourceMap.put(roomId, sourceLog);
//            }
//            sourceLog.merge(targetLog);
//
//        }
//        return sourceMap;
//    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> transform(String str) {
      Map<Integer, CsvRoomBankrollStatisticsLog> map = new TreeMap<Integer, CsvRoomBankrollStatisticsLog>();
      String[] arr = str.split("\n");
      for(String o : arr){
          String[] log = o.split(",");
          int roomId = Integer.valueOf(log[0].trim());
          int totalBankroll = Integer.valueOf(log[1].trim());
          int serviceBankroll = Integer.valueOf(log[2].trim());
          CsvRoomBankrollStatisticsLog logModel = map.get(roomId);
          if(logModel == null){
              logModel = new CsvRoomBankrollStatisticsLog();
              map.put(roomId, logModel);
          }
          logModel.setId(roomId);
          logModel.setTotalBankroll(logModel.getTotalBankroll() + totalBankroll);
          logModel.setServiceBankroll(logModel.getServiceBankroll() + serviceBankroll);
      }
      return (Map<E, T>) map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> merge(Map<E, T> sourceMap,
            Map<E, T> targetMap) {
        if(targetMap == null){
            return sourceMap;
        }
        Map<Integer, CsvRoomBankrollStatisticsLog> newTargetMap=(Map<Integer, CsvRoomBankrollStatisticsLog>) targetMap;
        Map<Integer, CsvRoomBankrollStatisticsLog> newSourceMap = (Map<Integer, CsvRoomBankrollStatisticsLog>) sourceMap;
        for(Map.Entry<Integer, CsvRoomBankrollStatisticsLog> entry : newTargetMap.entrySet()){
              int roomId = entry.getKey();
              CsvRoomBankrollStatisticsLog sourceLog = newSourceMap.get(roomId);
              CsvRoomBankrollStatisticsLog targetLog = entry.getValue();
              if(sourceLog == null){
                  sourceLog = new CsvRoomBankrollStatisticsLog();
                  newSourceMap.put(roomId, sourceLog);
              }
              sourceLog.merge(targetLog);
        
          }
          return (Map<E, T>) newSourceMap;
    }

    @Override
    public boolean isReading() {
        return reading;
    }

    @Override
    public int getCurrentLine() {
        return currentLine;
    }

    @Override
    public int getTotalLine() {
        return totalLine;
    }

    @Override
    public void setCurrentLine(int currentLine) {
        this.currentLine = currentLine;
    }

    @Override
    public void clear() {
        this.currentLine = 0;
        this.totalLine = 0;
        this.reading = false;
    }
    
    @Override
    public int getTotalSize() {
        if(this.dataMap != null){
            return this.dataMap.size();
        }
        return 0;
    }

    
}
