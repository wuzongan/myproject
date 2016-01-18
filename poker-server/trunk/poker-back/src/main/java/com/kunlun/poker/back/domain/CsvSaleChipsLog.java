package com.kunlun.poker.back.domain;

public class CsvSaleChipsLog extends AbstactLog<Integer>{
	private long saleChips;
	private int createTime;

	public long getSaleChips() {
		return saleChips;
	}

	public void setSaleChips(long saleChips) {
		this.saleChips = saleChips;
	}
	
	public int getCreateTime() {
		return createTime;
	}

	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}

	@Override
	public void merge(AbstactLog<Integer> log) {
		CsvSaleChipsLog targetLog = (CsvSaleChipsLog) log;
        this.setId(targetLog.getId());
        this.setSaleChips(targetLog.getSaleChips());
        this.setCreateTime(targetLog.getCreateTime());
	}
}
