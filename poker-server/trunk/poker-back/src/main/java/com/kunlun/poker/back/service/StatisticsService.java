package com.kunlun.poker.back.service;

import java.util.Map;



/***
 * 转换接口
 * @author ljx
 */
import com.kunlun.poker.back.domain.AbstactLog;

/***
 * 
 * @author ljx
 */
public interface StatisticsService {

    <E extends Number,T extends AbstactLog<E>> Map<E, T> transform(String str);
    
    <E extends Number,T extends AbstactLog<E>> Map<E, T> merge(Map<E, T> sourceMap, Map<E, T> map);
    
    boolean isReading();

    int getCurrentLine();
    
    int getTotalLine();
    
    void setCurrentLine(int currentLine);
    
    void clear();
    
    int getTotalSize();


}
