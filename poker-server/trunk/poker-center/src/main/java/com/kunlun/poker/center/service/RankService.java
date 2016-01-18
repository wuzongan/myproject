package com.kunlun.poker.center.service;

import java.util.Collection;

import com.kunlun.poker.center.domain.rank.Rank;

public interface RankService {

    static final int RANK_TYPE_WINRATE = 1;
    static final int RANK_TYPE_LEVEL = 2;
    static final int RANK_TYPE_SINGELWINBANKROLL = 3;
    static final int RANK_TYPE_BANKROLL = 4;
    
	int MAX_SIZE = 50;

	Collection<Rank<?, ?>> getRanks(int type);

	void loadAllRank();
	
	Rank<?,?> getFirstRank(int type);
}
