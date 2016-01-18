/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.game.domain.actions;

import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.Seat;

/**
 * 继承接口类Action类
 * @author Administrator
 */
public class Call implements Action{
    public static final String NAME = "call";

     /**
     * 接口isValid方法重写
     * @param seat
     * @return 
     */
    @Override
    public boolean isValid(Seat seat) {
        long maxBets = seat.getDesk().getMaxBets();
        return maxBets != 0 && seat.getStake() < maxBets && seat.getPlayer().getBankroll() + seat.getStake() > maxBets;
    }
    
    /**
     * 接口方法execute重写
     * @param seat 
     */
    @Override
    public void execute(Seat seat) {
        seat.bet(seat.getDesk().getMaxBets() - seat.getStake());
    }

    /**
     * 接口方法重写
     * @return 
     */
    @Override
    public String toString() {
        return "跟注";
    }

	@Override
    public String getName()
	{
		return NAME;
	}
}
