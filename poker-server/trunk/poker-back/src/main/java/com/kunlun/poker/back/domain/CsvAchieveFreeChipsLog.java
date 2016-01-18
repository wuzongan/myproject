package com.kunlun.poker.back.domain;

public class CsvAchieveFreeChipsLog extends AbstactLog<Integer>{
	private long achievementFreeChips;
	private int createTime;
	
	public long getAchievementFreeChips() {
		return achievementFreeChips;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public void setAchievementFreeChips(long achievementFreeChips) {
		this.achievementFreeChips = achievementFreeChips;
	}
	@Override
	public void merge(AbstactLog<Integer> log) {
        CsvAchieveFreeChipsLog targetLog = (CsvAchieveFreeChipsLog) log;
        this.setId(targetLog.getId());
        this.setAchievementFreeChips(targetLog.getAchievementFreeChips());
        this.setCreateTime(targetLog.getCreateTime());
	}
}
