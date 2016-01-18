/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.game.domain.actions;

import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.Seat;

/**
 * 全下，继承Action接口
 * 
 * @author Administrator
 */
public class AllIn implements Action {
	public static final String NAME = "allin";

	/**
	 * 接口isValid方法重写
	 * 
	 * @param seat
	 * @return
	 */
	@Override
	public boolean isValid(Seat seat) {
		return true;
	}

	/**
	 * 接口方法execute重写
	 * 
	 * @param seat
	 */
	@Override
	public void execute(Seat seat) {
		seat.bet(seat.getPlayer().getBankroll());
	}

	/**
	 * 接口方法重写
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "全入";
	}

	@Override
    public String getName()
	{
		return NAME;
	}
}
