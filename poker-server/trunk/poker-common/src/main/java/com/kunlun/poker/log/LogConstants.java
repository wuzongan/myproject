package com.kunlun.poker.log;

public interface LogConstants {

    static final String TABLE = "table";
    static final String USERID = "userId";
    static final String USERNAME = "userName";
    
    static final String CHANGE_BANKROLL = "changeBankroll";
    static final String TOTAL_BANKROLL = "totalBankroll";
    static final String FREE_BANKROLL = "freeBankroll";
    
    static final String  GAME_TYPE = "gameType"; //玩法
    static final String ROOM_ID = "roomId";
    static final String DESK_ID = "deskId";
    static final String GAME_ID = "gameId";//牌局id
    
    static final String LEVEL = "level";
    static final String HAND_CHOOSECOUNT = "handChooseCount";
    static final String QUICK_STARTCOUNT = "quickStartCount";
    static final String FB = "fb";
    static final String INVITE_FRIEND = "inviteFriend";
    static final String INVITE_FRIEND_COUNT = "inviteFriendCount";
    static final String SHARE_FB = "shareFb";
    static final String SHARE_FB_COUNT = "shareFbCount";
    
    static final String GO_IN_COUNT = "goInCount";
    
    static final String BET_COUNT = "betCount";
    static final String WIN_COUNT = "winCount";
    static final String POKERHAND = "pokerHand";
    

    static final String TOTAL_CHIPS = "totalChips";
    static final String AVERAGE_CHIPS = "averageChips";

    static final String START_TIME = "startTime";
    static final String END_TIME = "endTime";

    
    static final String LOGIN_FREE_CHIPS = "loginFreeChips";
    static final String ACHIEVEMENT_FREE_CHIPS = "achievementFreeChips";
    static final String LEVELUP_FREE_CHIPS = "levelUpFreeChips";
    

    static final String SALE_CHIPS = "saleChips";
    static final String DRAW_CHIPS = "drawChips";
    static final String DEALER_CHIPS = "dealerChips";
    
    static final String HAPPEN_TIME = "happenTime";
    
 
    
//    /***
//     * 玩家时间筹码进出表
//     */
//    static final String TL_TABLE_USER_BANKROLL_CHANGE_STATISTICS = "tl_user_bankroll_change_statistics";
//    /***
//     * 筹码总量统计表
//     */
//    static final String TL_TABLE_TOTAL_BANKROLL_STATISTICS = "tl_total_bankroll_statistics";
//    /***
//     * 某玩法房间牌桌带入筹码统计表
//     */
//    static final String TL_TABLE_GAME_ROOM_DESK_BK_STATISTICS = "tl_game_room_desk_bk_statistics";
//    /***
//     * 某时间段玩家领取筹码统计表
//     */
//    static final String TL_TABLE_USER_GET_FREEBANKROLL_STATISTICS = "tl_user_get_freebankroll_statistics";
//    /***
//     * 在线用户表
//     */
//    static final String TL_TABLE_USER_ONLINE_STATISTICS = "tl_user_online_statistics";
//    /***
//     * 用户统计表
//     */
//    static final String TL_TABLE_USER_STATISTICS = "tl_user_statistics";
//    /***
//     * 用户房间统计表
//     */
//    static final String TL_TABLE_USER_ROOM_STATISTICS = "tl_user_room_statistics";
//    /***
//     * 玩法房间统计表
//     */
//    static final String TL_TABLE_GAME_ROOM_STATISTICS = "tl_game_room_statistics";
//    /***
//     * 牌局统计表
//     */
//    static final String TL_TABLE_DESK_STATISTICS = "tl_desk_statistics";
//    /***
//     * 牌局玩家统计表
//     */
//    static final String TL_TABLE_DESK_USER_STATISTICS = "tl_desk_user_statistics";
    
    
    /**############################以上废弃##############################################**/
    
    static final String SELECT_TYPE = "selectType";
    
    static final String SERVICE_BANKROLL = "service_bankroll";
    
    static final String TL_CSV_USER_STATIC = "tl_csv_user_statics";
    
    static final String TL_CSV_ROOM_CHIPS_STATIC = "tl_csv_room_chips_statics";
   
    static final String TL_CSV_FB_FRIEND_STATIC = "tl_csv_fb_friend_statics";

    static final String TL_CSV_LOGIN_FREE_CHIPS_STATIC = "tl_csv_login_free_chips_statics";
    
    static final String TL_CSV_ACHIEVEMENT_FREE_CHIPS_STATIC = "tl_csv_achievement_free_chips_statics";

    static final String TL_CSV_GAMETYPE_USER_STATIC = "tl_csv_gameType_user_onine_statics";
    
    static final String TL_CSV_LEVELUP_FREE_CHIPS_STATIC = "tl_csv_levelUp_free_chips_statics";
    
    static final String TL_CSV_SALE_CHIPS_STATIC = "tl_cvs_sale_chips_statics";
    
    static final String TL_CSV_DRAW_CHIPS_STATIC = "tl_cvs_draw_chips_statics";
    
    static final String TL_CSV_DEALER_CHIPS_STATIC = "tl_cvs_dealer_chips_statics";
    
    static final String TL_CSV_ROOM_BANKROLL_STATIC = "tl_csv_room_bankroll_statics";

}
