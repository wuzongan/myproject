package com.kunlun.poker.back.domain;

public class CsvDrawChipsLog extends AbstactLog<Integer>{
	private long drawChips;
	private Integer createTime;

	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	public long getDrawChips() {
		return drawChips;
	}

	public void setDrawChips(long drawChips) {
		this.drawChips = drawChips;
	}

	@Override
	public void merge(AbstactLog<Integer> log) {
		CsvDrawChipsLog targetLog = (CsvDrawChipsLog) log;
        this.setId(targetLog.getId());
        this.setDrawChips(targetLog.getDrawChips());
        this.setCreateTime(targetLog.getCreateTime());
	}
}
