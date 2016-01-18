
package com.kunlun.poker.back.domain;

import java.util.HashSet;
import java.util.Set;

public class CsvUserOnlineStatisticsLog extends AbstactLog<Integer> implements Comparable<CsvUserOnlineStatisticsLog>{
    private Set<Integer> userIdSet;
    private int totalBankroll;
    public CsvUserOnlineStatisticsLog() {
        this.userIdSet = new HashSet<Integer>();
    }
    public Set<Integer> getUserIdSet() {
        return userIdSet;
    }
    public void setUserIdSet(Set<Integer> userIdSet) {
        this.userIdSet = userIdSet;
    }
    public int getTotalBankroll() {
        return totalBankroll;
    }
    public void setTotalBankroll(int totalBankroll) {
        this.totalBankroll = totalBankroll;
    }
    
    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && getId() == ((CsvUserOnlineStatisticsLog)obj).getId();
    }

    @Override
    public int compareTo(CsvUserOnlineStatisticsLog o) {
        int result = Integer.compare(getId(), o.getId());
        return result;
    }
    @Override
    public void merge(AbstactLog<Integer> log) {
        CsvUserOnlineStatisticsLog targetLog = (CsvUserOnlineStatisticsLog) log;
        this.setId(targetLog.getId());
        this.getUserIdSet().addAll(targetLog.getUserIdSet());
        this.setTotalBankroll(this.getTotalBankroll() + targetLog.getTotalBankroll());
    }
}
