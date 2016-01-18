package com.kunlun.poker.center.domain.rank;

import com.kunlun.poker.center.domain.User;

public class SingleWinBankrollRank extends Rank<Long,SingleWinBankrollRank>{   

    private long singleWinBankroll;
    private int cardNum;
    private float winRate;
    private long totalBankroll;
    public long getSingleWinBankroll() {
        return singleWinBankroll;
    }
    public float getWinRate() {
        return winRate;
    }
    public long getTotalBankroll() {
        return totalBankroll;
    }
    public int getCardNum() {
        return cardNum;
    }
    public SingleWinBankrollRank(User user) {
        super(user);
        this.singleWinBankroll = user.getSingleWinBankroll();
        this.winRate = user.getWinRate();
        this.totalBankroll = user.getTotalBankroll();
        this.cardNum = user.getCardNum();
    }

    @Override
    public int compareTo(SingleWinBankrollRank o) {
//        User user = getUser();
//        User otherUser = o.getUser();
        int  result = Long.compare(o.getSingleWinBankroll(), getSingleWinBankroll());
        if(result != 0){
            return result;
        }
        result = Integer.compare(o.getCardNum(), getCardNum());
        if(result != 0){
            if(result > 0){
                   return -1;
            }else {
                return 1;//牌局数量少的优先
            }
        }
        result =Float.compare(o.getWinRate(), getWinRate());
        if(result != 0){
            return result;
        }
        result = Integer.compare(o.getCardNum(), getCardNum());
        if(result != 0){
            return result;
        }
        return Long.compare(o.getTotalBankroll(), getTotalBankroll());
    }

    @Override
    protected Long initValue() {
        return getUser().getSingleWinBankroll();
    }

}
