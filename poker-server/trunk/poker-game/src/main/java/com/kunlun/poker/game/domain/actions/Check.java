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
public class Check implements Action {
    public static final String NAME = "check";

     /**
     * 接口isValid方法重写
     * @param seat
     * @return 
     */
    @Override
    public boolean isValid(Seat seat) {
        return seat.getDesk().getMaxBets() == seat.getStake();
    }

    /**
     * 接口方法execute重写
     * @param seat 
     */
    @Override
    public void execute(Seat seat) {
    }
    
    /**
     * 接口方法重写
     * @return 
     */
    @Override
    public String toString() {
        return "过牌"; //To change body of generated methods, choose Tools | Templates.
    }


	@Override
    public String getName()
	{
		return NAME;
	}
    
}
