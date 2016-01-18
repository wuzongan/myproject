package com.kunlun.poker.rmi.dto;

import java.io.Serializable;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.PokerHand;

public class PlayerDTO implements Serializable {
    private static final long serialVersionUID = 7415605099940533916L;

    private int id;
    private String name;
    private int roomId;
    private long bankroll;
    private float revenue;
    private int exp;
    private boolean win;
    private PokerHand pokerHand;
    private Card[] bestCards;
    private long dealerTips;
    private boolean autoPlay;
    private int robotAi;
    private String portrait;

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

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

    public long getBankroll() {
        return bankroll;
    }

    public void setBankroll(long bankroll) {
        this.bankroll = bankroll;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public boolean isWin() {
        return win;
    }

    public void setWin(boolean isWin) {
        this.win = isWin;
    }

    public PokerHand getPokerHand() {
        return pokerHand;
    }

    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    public Card[] getBestCards() {
        return bestCards;
    }

    public void setBestCards(Card[] bestCards) {
        this.bestCards = bestCards;
    }

    public long getDealerTips() {
        return dealerTips;
    }

    public void setDealerTips(long dealerTips) {
        this.dealerTips = dealerTips;
    }
    
    public boolean isAutoPlay() {
		return autoPlay;
	}

	public void setAutoPlay(boolean autoPlay) {
		this.autoPlay = autoPlay;
	}

	public int getRobotAi() {
		return robotAi;
	}

	public void setRobotAi(int robotAi) {
		this.robotAi = robotAi;
	}

}
