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
public class Fold implements Action{
    public static final String NAME = "fold";

     /**
     * 接口isValid方法重写
     * @param seat
     * @return 
     */
    @Override
    public boolean isValid(Seat seat) {
        return true;
    }

    /**
     * 接口方法execute重写
     * @param seat 
     */
    @Override
    public void execute(Seat seat) {
        seat.fold();
    }

    /**
     * 接口方法重写
     * @return 
     */
    @Override
    public String toString() {
        return "弃牌"; //To change body of generated methods, choose Tools | Templates.
    }


	@Override
    public String getName()
	{
		return NAME;
	}
    
}
