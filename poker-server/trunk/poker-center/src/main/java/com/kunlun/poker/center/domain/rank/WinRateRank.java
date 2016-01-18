package com.kunlun.poker.center.domain.rank;

import com.kunlun.poker.center.domain.User;

public class WinRateRank extends Rank<Float, WinRateRank>{
    
    private float winRate;
    private int cardNum;
    private int level;
    private long totalBankroll;
    public float getWinRate() {
        return winRate;
    }
    public int getCardNum() {
        return cardNum;
    }
    public int getLevel() {
        return level;
    }
    public long getTotalBankroll() {
        return totalBankroll;
    }
    public WinRateRank(User user) {
        super(user);
        this.winRate = user.getWinRate();
        this.cardNum = user.getCardNum();
        this.level = user.getLevel();
        this.totalBankroll = user.getTotalBankroll();
    }

    @Override
    public int compareTo(WinRateRank o) {
//        User user = getUser();
//        User otherUser = o.getUser();
        int  result =Float.compare(o.getWinRate(), getWinRate());
        if(result != 0){
            return result;
        }
        result = Integer.compare(o.getCardNum(), getCardNum());
        if(result != 0){
            return result;
        }
        result = Integer.compare(o.getLevel(), getLevel());
        if(result != 0){
            return result;
        }
        return Long.compare(o.getTotalBankroll(), getTotalBankroll());
    }

    @Override
    protected Float initValue() {
        return getUser().getWinRate();
    }
}
