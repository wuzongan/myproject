/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.game.domain.actions;

import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.Seat;

/**
 *
 * @author Administrator
 */
public class Raise implements Action{
    public static final String NAME = "raise";
    private long stake;
    
     /**
     * 接口isValid方法重写
     * @param seat
     * @return 
     */
    @Override
    public boolean isValid(Seat seat) {
        int bigBlindBets = seat.getDesk().getRoom().getBigBlindBets();
        return  stake > Math.max(seat.getDesk().getMaxBets(), bigBlindBets)
                && stake % bigBlindBets == 0
                && seat.getPlayer().getBankroll() + seat.getStake() > stake;
    }

    /**
     * 接口方法execute重写
     * @param seat 
     */
    @Override
    public void execute(Seat seat) {
        seat.bet(getStake() - seat.getStake());
    }

    /**
     * @return the stake
     */
    public long getStake() {
        return stake;
    }

    /**
     * @param stake the stake to set
     */
    public void setStake(long stake) {
        this.stake = stake;
    }

    /**
     * 接口方法重写
     * @return 
     */
    @Override
    public String toString() {
        return "加注" + stake; //To change body of generated methods, choose Tools | Templates.
    }

	@Override
    public String getName()
	{
		return NAME;
	}
}
