package com.kunlun.poker.game.domain.robot;

public class ActionProbability {
	public static float PRE_FLOP_AA = 1f;
	public static float PRE_FLOP_KK = .99547f;
	public static float PRE_FLOP_QQ = .99094f;
	public static float PRE_FLOP_JJ = .97734f;
	public static float PRE_FLOP_TT = .95924f;
	public static float PRE_FLOP_PAIR = .93684f;
	public static float PRE_FLOP_A_OTHER = .87804f;
	public static float PRE_FLOP_FLUSH = .86594f;
	public static float PRE_FLOP_CONNECTORS = .70592f;
	public static float PRE_FLOP_TUP_CONNECTORS = .86594f;
	public static float PRE_FLOP_OTHER = .51264f;
	
	public static float OTHER_THREE_OF_A_KIND = .849f ;
	public static float OTHER_FULL_HOUSE = .99752f ;
	public static float OTHER_FOUR_OF_A_KIND = .99992f ;
	public static float OTHER_ONE_PAIR = .711f ;
	public static float OTHER_TWO_PAIR = .731f ;
	public static float OTHER_FLUSH = .9891f ;
	public static float OTHER_FLUSH_SHIFT = .196f ;
	public static float OTHER_STRAIGHT_SHIFT = .096f ;
	public static float OTHER_FLUSH_STRAIGHT_SHIFT = .387f ;
	public static float OTHER_STRAIGHT = .976f ;
	public static float OTHER_ROYAL_FLUSH = 1f ;
	public static float OTHER_STRAIGHT_FLUSH = .99996f ;
	public static float OTHER_HIGH_CARD = .096f;
	
	public static int CALL_PROB_ONE_CASE = 5;
	public static int CALL_PROB_FLOP_TWO_CASE = 10;
	public static int CALL_PROB_THREE_CASE = 20;
	
	public static float SCOPE_PROB_ZERO = .01f;
	public static float SCOPE_PROB_ONE  = .5f;
	public static float SCOPE_PROB_TWO = .8f;
	public static float SCOPE_PROB_THREE = .94f;
	public static float SCOPE_PROB_FOUR = 1f;
	
	public static int FIRST_BIG_BLIND_RISE_LOW = 2;
	public static int FIRST_BIG_BLIND_RISE_HIGH = 5;
	
}
