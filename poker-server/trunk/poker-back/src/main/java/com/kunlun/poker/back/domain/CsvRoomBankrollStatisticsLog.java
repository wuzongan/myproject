package com.kunlun.poker.back.domain;

public class CsvRoomBankrollStatisticsLog extends AbstactLog<Integer> implements
        Comparable<CsvRoomBankrollStatisticsLog> {

    private int totalBankroll;
    private int serviceBankroll;
    
    public int getTotalBankroll() {
        return totalBankroll;
    }

    public void setTotalBankroll(int totalBankroll) {
        this.totalBankroll = totalBankroll;
    }

    public int getServiceBankroll() {
        return serviceBankroll;
    }

    public void setServiceBankroll(int serviceBankroll) {
        this.serviceBankroll = serviceBankroll;
    }

    @Override
    public boolean equals(Object obj) {
        return getClass() == obj.getClass() && getId() == ((CsvRoomBankrollStatisticsLog)obj).getId();
    }
    
    @Override
    public int compareTo(CsvRoomBankrollStatisticsLog o) {
        int result = Integer.compare(getId(), o.getId());
        return result;
    }

    @Override
    public void merge(AbstactLog<Integer> log) {
        CsvRoomBankrollStatisticsLog targetLog = (CsvRoomBankrollStatisticsLog) log;
        this.setId(targetLog.getId());
        this.setTotalBankroll(this.getTotalBankroll() + targetLog.getTotalBankroll());
        this.setServiceBankroll(this.getServiceBankroll() + targetLog.getServiceBankroll());
    }

}
