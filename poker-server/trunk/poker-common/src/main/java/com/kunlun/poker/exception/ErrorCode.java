package com.kunlun.poker.exception;

public class ErrorCode {
	/**
	 * 无法进入房间
	 */
    public static final int CAN_NOT_ENTER_ROOM = 10000001;
    /**
     * 未到自己出牌的顺序
     */
    public static final int NOT_CORRECT_TURN = 10000002;
    /**
     * 筹码不足无法坐下
     */
    public static final int CHIPS_NOT_ENOUGH_CAN_NOT_SEAT_DOWN = 10000003;
    /**
     * 座位已经有人，无法坐下
     */
    public static final int CAN_NOT_SET_AUTO_MAKING_UP = 10000004;
    /**
     * 游戏进行中无法站起
     */
    public static final int GAMING_PLAYING_CAN_NOT_STAND_UP = 10000005;
    /**
     * 金币不足无法进入
     */
    public static final int CAN_NOT_IN_ROOM_BY_CHIP =10000006;
    /**
     * 房间满了
     */
    public static final int ROOM_IS_FULL = 10000007;
	/**
	 * 商城列表错误
	 */
    public static final int SHOP_LIST_IS_ERROR = 20000001;
    /**
     * 获取其他玩家信息失败
     */
    public static final int GET_OTHER_INFO_IS_ERROR = 20000002;
    /**
     * 没有找到用户
     */
    public static final int LOGIN_NOT_FIND_USER     = 90000001;
}
