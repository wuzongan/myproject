package com.kunlun.poker.center.domain;

import com.googlecode.canoe.event.Event;


public class UserEvent extends Event<User>{
    
	public static final String ATTR_CHANGED = "attrChanged";
	
	public static final String LEVEL_CHANGED = "levelChanged";
	
	public static final String BANKROLL_CHANGED = "bankrollChanged";
	
	public static final String BUY_CHIPS = "buyChips";
	
	public static final String TOOL_TIP = "toolTip";
	
	private final int chips;
	private final String content;
	
    public UserEvent(String type) {
    	this(type, 0, null);
	}
	
    public UserEvent(String type, int chips){
    	this(type ,chips, null);
    }
    
    private UserEvent(String type, int chips, String content) 
    {
		super(type);
		this.chips = chips;
		this.content = content;
    }
    
    public int getChips(){
    	return chips;
    }
    
    public String getContent(){
    	return content;
    }
	
}
