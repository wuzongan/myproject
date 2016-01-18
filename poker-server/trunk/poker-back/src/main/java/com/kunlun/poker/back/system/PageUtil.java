package com.kunlun.poker.back.system;

import java.util.Map;
import com.kunlun.poker.back.domain.AbstactLog;

public class PageUtil {

    @SuppressWarnings("unchecked")
    public static <E extends Number,T> Map<E, T> getPageMap(Map<E, T> dataMap,
            Map<E, T> sourceMap, int pageSize, int pageIndex) {
        int totalCount = dataMap.size();
        if (pageIndex > totalCount) {
            throw new IllegalArgumentException("请求的行数错误"
                    + String.format("pageIndex:%s>totalCount:%s", pageIndex,
                            totalCount));
        }
        int pageStartIndex = pageIndex;
        int pageEndIndex = pageIndex + pageSize;
        if (pageEndIndex > totalCount) {
            pageEndIndex = totalCount;
        }
        int tempIndex = 0;
        for (T log : dataMap.values()) {
            if (tempIndex >= pageStartIndex && tempIndex < pageEndIndex) {
                if (log instanceof AbstactLog) {
                    sourceMap.put(((AbstactLog<E>) log).getId(), log);
                }
            }
            tempIndex++;
        }
        return sourceMap;

    }
    
    
    
    
}
