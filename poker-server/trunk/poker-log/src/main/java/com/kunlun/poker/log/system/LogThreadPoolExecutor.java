package com.kunlun.poker.log.system;

import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;

/**
 * 业务线程池
 * @author ljx
 */
public class LogThreadPoolExecutor extends OrderedThreadPoolExecutor {

    public LogThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        super(corePoolSize, maximumPoolSize);
    }
}
