package com.kunlun.poker.back.domain;

public class CsvLoginFreeChipsLog extends AbstactLog<Integer>{

	private long loginFreeChips;
	private int createTime;
		
	public long getLoginFreeChips() {
		return loginFreeChips;
	}
	public void setLoginFreeChips(long loginFreeChips) {
		this.loginFreeChips = loginFreeChips;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	@Override
	public void merge(AbstactLog<Integer> log) {
		CsvLoginFreeChipsLog targetLog = (CsvLoginFreeChipsLog) log;
        this.setId(targetLog.getId());
        this.setLoginFreeChips(targetLog.getLoginFreeChips());
        this.setCreateTime(targetLog.getCreateTime());		
	}
}
