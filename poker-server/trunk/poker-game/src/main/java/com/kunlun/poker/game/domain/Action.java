package com.kunlun.poker.game.domain;



/**
 * Action接口
 * @author Administrator
 */
public interface Action {
    boolean isValid(Seat seat);
    void execute(Seat seat);
    String getName();
}
