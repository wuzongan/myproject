package com.kunlun.poker.domain;

/***
 * 
 * @author ljx
 */
public interface RobotAiConstants {
    /***两张牌 ************************************************************************************/
    public static final int TWO_ONE_PAIR = 0;
    public static final int TWO_STRAIGHT= 1;
    public static final int TWO_THREE_OF_A_KIND = 2;
    public static final int TWO_FLUSH = 3;
    public static final float[] TWO_WEIGHT = new float[]{32.4f, 1.31f, 12.7f, 0.842f};    
    /***两张牌 ************************************************************************************/

    /***首三张牌 ************************************************************************************/
    public static final int FIVE_FLUSH = 0;
    public static final int FIVE_ONE_PAIR = 1;
    public static final int FIVE_STRAIGHT = 2;
    public static final int FIVE_INNER_STRAIGHT = 3;
    public static final int FIVE_FULL_HOUSE = 4;
    public static final int FIVE_FOUR_OF_A_KIND = 5;
    public static final int FIVE_THREE_OF_A_KIND = 6;
    public static final int FIVE_TWO_PAIR = 7;
    public static final float[] FIVE_WEIGHT = new float[]{19.1f, 12.8f, 17f, 8.5f, 8.5f, 2.1f, 4.3f, 6.4f};
    /***首三张牌 ************************************************************************************/
    
    /***第四张牌 ************************************************************************************/
    public static final int SIX_FLUSH = 0;
    public static final int SIX_ONE_PAIR = 1;
    public static final int SIX_STRAIGHT = 2;
    public static final int SIX_INNER_STRAIGHT = 3;
    public static final int SIX_FULL_HOUSE = 4;
    public static final int SIX_FOUR_OF_A_KIND = 5;
    public static final int SIX_THREE_OF_A_KIND = 6;
    public static final int SIX_TWO_PAIR = 7;
    public static final float[] SIX_WEIGHT = new float[]{19.6f, 13f, 17.4f, 8.7f, 8.7f, 2.2f, 4.3f, 6.4f};
    /***第四张牌 ************************************************************************************/
    
    
}
