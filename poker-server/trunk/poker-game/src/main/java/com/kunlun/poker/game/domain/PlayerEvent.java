package com.kunlun.poker.game.domain;

import com.googlecode.canoe.event.Event;

public class PlayerEvent extends Event<Player> {
    public static final String SWITCH_AGENT = "switchAgent";

    public PlayerEvent(String type) 
    {
		super(type);
    }
}
