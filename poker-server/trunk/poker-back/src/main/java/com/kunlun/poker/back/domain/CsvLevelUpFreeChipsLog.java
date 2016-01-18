package com.kunlun.poker.back.domain;

public class CsvLevelUpFreeChipsLog extends AbstactLog<Integer>{
	private long levelUpChips;
	private int createTime;
	
	public long getLevelUpChips() {
		return levelUpChips;
	}
	public void setLevelUpChips(long levelUpChips) {
		this.levelUpChips = levelUpChips;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	@Override
	public void merge(AbstactLog<Integer> log) {
        CsvLevelUpFreeChipsLog targetLog = (CsvLevelUpFreeChipsLog) log;
        this.setId(targetLog.getId());
        this.setLevelUpChips(targetLog.getLevelUpChips());
        this.setCreateTime(targetLog.getCreateTime());
	}
}
