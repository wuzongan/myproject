package com.kunlun.poker.center.domain.rank;

import com.kunlun.poker.center.domain.User;

public class LevelRank extends Rank<Integer,LevelRank>{

    private int level;
    private float winRate;
    private int cardNum;
    private long totalBankroll;
    public int getLevel() {
        return level;
    }
    public float getWinRate() {
        return winRate;
    }
    public int getCardNum() {
        return cardNum;
    }
    public long getTotalBankroll() {
        return totalBankroll;
    }
    public LevelRank(User user) {
        super(user);
        this.level = user.getLevel();
        this.winRate = user.getWinRate();
        this.cardNum = user.getCardNum();
        this.totalBankroll = user.getTotalBankroll();
    }

    @Override
    public int compareTo(LevelRank o) {
//        User user = getUser();
//        User otherUser = o.getUser();
        int result = Integer.compare(o.getLevel(), getLevel());
        if (result != 0) {
            return result;
        }
        result = Float.compare(o.getWinRate(), getWinRate());
        if (result != 0) {
            return result;
        }
        result = Integer.compare(o.getCardNum(), getCardNum());
        if (result != 0) {
            return result;
        }
        return Long.compare(o.getTotalBankroll(),
                getTotalBankroll());
    }

    @Override
    protected Integer initValue() {
        return getUser().getLevel();
    }

}
