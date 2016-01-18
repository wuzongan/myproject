package com.kunlun.poker.back.system;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.util.Map;
import java.util.TreeMap;

import com.kunlun.poker.Config;
import com.kunlun.poker.back.domain.AbstactLog;
import com.kunlun.poker.back.service.StatisticsService;
import com.kunlun.poker.log.LogConstants;

/***
 * 
 * @author ljx
 */
public class ServiceUtil {

    /***
     * 从文件流获取数据
     * @param startTimePath
     * @param startTime
     * @param endTime
     * @param cloumnCount 
     *                 字段格式
     * @param lineByteLenth
     *                 一行的长度
     * @param service
     * @param mulity
     *               0 时间点某天 1 时间段 跨天
     * @return
     * @throws Exception
     */
    public static <E extends Number,T extends AbstactLog<E>> Map<E, T> getDataMap(MappedByteBuffer mbb, int fileLineCount, String startTimePath,int startTime, int endTime, int cloumnCount, int lineByteLenth, StatisticsService service, int mulity) throws Exception{
        int startLine = FileUtil.binarySearch(mbb, startTime, fileLineCount, cloumnCount, lineByteLenth, 8);
        
        if(1==mulity){
            endTime = FileUtil.getMiddleTime(mbb, fileLineCount, cloumnCount, lineByteLenth);
        }
        int endLine = FileUtil.binarySearch(mbb, endTime, fileLineCount, cloumnCount, lineByteLenth, 2);
        if(startLine != -1 && endLine != -1){
            int totalLine = endLine - startLine;
            if(totalLine > Constants.READ_LINE_COUNT){
                Map<E, T> sourceMap = new TreeMap<E, T>();
                int tempEndLine = Constants.READ_LINE_COUNT;
                do {
                    String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, lineByteLenth);
                    service.setCurrentLine(tempEndLine);
                    service.merge(sourceMap, service.transform(dataStr));
                    tempEndLine += Constants.READ_LINE_COUNT;
                    if(tempEndLine > fileLineCount){
                        tempEndLine = fileLineCount;
                        startLine += Constants.READ_LINE_COUNT;
                        dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, tempEndLine, lineByteLenth);
                        service.merge(sourceMap, service.transform(dataStr));
                        break;
                    }
                    startLine += Constants.READ_LINE_COUNT;
                } while (tempEndLine < fileLineCount);
                return (Map<E, T>) sourceMap;
            }
            String dataStr = FileUtil.obtainStringInRandomAccessFile(mbb, startLine, endLine, lineByteLenth);
            return service.transform(dataStr);
        }
        return null;
    }
    
    public static int getTotalLine(int diffValue, int startTime, String startTimePath,int cloumnCount, int lineByteLength) throws Exception{
        String startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
        int totalLine=0;
        for(int idx=0; idx<diffValue; idx++){
            if(!FileUtil.isExist(startTimePath)){
                break;
            }
            try(RandomAccessFile raf = new RandomAccessFile(startTimePath, "r");) {
                int fileLineCount = (int) (raf.length()/lineByteLength);
                totalLine += fileLineCount;
            }
            startTime = startTime + 86400;
            startTimeStr = Constants.DATAFORMAT.format(startTime*1000l);
            startTimePath = String.format("%s%s_%s",Config.getInstance().getLogDirName() + File.separator,
                    LogConstants.TL_CSV_USER_STATIC, startTimeStr+".csv");
        }
        return totalLine;
    }
    
}
