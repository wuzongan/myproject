package com.kunlun.poker.center.domain;

import com.googlecode.canoe.event.EventDispatcherAdapter;

public class Actor<T extends Actor<T>> extends EventDispatcherAdapter<T>{
    protected int id;
    protected String name;
    protected int sex;
    protected String portrait;
    protected int level;
    protected long bankroll;
    protected int cardNum;
	protected int winCardNum;
    protected String bestCards;
    protected long dealerTips;
    protected long singleWinBankroll;
    
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public int getLevel() {
		return level;
	}
    public void setLevel(int level) {
    	this.level = level;
    }
	public long getBankroll() {
		return bankroll;
	}
	public void setBankroll(long bankroll) {
		this.bankroll = bankroll;
	}
	public int getCardNum() {
		return cardNum;
	}
	public void setCardNum(int cardNum) {
		this.cardNum = cardNum;
	}
	public int getWinCardNum() {
		return winCardNum;
	}
	public void setWinCardNum(int winCardNum) {
		this.winCardNum = winCardNum;
	}
	public String getBestCards() {
		return bestCards;
	}
	public void setBestCards(String bestCards) {
		this.bestCards = bestCards;
	}
    public long getDealerTips() {
		return dealerTips;
	}
	public void setDealerTips(long dealerTips) {
		this.dealerTips = dealerTips;
	}
    /**
     * 单局赢的最大筹码量
     * 
     * @return
     */
    public long getSingleWinBankroll(){
		return singleWinBankroll;
    }
    
    public void setSingleWinBankroll(long singleWinBankroll) {
        if (this.getSingleWinBankroll() < singleWinBankroll) {
            this.singleWinBankroll = singleWinBankroll;
        }
    }
    /**
     * 玩家称号信息
     * 
     * @return String
     */

    public String toString() {
        return "[玩家]" + name;
    }
}
