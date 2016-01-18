package com.kunlun.poker.game.domain;

import com.googlecode.canoe.event.Event;

public class DeskEvent extends Event<Desk>{
	public static final String GAME_START = "gameStart";
    public static final String ACTION = "action";
    public static final String TURN_ROUND = "turnRound";
    public static final String GAME_OVER = "gameOver";
    public static final String COUNTDOWN = "countdown";
    public static final String DISPOSE_POTS = "disposePots";
    public static final String SHOW_DOWN = "showdown";
    public static final String STAND_UP = "standUp";
    public static final String ENTER = "enter";
    public static final String EXIT = "exit";
    public static final String LOGGER = "logger";
    public static final String SEAT_DOWN = "seatDown";
    public static final String RUSH = "rush";
    // public DeskEvent()

    private final Action action;
    private final Seat seat;
    private final Player player;
    private final float seconds;

    public DeskEvent(String type)
    {
    	this(type, null, null, null, 0);
    }
    
    public DeskEvent(String type, Action action) {
    	this(type, null, action, null, 0);
	}
    
    public DeskEvent(String type, Seat seat) {
    	this(type, seat, null, null, 0);
	}
    public DeskEvent(String type, Player player) {
    	this(type, null, null, player, 0);
	}

    public DeskEvent(String type, float seconds) {
    	this(type, null, null, null, seconds);
	}
    
    public DeskEvent(String type, Seat seat, Player player)
    {
    	this(type, seat, null, player, 0);
    }
    
    private DeskEvent(String type, Seat seat, Action action, Player player, float seconds) 
    {
		super(type);
		this.seat = seat;
		this.action = action;
		this.player = player;
		this.seconds = seconds;
    }

	public Action getAction() {
		return action;
	}

	public Seat getSeat() {
		return seat;
	}

	public Player getPlayer() {
		return player;
	}

	public float getSeconds() {
		return seconds;
	}

}
