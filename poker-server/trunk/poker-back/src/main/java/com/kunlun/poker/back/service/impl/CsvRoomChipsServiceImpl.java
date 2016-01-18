package com.kunlun.poker.back.service.impl;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.kunlun.poker.Config;
import com.kunlun.poker.back.domain.AbstactLog;
import com.kunlun.poker.back.domain.CsvRoomChipsLog;
import com.kunlun.poker.back.service.CsvRoomChipsService;
import com.kunlun.poker.back.system.Constants;
import com.kunlun.poker.back.system.FileUtil;
import com.kunlun.poker.back.system.PageUtil;
import com.kunlun.poker.log.LogConstants;
/**
 * 
 * @author zern
 *
 */
@Service("csvRoomChipsService")
public class CsvRoomChipsServiceImpl implements CsvRoomChipsService {
    private static final SimpleDateFormat DATAFORMAT = new SimpleDateFormat(
            "yyyy_MM_dd");
    private static final int CLOUMN_COUNT = 4;
    private static final int LINE_BYTE_LENGTH = 2 + 1 + 20 + 1 + 10 + 1 + 10 + System.getProperty("line.separator").getBytes().length;
    private int startTime;
    private int endTime;
    private Map<Long, CsvRoomChipsLog> dataMap;
    
    @Override
    public  Map<Long, CsvRoomChipsLog> obtainLogDataInFile(int startTime, int endTime, 
    		int pageSize, int pageIndex) throws Exception{
        if(this.startTime == 0 && this.endTime == 0 && dataMap == null){
            this.startTime = startTime;
            this.endTime = endTime;
         }else if(this.startTime != startTime && this.endTime != endTime && dataMap != null){
            this.startTime = startTime;
            this.endTime = endTime;
            this.dataMap = null;
        }else if(this.startTime == startTime && this.endTime == endTime && this.dataMap != null){
            /** 支持分页*/
            return PageUtil.getPageMap(dataMap, new TreeMap<Long, CsvRoomChipsLog>(), pageSize, pageIndex);
        }
        
        int diffValue = (int) (TimeUnit.SECONDS.toDays(endTime) - TimeUnit.SECONDS.toDays(startTime));
        String startTimeStr = DATAFORMAT.format(startTime*1000l);
        
//        Config.getInstance().getLogDirName()
        String startTimePath = String.format("%s%s_%s", "D:\\workcode\\poker-server\\trunk\\poker-log\\log" + File.separator,
                LogConstants.TL_CSV_ROOM_CHIPS_STATIC, startTimeStr+".csv");
        
        if(diffValue == 0){
            if(FileUtil.isExist(startTimePath)){
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");){
                    FileChannel fileChannel = raf.getChannel();
                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    
                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 8);
                    int endLine = FileUtil.binarySearch(mbb, endTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
                    
                    if(startLine != -1 && endLine != -1){
                        int totalLine = endLine - startLine;
                        if(totalLine > Constants.READ_LINE_COUNT){
                            Map<Long, CsvRoomChipsLog> sourceMap = new TreeMap<Long, CsvRoomChipsLog>();
                            int size = totalLine % Constants.READ_LINE_COUNT==0 ? totalLine/Constants.READ_LINE_COUNT : totalLine/Constants.READ_LINE_COUNT + 1;
                            int tempEndLine = startLine;
                          
                            do {
                            	String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                            	this.merge(sourceMap, this.transform(dataStr)); 
                            	tempEndLine += Constants.READ_LINE_COUNT;
                                if(tempEndLine > size){
                                    tempEndLine = size;
                                    startLine += Constants.READ_LINE_COUNT;
                                    dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                                    this.merge(sourceMap, this.transform(dataStr)); 
                                    break;
                                }
                                startLine += Constants.READ_LINE_COUNT;
							} while (tempEndLine < size);
                            
                            return this.dataMap = sourceMap;
                        }
                        
                        String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine, LINE_BYTE_LENGTH);
                        this.dataMap = this.transform(dataStr);
                        return PageUtil.getPageMap(dataMap, new TreeMap<Long, CsvRoomChipsLog>(), pageSize, pageIndex);
                    }
                }
                
            }
        }else if(diffValue > 0){
            Map<Long, CsvRoomChipsLog> sourceMap = new TreeMap<Long, CsvRoomChipsLog>();
            for(int idx=0; idx<diffValue; idx++){
                if(!FileUtil.isExist(startTimePath)){
                    break;
                }
                try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");) {
                    FileChannel fileChannel = raf.getChannel();
                    MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());

                    int fileLineCount = (int) (raf.length()/LINE_BYTE_LENGTH);
                    
                    int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH,8);
                    int endLine = FileUtil.binarySearch(mbb, FileUtil.getMiddleTime(mbb, fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH), fileLineCount, CLOUMN_COUNT, LINE_BYTE_LENGTH, 2);
                    
                    if(startLine != -1 && endLine != -1){
                        int totalLine = endLine - startLine;
                        if(totalLine > Constants.READ_LINE_COUNT){
                            Map<Long, CsvRoomChipsLog> oneHSourceMap = new TreeMap<Long, CsvRoomChipsLog>();
                            int size = totalLine % Constants.READ_LINE_COUNT==0 ? totalLine/Constants.READ_LINE_COUNT : totalLine/Constants.READ_LINE_COUNT + 1;
                            int tempEndLine = startLine;
                            do {
                            	String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                            	this.merge(sourceMap, this.transform(dataStr)); 
                            	tempEndLine += Constants.READ_LINE_COUNT;
                                if(tempEndLine > size){
                                    tempEndLine = size;
                                    startLine += Constants.READ_LINE_COUNT;
                                    dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, LINE_BYTE_LENGTH);
                                    this.merge(sourceMap, this.transform(dataStr)); 
                                    break;
                                }
                                startLine += Constants.READ_LINE_COUNT;
							} while (tempEndLine < size);
                            this.merge(sourceMap, oneHSourceMap);
                        }
                        String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine, LINE_BYTE_LENGTH);
                        this.merge(sourceMap, this.transform(dataStr));
                    }
                }

                startTime = startTime + 86400;
                startTimeStr = DATAFORMAT.format(startTime);
                startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                        LogConstants.TL_CSV_ROOM_CHIPS_STATIC, startTimeStr+".csv");
            }
            this.dataMap = sourceMap;
            return PageUtil.getPageMap(dataMap, new TreeMap<Long, CsvRoomChipsLog>(), pageSize, pageIndex);
            
        }

        return null;
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public <E extends Number,T extends AbstactLog<E>> Map<E, T> transform(String str){
        Map<Long, CsvRoomChipsLog> map = new TreeMap<Long, CsvRoomChipsLog>();
        String[] arr = str.split("\n");
        for(String o : arr){
            String[] log = o.split(",");
            int roomId = Integer.valueOf(log[0].trim());
            long totalChips = Long.parseLong(log[1].trim());
            int averageChips = Integer.valueOf(log[2].trim());
            int time = Integer.valueOf(log[3].trim().replace("\r", ""));
            
        	String logIdString = roomId + "" + time +"";
        	long logId = Long.parseLong(logIdString);
        	System.out.println(logId);
            
            CsvRoomChipsLog logModel = map.get(logId);
            if(logModel == null){
                logModel = new CsvRoomChipsLog();
            }
            logModel.setId(logId);
            logModel.setTotalChips(totalChips);
            logModel.setAverageChips(averageChips);
            logModel.setTime(time);
            map.put(logId, logModel);
        }
        return (Map<E, T>) map;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public <E extends Number, T extends AbstactLog<E>> Map<E, T> merge(Map<E, T> sMap, Map<E, T> tMap){
		Map<Long, CsvRoomChipsLog> sourceMap = (Map<Long, CsvRoomChipsLog>) sMap;
        Map<Long, CsvRoomChipsLog> targetMap = (Map<Long, CsvRoomChipsLog>) tMap;
        for(Map.Entry<Long, CsvRoomChipsLog> entry : targetMap.entrySet()){
            Long Id = entry.getKey();
            CsvRoomChipsLog sourceLog = sourceMap.get(Id);
            CsvRoomChipsLog targetLog = entry.getValue();
            
            if(sourceLog == null){
                sourceLog = new CsvRoomChipsLog();
                sourceMap.put(Id, sourceLog);
            }
            System.out.println(targetLog);
            sourceLog.merge(targetLog);
        }
        return sMap;
    }
    
    
 	  //1410768588
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile(
                "D:\\workcode\\poker-server\\trunk\\poker-log\\log\\tl_csv_room_chips_statics_2014_10_09.csv",
                "r");
        
        String str = "2014-10-09";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        int millionSeconds = (int) (sdf.parse(str).getTime()/1000);
        long along = (long)millionSeconds * 1000;
        
        FileChannel fileChannel = file.getChannel();
        MappedByteBuffer mbb = fileChannel.map(MapMode.READ_ONLY, 0, fileChannel.size());
        int lineCount = ((int) file.length()) / 47;
        int startLine = FileUtil.binarySearch(mbb, 1412006401, lineCount, CLOUMN_COUNT, 47, 8);
        int endLine = FileUtil.binarySearch(mbb, 1412006413, lineCount, CLOUMN_COUNT, 47, 2);
        CsvRoomChipsServiceImpl csv = new CsvRoomChipsServiceImpl();
        Map<Long, CsvRoomChipsLog> getCsv = csv.obtainLogDataInFile(1412784000, 1412784023, 10, 1);
        System.out.println(getCsv);
        file.close();
    }
    
    @Override
    public int getTotalSize() {
        if(this.dataMap != null){
            return this.dataMap.size();
        }
        return 0;
    }

	@Override
	public boolean isReading() {
		return false;
	}

	@Override
	public int getCurrentLine() {
		return 0;
	}

	@Override
	public int getTotalLine() {
		return 0;
	}

	@Override
	public void setCurrentLine(int currentLine) {
	
		
	}

	@Override
	public void clear() {
		
	}
}
