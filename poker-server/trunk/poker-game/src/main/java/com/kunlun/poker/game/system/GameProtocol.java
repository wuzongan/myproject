package com.kunlun.poker.game.system;

public class GameProtocol {
	public static final int S_ERROR = 102;
	public static final int C_ENTER = 201;
	public static final int S_ENTER_SELF = 202;
	public static final int S_ENTER_MASS = 204;
	public static final int S_GAME_START = 208;
	public static final int S_DEAL_HAND = 210;
	public static final int S_ACTION = 206;
	public static final int S_DISPOSE_POTS = 212;
	public static final int S_TURN_ROUND = 214;
	public static final int S_SHUT_DOWN = 216;
	public static final int S_COUNTDOWN = 218;
	public static final int C_ACTION = 205;
	public static final int C_SHOW_DOWN = 219;
	public static final int S_SHOW_DOWN = 220;
	public static final int C_CANCEL_AGENT = 221;
	public static final int S_SWITCH_AGENT = 222;
    public static final int C_SET_AUTO_MAKING_UP = 223;
	public static final int S_SET_AUTO_MAKING_UP = 224;
	public static final int C_SET_AUTO_STANDING_UP = 225;
	public static final int S_SET_AUTO_STANDING_UP = 226;
	public static final int C_EXIT = 227;
	public static final int S_EXIT = 228;
	public static final int C_SEAT_DOWN = 229;
	public static final int S_SEAT_DOWN = 230;
	public static final int C_STAND_UP = 231;
	public static final int S_STAND_UP = 232;
	public static final int C_GIVE_DEALER_TIP = 233;
	public static final int S_GIVE_DEALER_TIP = 234;
	public static final int C_CHANGE_DEALER = 235;
	public static final int S_CHANGE_DEALER = 236;
	
        
	public static final int C_CHAT = 301;
    public static final int S_CHAT = 302;
    
    //public static final int C_ROOM_PLAYER_NUMS = 411;
    //public static final int S_ROOM_PLAYER_NUMS =412;
    
    public static final int C_RUSH = 417;
    public static final int S_RUSH = 418;
    public static final int C_MAKING_UP = 423;
    public static final int S_MAKING_UP = 424;
    
}
