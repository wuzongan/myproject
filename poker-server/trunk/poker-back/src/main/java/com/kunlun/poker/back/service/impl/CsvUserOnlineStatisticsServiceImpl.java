package com.kunlun.poker.back.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;

import com.kunlun.poker.Config;
import com.kunlun.poker.back.domain.AbstactLog;
import com.kunlun.poker.back.domain.CsvUserOnlineStatisticsLog;
import com.kunlun.poker.back.service.CsvUserOnlineStatisticsService;
import com.kunlun.poker.back.system.PageUtil;
import com.kunlun.poker.back.system.Constants;
import com.kunlun.poker.back.system.FileUtil;
import com.kunlun.poker.log.LogConstants;

/***
 * 房间在线人数
 * @author ljx
 */
@Service("csvUserOnlineStatisticsService")
public class CsvUserOnlineStatisticsServiceImpl implements CsvUserOnlineStatisticsService {
    //private static final int CLOUMN_COUNT = 5;
    private static final int LINE_BYTE_LENGTH = 2 + 1 + 10 + 1 + 10 + 1 + 10+ 1 +  10 + Constants.LINE_SEPARATOR_LENGTH;

    private int specifiedTime;
    private volatile Map<Integer, CsvUserOnlineStatisticsLog> dataMap;
    private volatile boolean reading = false;
    private volatile int currentLine = 0;
    private int totalLine = 0;
    
    public Map<Integer, CsvUserOnlineStatisticsLog> obtainLogDataInFile(int specifiedTime, int pageSize, int pageIndex) throws Exception{
        reading = true;
        if(this.specifiedTime == 0 && dataMap == null){
            this.specifiedTime = specifiedTime;
        }else if(this.specifiedTime != specifiedTime && this.dataMap != null){
            this.specifiedTime = specifiedTime;
            this.dataMap = null;
        }else if(this.specifiedTime == specifiedTime && this.dataMap != null){
            Map<Integer,CsvUserOnlineStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserOnlineStatisticsLog>();
            this.clear();
            return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
        }
        
        String specifiedTimeStr = Constants.DATAFORMAT.format(specifiedTime*1000l);
        String specifiedTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                LogConstants.TL_CSV_GAMETYPE_USER_STATIC, specifiedTimeStr+".csv");
        
        if(!FileUtil.isExist(specifiedTimePath)){
            this.clear();
            return null;
        }
        
        try(RandomAccessFile raf = new RandomAccessFile(specifiedTimePath, "r");){
            FileChannel fileChannel = raf.getChannel();
            MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
            int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
            this.totalLine = fileLineCount;
            if(fileLineCount > Constants.READ_LINE_COUNT){
                Map<Integer, CsvUserOnlineStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserOnlineStatisticsLog>();
                int size = fileLineCount/Constants.READ_LINE_COUNT;
                int startLine = 1;
                int tempEndLine = startLine;
                
                do {
                    String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                    this.merge(sourceMap, this.transform(dataStr, specifiedTime));
                    tempEndLine += Constants.READ_LINE_COUNT;
                    this.setCurrentLine(currentLine);
                    if(tempEndLine > size){
                        tempEndLine = size;
                        this.setCurrentLine(currentLine);
                        startLine += Constants.READ_LINE_COUNT;
                        dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                        this.merge(sourceMap, this.transform(dataStr, specifiedTime));
                        break;
                    }
                    startLine += Constants.READ_LINE_COUNT;
                } while (tempEndLine < size);
                this.dataMap = sourceMap;
                return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
            }
            String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, 1, fileLineCount, LINE_BYTE_LENGTH);
            this.dataMap = this.transform(dataStr, specifiedTime);
            Map<Integer,CsvUserOnlineStatisticsLog> sourceMap = new TreeMap<Integer, CsvUserOnlineStatisticsLog>();
            return PageUtil.getPageMap(dataMap, sourceMap, pageSize, pageIndex);
        }
    }
    
    @SuppressWarnings("unused")
    private Map<Integer, CsvUserOnlineStatisticsLog> transform(String str, int specifiedTime){
        Map<Integer, CsvUserOnlineStatisticsLog> map = new TreeMap<Integer, CsvUserOnlineStatisticsLog>();
        String[] arr = str.split("\n");
        for(String o : arr){
            String[] log = o.split(",");
            int roomId = Integer.valueOf(log[0].trim());
            int userId = Integer.valueOf(log[1].trim());
            int bankroll = Integer.valueOf(log[2].trim());
            int startTime = Integer.valueOf(log[3].trim());
            String temp;
            int endTime = Integer.valueOf(temp = log[4].trim().replace(Constants.LINE_SEPARATOR, ""));
            
            if(specifiedTime >= startTime && specifiedTime <= endTime){
                CsvUserOnlineStatisticsLog clog = map.get(roomId);
                if(clog==null){
                    clog = new CsvUserOnlineStatisticsLog();
                    map.put(roomId, clog);
                }
                
                boolean flag = clog.getUserIdSet().add(userId);
                if(flag){
                    clog.setId(roomId);
                    clog.setTotalBankroll(clog.getTotalBankroll() + bankroll);
                }
            }
        }
        return map;
    }
    
//    public Map<Integer, CsvUserOnlineStatisticsLog> merge(Map<Integer, CsvUserOnlineStatisticsLog> sourceMap, Map<Integer, CsvUserOnlineStatisticsLog> targetMap){
//        for(Map.Entry<Integer, CsvUserOnlineStatisticsLog> entry : targetMap.entrySet()){
//            int roomId = entry.getKey();
//            CsvUserOnlineStatisticsLog sourceLog = sourceMap.get(roomId);
//            CsvUserOnlineStatisticsLog targetLog = entry.getValue();
//            if(sourceLog == null){
//                sourceLog = new CsvUserOnlineStatisticsLog();
//                sourceMap.put(roomId, sourceLog);
//            }
//            sourceLog.merge(targetLog);
//        }
//        return sourceMap;
//    }

    @Override
    public <E extends Number,T extends AbstactLog<E>> Map<E, T> transform(String str) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> merge(
            Map<E, T> sourceMap, Map<E, T> map) {
        Map<Integer, CsvUserOnlineStatisticsLog> newSourceMap = (Map<Integer, CsvUserOnlineStatisticsLog>) sourceMap;
        Map<Integer, CsvUserOnlineStatisticsLog> targetMap = (Map<Integer, CsvUserOnlineStatisticsLog>) map;
        for(Map.Entry<Integer, CsvUserOnlineStatisticsLog> entry : targetMap.entrySet()){
            int roomId = entry.getKey();
            CsvUserOnlineStatisticsLog sourceLog = newSourceMap.get(roomId);
            CsvUserOnlineStatisticsLog targetLog = entry.getValue();
            if(sourceLog == null){
                sourceLog = new CsvUserOnlineStatisticsLog();
                newSourceMap.put(roomId, sourceLog);
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
        this.currentLine = currentLine;
    }

    @Override
    public void clear() {
        this.reading = false;
        this.currentLine = 0;
    }

    @Override
    public int getTotalSize() {
        if(this.dataMap != null){
            return this.dataMap.size();
        }
        return 0;
    }
    
}
