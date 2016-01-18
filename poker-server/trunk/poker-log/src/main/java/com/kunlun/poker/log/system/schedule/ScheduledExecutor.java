package com.kunlun.poker.log.system.schedule;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutor {
    private static final ScheduledExecutor instance = new ScheduledExecutor();
    public static ScheduledExecutor getIntance(){
        return instance;
    }
    private final ScheduledExecutorService exec;
    public ScheduledExecutor(){
        this.exec = Executors.newScheduledThreadPool(1);
    }
    
    public void schedule(Runnable task){
        long initialDelay = 0l;
        Calendar cal = Calendar.getInstance();
        cal.get(Calendar.HOUR_OF_DAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 1);
        
        initialDelay = cal.getTimeInMillis() - System.currentTimeMillis();
        if(initialDelay < 0){
            initialDelay = 0l;
        }
        this.exec.scheduleAtFixedRate(task, initialDelay, 1, TimeUnit.DAYS);
    }
    
    public void stop(){
        exec.shutdown();
    }
    
}
