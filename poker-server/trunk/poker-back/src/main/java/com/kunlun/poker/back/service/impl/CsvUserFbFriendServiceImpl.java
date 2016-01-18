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
import com.kunlun.poker.back.domain.CsvUserFbFriendStatisticsLog;
import com.kunlun.poker.back.service.CsvUserFbFriendService;
import com.kunlun.poker.back.system.Constants;
import com.kunlun.poker.back.system.FileUtil;
import com.kunlun.poker.back.system.PageUtil;
import com.kunlun.poker.back.system.ServiceUtil;
import com.kunlun.poker.log.LogConstants;

@Service("csvUserFbFriendService")
public class CsvUserFbFriendServiceImpl implements CsvUserFbFriendService {
    private static final int CLOUMN_COUNT = 4;
    private static final int LINE_BYTE_LENGTH = 10 + 1 + 10 + 1 + 10 + 1 + 10 + Constants.LINE_SEPARATOR_LENGTH;

    private int startTime;
    private int endTime;
    private volatile Map<Integer, CsvUserFbFriendStatisticsLog> dataMap;
    private volatile boolean reading = false;
    private volatile int currentLine = 0;
    private int totalLine = 0;
    
    public Map<Integer, CsvUserFbFriendStatisticsLog> obtainLogDataInFile(int startTime, int endTime, int pageSize, int pageIndex) throws Exception{
        reading = true;
        if(this.startTime == 0 && this.endTime == 0 && dataMap == null){
            this.startTime = startTime;
            this.endTime = endTime;
        }else if(this.startTime != startTime && this.endTime != endTime && dataMap != null){
            this.startTime = startTime;
            this.endTime = endTime;
            this.dataMap = null;
        }else if(this.startTime == startTime && this.endTime == endTime && this.dataMap != null){
            Map<Integer, CsvUserFbFriendStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
        }
        int diffValue = (int) (TimeUnit.SECONDS.toDays(endTime) - TimeUnit.SECONDS.toDays(startTime));
        String startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
        String startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                LogConstants.TL_CSV_FB_FRIEND_STATIC, startTimeStr+".csv");
        if(diffValue==0){
            if(FileUtil.isExist(startTimePath)){
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");){
                    FileChannel fileChannel = raf.getChannel();
                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    this.totalLine = fileLineCount;
                    this.dataMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 0);
                    Map<Integer, CsvUserFbFriendStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
                    this.clear();
                    return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
//                    int endLine = FileUtil.binarySearch(mbb, endTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvUserFbFriendStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
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
                    
                }
            }
        }else{
            this.totalLine = ServiceUtil.getTotalLine(diffValue, startTime, startTimePath, CLOUMN_COUNT, LINE_BYTE_LENGTH);
            Map<Integer, CsvUserFbFriendStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
            for(int idx=0; idx<diffValue; idx++){
                if(!FileUtil.isExist(startTimePath)){
                    break;
                }
                MappedByteBuffer mbb;
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");) {
                    FileChannel fileChannel = raf.getChannel();
                    mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    Map<Integer, CsvUserFbFriendStatisticsLog> targetMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 1);
                    this.merge(sourceMap, targetMap);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH,8);
//                    int endLine = FileUtil.binarySearch(mbb, FileUtil.getMiddleTime(mbb, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH), fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvUserFbFriendStatisticsLog> oneHSourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
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

                    startTime = startTime + 86400;
                    startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
                    startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                            LogConstants.TL_CSV_USER_STATIC, startTimeStr+".csv");
                    //startTime = FileUtil.getMiddleTime(mbb, 1, CLOUMN_COUNT, LINE_BYTE_LENGTH);
                }
            }
            this.dataMap = sourceMap;
            Map<Integer, CsvUserFbFriendStatisticsLog> newSourceMap = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, newSourceMap, pageSize, pageIndex);
        }
        return null;
    }
    
//    private Map<Integer, CsvUserFbFriendStatisticsLog> transform(String str){
//        Map<Integer, CsvUserFbFriendStatisticsLog> map = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
//        String[] arr = str.split("\n");
//        for(String o : arr){
//            String[] log = o.split(",");
//            int userId = Integer.valueOf(log[0].trim());
//            int shareFbCount = Integer.valueOf(log[1].trim());
//            int inviteCount = Integer.valueOf(log[2].trim());
//            CsvUserFbFriendStatisticsLog logModel = map.get(userId);
//            if(logModel == null){
//                logModel = new CsvUserFbFriendStatisticsLog();
//                map.put(userId, logModel);
//            }
//            logModel.setId(userId);
//            logModel.setShareFbCount(logModel.getShareFbCount() + shareFbCount);
//            logModel.setInviteFriendCount(logModel.getInviteFriendCount() + inviteCount);
//        }
//        return map;
//    }
//
//    public Map<Integer, CsvUserFbFriendStatisticsLog> merge(Map<Integer,CsvUserFbFriendStatisticsLog> sourceMap, Map<Integer, CsvUserFbFriendStatisticsLog> targetMap){
//        for(Map.Entry<Integer, CsvUserFbFriendStatisticsLog> entry : targetMap.entrySet()){
//            int userId = entry.getKey();
//            CsvUserFbFriendStatisticsLog sourceLog = sourceMap.get(userId);
//            CsvUserFbFriendStatisticsLog targetLog = entry.getValue();
//            if(sourceLog == null){
//                sourceLog = new CsvUserFbFriendStatisticsLog();
//                sourceMap.put(userId, sourceLog);
//            }
//            sourceLog.merge(targetLog);
//
//        }
//        return sourceMap;
//    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> transform(String str) {
        Map<Integer, CsvUserFbFriendStatisticsLog> map = new TreeMap<Integer, CsvUserFbFriendStatisticsLog>();
        String[] arr = str.split("\n");
        for(String o : arr){
            String[] log = o.split(",");
            int userId = Integer.valueOf(log[0].trim());
            int shareFbCount = Integer.valueOf(log[1].trim());
            int inviteCount = Integer.valueOf(log[2].trim());
            CsvUserFbFriendStatisticsLog logModel = map.get(userId);
            if(logModel == null){
                logModel = new CsvUserFbFriendStatisticsLog();
                map.put(userId, logModel);
            }
            logModel.setId(userId);
            logModel.setShareFbCount(logModel.getShareFbCount() + shareFbCount);
            logModel.setInviteFriendCount(logModel.getInviteFriendCount() + inviteCount);
        }
        return (Map<E, T>) map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> merge(
            Map<E, T> sourceMap, Map<E, T> map) {
        if(map==null){
            return null;
        }
        Map<Integer, CsvUserFbFriendStatisticsLog> newSourceMap = (Map<Integer, CsvUserFbFriendStatisticsLog>) sourceMap;
        Map<Integer, CsvUserFbFriendStatisticsLog> newTargetMap = (Map<Integer, CsvUserFbFriendStatisticsLog>) map;
        for(Map.Entry<Integer, CsvUserFbFriendStatisticsLog> entry : newTargetMap.entrySet()){
            int userId = entry.getKey();
            CsvUserFbFriendStatisticsLog sourceLog = newSourceMap.get(userId);
            CsvUserFbFriendStatisticsLog targetLog = entry.getValue();
            if(sourceLog == null){
                sourceLog = new CsvUserFbFriendStatisticsLog();
                newSourceMap.put(userId, sourceLog);
            }
            sourceLog.merge(targetLog);

        }
        return sourceMap;
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
        this.currentLine  = currentLine;
    }

    @Override
    public void clear() {
        this.totalLine = 0;
        this.currentLine = 0;
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
