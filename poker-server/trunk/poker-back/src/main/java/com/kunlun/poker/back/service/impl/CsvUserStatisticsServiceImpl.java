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
import com.kunlun.poker.back.domain.CsvUserStatisticsLog;
import com.kunlun.poker.back.service.CsvUserStatisticsService;
import com.kunlun.poker.back.system.Constants;
import com.kunlun.poker.back.system.FileUtil;
import com.kunlun.poker.back.system.PageUtil;
import com.kunlun.poker.back.system.ServiceUtil;
import com.kunlun.poker.log.LogConstants;

@Service("csvUserStatisticsService")
public class CsvUserStatisticsServiceImpl implements CsvUserStatisticsService {
    private static final int CLOUMN_COUNT = 4;
    private static final int LINE_BYTE_LENGTH = 2 + 1 + 10 + 1 + 1 + 1 + 10 + Constants.LINE_SEPARATOR_LENGTH;
    
    private int startTime;
    private int endTime;
    private volatile Map<Integer, CsvUserStatisticsLog> dataMap;
    private volatile boolean reading;
    private volatile int currentLine;
    private int totalLine;
    
    public  Map<Integer, CsvUserStatisticsLog> obtainLogDataInFile(int startTime, int endTime, int pageSize, int pageIndex) throws Exception{
        reading = true;
        if(this.startTime == 0 && this.endTime == 0 && dataMap == null){
            this.startTime = startTime;
            this.endTime = endTime;
        }else if(this.startTime != startTime && this.endTime != endTime && dataMap != null){
            this.startTime = startTime;
            this.endTime = endTime;
            this.dataMap = null;
        }else if(this.startTime == startTime && this.endTime == endTime && this.dataMap != null){
            Map<Integer, CsvUserStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
        }
        int diffValue = (int) (TimeUnit.SECONDS.toDays(endTime) - TimeUnit.SECONDS.toDays(startTime));
        String startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
        String startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                LogConstants.TL_CSV_USER_STATIC, startTimeStr+".csv");
        if(diffValue == 0){
            if(FileUtil.isExist(startTimePath)){
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");){
                    FileChannel fileChannel = raf.getChannel();
                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    this.totalLine = fileLineCount;
                    this.dataMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 0);
                    Map<Integer, CsvUserStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
                    this.clear();
                    return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
//                    int endLine = FileUtil.binarySearch(mbb, endTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvUserStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
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
        }else if(diffValue > 0){
            this.totalLine = ServiceUtil.getTotalLine(diffValue, startTime, startTimePath, CLOUMN_COUNT, LINE_BYTE_LENGTH);
            Map<Integer, CsvUserStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
            for(int idx=0; idx<diffValue; idx++){
                if(!FileUtil.isExist(startTimePath)){
                    break;
                }
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");) {
                    FileChannel fileChannel = raf.getChannel();
                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    Map<Integer, CsvUserStatisticsLog> targetMap = ServiceUtil.getDataMap(mbb, fileLineCount, startTimePath, startTime, endTime, CLOUMN_COUNT, LINE_BYTE_LENGTH, this, 1);
                    this.merge(sourceMap, targetMap);
//                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH,8);
//                    int endLine = FileUtil.binarySearch(mbb, FileUtil.getMiddleTime(mbb, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH), fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
//                    if(startLine != -1 && endLine != -1){
//                        int totalLine = endLine - startLine;
//                        if(totalLine > Constants.READ_LINE_COUNT){
//                            Map<Integer, CsvUserStatisticsLog> oneHSourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
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
            Map<Integer, CsvUserStatisticsLog> newSourceMap = new TreeMap<Integer, CsvUserStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, newSourceMap, pageSize, pageIndex);
        }
        return null;
    }
    
//    private Map<Integer, CsvUserStatisticsLog> transform(String str){
//        Map<Integer, CsvUserStatisticsLog> map = new TreeMap<Integer, CsvUserStatisticsLog>();
//        
//        String[] arr = str.split("\n");
//        for(String o : arr){
//            String[] log = o.split(",");
//            int roomId = Integer.valueOf(log[0].trim());
//            int userId = Integer.valueOf(log[1].trim());
//            int selectType = Integer.valueOf(log[2].trim());
//            //int time = Integer.valueOf(log[3].trim().replace("\r", ""));
//            
//            CsvUserStatisticsLog logModel = map.get(roomId);
//            if(logModel == null){
//                logModel = new CsvUserStatisticsLog();
//                map.put(roomId, logModel);
//            }
//            logModel.setId(roomId);
//            logModel.getUserIdSet().add(userId);
//            logModel.setUserFrequencyCount(logModel.getUserFrequencyCount() + 1);
//            if(selectType == 2){
//                logModel.setHandChooseCount(logModel.getHandChooseCount() + 1);
//            }else if(selectType == 1){
//                logModel.setQuickStartCount(logModel.getQuickStartCount() + 1);
//            }
//            logModel.getQuickStartUserIdSet().add(userId);
//            
//        }
//        return map;
//    }
//    
//    private Map<Integer, CsvUserStatisticsLog> merge(Map<Integer, CsvUserStatisticsLog> sourceMap, Map<Integer, CsvUserStatisticsLog> targetMap){
//        for(Map.Entry<Integer, CsvUserStatisticsLog> entry : targetMap.entrySet()){
//            int roomId = entry.getKey();
//           CsvUserStatisticsLog sourceLog = sourceMap.get(roomId);
//           CsvUserStatisticsLog targetLog = entry.getValue();
//            if(sourceLog == null){
//                sourceLog = new CsvUserStatisticsLog();
//                sourceMap.put(roomId, sourceLog);
//            }
//            sourceLog.merge(targetLog);
//        }
//        return sourceMap;
//    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> transform(String str) {
        Map<Integer, CsvUserStatisticsLog> map = new TreeMap<Integer, CsvUserStatisticsLog>();
        String[] arr = str.split("\n");
        for(String o : arr){
            String[] log = o.split(",");
            int roomId = Integer.valueOf(log[0].trim());
            int userId = Integer.valueOf(log[1].trim());
            int selectType = Integer.valueOf(log[2].trim());
            //int time = Integer.valueOf(log[3].trim().replace("\r", ""));
            CsvUserStatisticsLog logModel = map.get(roomId);
            if(logModel == null){
                logModel = new CsvUserStatisticsLog();
                map.put(roomId, logModel);
            }
            logModel.setId(roomId);
            logModel.getUserIdSet().add(userId);
            logModel.setUserFrequencyCount(logModel.getUserFrequencyCount() + 1);
            if(selectType == 2){
                logModel.setHandChooseCount(logModel.getHandChooseCount() + 1);
            }else if(selectType == 1){
                logModel.setQuickStartCount(logModel.getQuickStartCount() + 1);
            }
            logModel.getQuickStartUserIdSet().add(userId);
        }
        return (Map<E, T>) map;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> merge(
            Map<E, T> sourceMap, Map<E, T> map) {
        if(map == null){
            return null;
        }
        Map<Integer, CsvUserStatisticsLog> newTargetMap = (Map<Integer, CsvUserStatisticsLog>) map;
        Map<Integer, CsvUserStatisticsLog> newSourceMap = (Map<Integer, CsvUserStatisticsLog>) sourceMap;
        for(Map.Entry<Integer, CsvUserStatisticsLog> entry : newTargetMap.entrySet()){
            int roomId = entry.getKey();
           CsvUserStatisticsLog sourceLog = newSourceMap.get(roomId);
           CsvUserStatisticsLog targetLog = entry.getValue();
            if(sourceLog == null){
                sourceLog = new CsvUserStatisticsLog();
                newSourceMap.put(roomId, sourceLog);
            }
            sourceLog.merge(targetLog);
        }
        return sourceMap;
    }
    
    
    // 1410768588
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile(
                "D:\\pok\\poker-server\\trunk\\poker-log\\log\\tl_csv_user_statics_2014_09_15.csv",
                "r");
        
        FileChannel fileChannel = file.getChannel();
        MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
        int lineCount = ((int) file.length()) / LINE_BYTE_LENGTH;
        System.out.println("行数：" + lineCount);

        // file.seek(lineCount/2 * BYTE_LENGTH);
        // byte[] bytes = new byte[BYTE_LENGTH*4];
        // file.read(bytes, 0, BYTE_LENGTH*4);
        // String str = new String(bytes);
        // System.out.println(str);
        // System.out.println(getMiddleTime(file, lineCount-1, CLOUMN_COUNT,
        // BYTE_LENGTH));

//        int lineIndex = binarySearch(file, 1410768588, lineCount);
//        System.out.println(lineIndex);
//        System.out.println(obtainStringInRandomAccessFile(file, lineIndex,
//                lineIndex + 1, LINE_BYTE_LENGTH, 0, 0));
        
        int startLine = FileUtil.binarySearch(mbb, 1410768930, lineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
        int endLine = FileUtil.binarySearch(mbb, 1410769002, lineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
        
//        int startLine = FileUtil.binarySearch(mbb, 1410768588, lineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
//        int endLine = FileUtil.binarySearch(mbb, 1410768588, lineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);

//        
        String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine + 1, LINE_BYTE_LENGTH);
        
        System.out.println(dataStr);
//
        file.close();
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
