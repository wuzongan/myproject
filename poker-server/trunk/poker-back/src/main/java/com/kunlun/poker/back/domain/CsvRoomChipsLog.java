package com.kunlun.poker.back.domain;


public class CsvRoomChipsLog extends AbstactLog<Long>{
	private long totalChips;
	private int averageChips;
	private int time;
	public long getTotalChips() {
		return totalChips;
	}
	public void setTotalChips(long totalChips) {
		this.totalChips = totalChips;
	}
	public int getAverageChips() {
		return averageChips;
	}
	public void setAverageChips(int average) {
		this.averageChips = average;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	@Override
	public void merge(AbstactLog<Long> log) {
        CsvRoomChipsLog targetLog = (CsvRoomChipsLog) log;
        this.setId(targetLog.getId());
        this.setAverageChips(targetLog.getAverageChips());
        this.setTotalChips(targetLog.getTotalChips());
        this.setTime(targetLog.getTime());
	}
}
