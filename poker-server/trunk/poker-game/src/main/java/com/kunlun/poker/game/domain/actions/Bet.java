/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kunlun.poker.game.domain.actions;

import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.Seat;

/**
 * 下注操作
 * @author Administrator
 */
public class Bet implements Action {

    public static final String NAME = "bet";

     /**
     * 接口isValid方法重写
     * @param seat
     * @return 
     */
    @Override
    public boolean isValid(Seat seat) {
        int bigBlindBets = seat.getDesk().getRoom().getBigBlindBets();
        return seat.getDesk().getMaxBets() == 0 && seat.getPlayer().getBankroll() > bigBlindBets;
    }

    /**
     * 接口方法execute重写
     * @param seat 
     */
    @Override
    public void execute(Seat seat) {
        seat.bet(seat.getDesk().getRoom().getBigBlindBets() - seat.getStake());
    }

    /**
     * 接口方法重写
     * @return 
     */
    @Override
    public String toString() {
        return "押注"; //To change body of generated methods, choose Tools | Templates.
    }

	@Override
    public String getName()
	{
		return NAME;
	}
}
