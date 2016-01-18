package com.kunlun.poker.back.domain;

public class CsvDealerChipsLog extends AbstactLog<Integer>{
	private long dealerChips;
	private Integer createTime;

	public long getDealerChips() {
		return dealerChips;
	}

	public void setDealerChips(long dealerChips) {
		this.dealerChips = dealerChips;
	}
	
	public Integer getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Integer createTime) {
		this.createTime = createTime;
	}

	@Override
	public void merge(AbstactLog<Integer> log) {
		CsvDealerChipsLog targetLog = (CsvDealerChipsLog) log;
        this.setId(targetLog.getId());
        this.setDealerChips(targetLog.getDealerChips());
        this.setCreateTime(targetLog.getCreateTime());
	}
}
