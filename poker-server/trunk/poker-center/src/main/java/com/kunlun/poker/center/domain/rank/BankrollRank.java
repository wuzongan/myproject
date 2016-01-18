package com.kunlun.poker.center.domain.rank;

import com.kunlun.poker.center.domain.User;

public class BankrollRank extends Rank<Long, BankrollRank> {
    
    private long totalBankroll;
    public long getTotalBankroll() {
        return totalBankroll;
    }
    public BankrollRank(User user) {
        super(user);
        this.totalBankroll = user.getTotalBankroll();
    }

    protected Long initValue() {
        return getUser().getTotalBankroll();
    }

    @Override
    public int compareTo(BankrollRank o) {
    	return Long.compare(o.getTotalBankroll(), getTotalBankroll());
    }
}
