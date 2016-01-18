package com.kunlun.poker.back.domain;

import java.util.HashSet;
import java.util.Set;

public class CsvUserStatisticsLog extends AbstactLog<Integer> implements Comparable<CsvUserStatisticsLog>{
//    //房间id
//    private int roomId;
    //累计人数
    private Set<Integer> userIdSet;
    //累计次数
    private int userFrequencyCount;
    //手动选择人数
    private Set<Integer> handChooseUserIdSet;
    //快熟开始人数
    private Set<Integer> quickStartUserIdSet;
    //手动选择次数
    private int handChooseCount;
    //快速开始次数
    private int quickStartCount;
    
    public CsvUserStatisticsLog() {
        this.userIdSet = new HashSet<Integer>();
        this.handChooseUserIdSet = new HashSet<Integer>();
        this.quickStartUserIdSet = new HashSet<Integer>();
    }
    public Set<Integer> getUserIdSet() {
        return userIdSet;
    }
    public void setUserIdSet(Set<Integer> userIdSet) {
        this.userIdSet = userIdSet;
    }
    public int getUserFrequencyCount() {
        return userFrequencyCount;
    }
    public void setUserFrequencyCount(int userFrequencyCount) {
        this.userFrequencyCount = userFrequencyCount;
    }
    public Set<Integer> getHandChooseUserIdSet() {
        return handChooseUserIdSet;
    }
    public void setHandChooseUserIdSet(Set<Integer> handChooseUserIdSet) {
        this.handChooseUserIdSet = handChooseUserIdSet;
    }
    public Set<Integer> getQuickStartUserIdSet() {
        return quickStartUserIdSet;
    }
    public void setQuickStartUserIdSet(Set<Integer> quickStartUserIdSet) {
        this.quickStartUserIdSet = quickStartUserIdSet;
    }
    public int getHandChooseCount() {
        return handChooseCount;
    }
    public void setHandChooseCount(int handChooseCount) {
        this.handChooseCount = handChooseCount;
    }
    public int getQuickStartCount() {
        return quickStartCount;
    }
    public void setQuickStartCount(int quickStartCount) {
        this.quickStartCount = quickStartCount;
    }
    
    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && getId() == ((CsvUserStatisticsLog) obj).getId();
    }
    
    @Override
    public int compareTo(CsvUserStatisticsLog o) {
        int result = Integer.compare(this.getId(), o.getId());
        return result;
    }
    @Override
    public void merge(AbstactLog<Integer> log) {
        CsvUserStatisticsLog targetLog = (CsvUserStatisticsLog) log;
        this.setId(targetLog.getId());
        this.getUserIdSet().addAll(targetLog.getUserIdSet());
        this.setUserFrequencyCount(this.getUserFrequencyCount() + targetLog.getUserFrequencyCount());
        this.setHandChooseCount(this.getHandChooseCount() + targetLog.getHandChooseCount());
        this.setQuickStartCount(this.getQuickStartCount() + targetLog.getQuickStartCount());
        this.getQuickStartUserIdSet().addAll(targetLog.getQuickStartUserIdSet());
        this.getHandChooseUserIdSet().addAll(targetLog.getHandChooseUserIdSet());
     }
    
}
