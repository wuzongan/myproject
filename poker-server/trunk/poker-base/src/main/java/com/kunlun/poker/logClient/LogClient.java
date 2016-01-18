package com.kunlun.poker.logClient;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.Config;
import com.kunlun.poker.log.LogConstants;
import com.kunlun.poker.log.LogMessage;

public class LogClient {
    private static final Logger logger = LoggerFactory
            .getLogger(LogClient.class);
    private static LogClientService logClientService;
    
    public static void setLogClientService(final LogClientService service){
        logClientService = service;
    }
    
    public static void sendLogMessage(LogMessage logMessage){
        if(!Config.getInstance().isLogEnabled()){
            return;
        }
        try {
            logClientService.sendLogMessage(logMessage);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
    
/*    *//***
     * 玩家筹码量进出统计
     * @param userId
     * @param changeBankroll
     * @param totalBankroll
     * @return
     *//*
    public static LogMessage buildTlUserBankrollChangeLogMessage(int userId, int changeBankroll, int totalBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(5);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_BANKROLL_CHANGE_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.CHANGE_BANKROLL, String.valueOf(changeBankroll));
        map.put(LogConstants.TOTAL_BANKROLL, String.valueOf(totalBankroll));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 一段时间筹码总量统计
     * @param totalBankroll
     * @return
     *//*
    public static LogMessage buildTlTotalBankrollLogMessage(int totalBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_TOTAL_BANKROLL_STATISTICS);
        map.put(LogConstants.TOTAL_BANKROLL, String.valueOf(totalBankroll));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * xx玩法xx房间牌桌带入筹码量统计
     * @param gameType
     * @param roomId
     * @param deskId
     * @param totalBankroll
     * @return
     *//*
    public static LogMessage buildTlGameRoomDeskLogMessage(GameType gameType, int roomId, int deskId, int totalBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(5);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_GAME_ROOM_DESK_BK_STATISTICS);
        map.put(LogConstants.TOTAL_BANKROLL, String.valueOf(totalBankroll));
        map.put(LogConstants.GAME_TYPE, String.valueOf(gameType.ordinal()));
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.DESK_ID, String.valueOf(deskId));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 免费筹码领取统计
     * @param userId
     * @param freeBankroll
     * @return
     *//*
    public static LogMessage buildTlUserGetFreeBankrollLogMessage(int userId, int freeBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_GET_FREEBANKROLL_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.FREE_BANKROLL, String.valueOf(freeBankroll));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 玩家在线统计
     * @param userId
     * @return
     *//*
    public static LogMessage buildTlUserOnlineLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_ONLINE_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 用户统计表
     * @param userId
     * @param userName
     * @param fb
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsLogMessage(int userId, String userName, int fb){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.USERNAME, userName);
        map.put(LogConstants.FB, String.valueOf(fb));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录玩家等级
     * @param userId
     * @param level
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsLevelLogMessage(int userId, int level){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.LEVEL, String.valueOf(level));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录手动选择次数
     * @param userId
     * @param handChooseCount
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsHandChooseCountLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.HAND_CHOOSECOUNT, String.valueOf(1));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录快速开始次数
     * @param userId
     * @param quickStartCount
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsQuickStartCountLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.QUICK_STARTCOUNT, String.valueOf(1));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录是否绑定fb
     * @param userId
     * @param fb
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsFbLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.FB, String.valueOf(1));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录是否邀请好友
     * @param userId
     * @param inviteFriend
     * @return
     *//*
//    public static LogMessage buildTlUserStatisticsInviteFriendLogMessage(int userId, int inviteFriend){
//        LogMessage logMessage = new LogMessage();
//        Map<String, String> map = new HashMap<String, String>(2);
//        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
//        map.put(LogConstants.INVITE_FRIEND, String.valueOf(inviteFriend));
//        logMessage.setValue(map);
//        return logMessage;
//    }
    
    *//***
     * 记录邀请好友次数
     * @param userId
     * @param inviteFriendCount
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsInviteFriendCountLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.INVITE_FRIEND_COUNT, String.valueOf(1));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录是否分享fb
     * @param userId
     * @param shareFb
     * @return
     *//*
//    public static LogMessage buildTlUserStatisticsShareFbLogMessage(int userId, int shareFb){
//        LogMessage logMessage = new LogMessage();
//        Map<String, String> map = new HashMap<String, String>(2);
//        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
//        map.put(LogConstants.SHARE_FB, String.valueOf(shareFb));
//        logMessage.setValue(map);
//        return logMessage;
//    }
    
    *//***
     * 记录分享fb次数
     * @param userId
     * @param shareFbCount
     * @return
     *//*
    public static LogMessage buildTlUserStatisticsShareFbCountLogMessage(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_STATISTICS);
        map.put(LogConstants.SHARE_FB_COUNT, String.valueOf(1));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 牌局数据统计
     * @param gameType
     * @param gameId
     * @param roomId
     * @param deskId
     * @return
     *//*
    public static LogMessage buildTlDeskStatisticsLogMessage(GameType gameType, long gameId, int roomId, int deskId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(5);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_DESK_STATISTICS);
        map.put(LogConstants.GAME_TYPE, String.valueOf(gameType.ordinal()));
        map.put(LogConstants.GAME_ID, String.valueOf(gameId));
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.DESK_ID, String.valueOf(deskId));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 牌局流水记录
     * @param userId
     * @param gameId
     * @param betCount
     * @param winCount
     * @param pokerHand
     * @return
     *//*
    public static LogMessage buildTlDeskUserStatisticsLogMessage(int userId, long gameId, int betCount, int winCount, PokerHand pokerHand){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(6);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_DESK_USER_STATISTICS);
        map.put(LogConstants.GAME_ID, String.valueOf(gameId));
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.BET_COUNT, String.valueOf(betCount));
        map.put(LogConstants.WIN_COUNT, String.valueOf(winCount));
        map.put(LogConstants.POKERHAND, String.valueOf(pokerHand.ordinal()));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录玩家房间参与次数
     * @param userId
     * @param gameType
     * @param roomId
     * @return
     *//*
    public static LogMessage buildTlUserRoomStatisticsLogMessage(int userId, GameType gameType, int roomId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_USER_ROOM_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.GAME_TYPE, String.valueOf(gameType.ordinal()));
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        logMessage.setValue(map);
        return logMessage;
    }
    
    *//***
     * 记录xx玩法xx房间用户分布
     * @param userId
     * @param roomId
     * @param gameType
     * @return
     *//*
    public static LogMessage buildTlGameRoomStatisticsLogMessage(int userId, int roomId, GameType gameType){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_TABLE_GAME_ROOM_STATISTICS);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.GAME_TYPE, String.valueOf(gameType.ordinal()));
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        logMessage.setValue(map);
        return logMessage;
    }*/
    
    /******************************************************以上废弃**********************************************************/
    
    public static void logUserStatistics(int roomId, int userId, int selectType){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_USER_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.SELECT_TYPE, String.valueOf(selectType));
        logMessage.setValue(map);
        sendLogMessage(logMessage);
    }
    
    public static void logRoomChip(int roomId, int totalChips, int averageChips){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_ROOM_CHIPS_STATIC);
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.TOTAL_CHIPS, String.valueOf(totalChips));
        map.put(LogConstants.AVERAGE_CHIPS, String.valueOf(averageChips));
        logMessage.setValue(map);
		sendLogMessage(logMessage);    	
    }

    public static void logShareFbCount(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_FB_FRIEND_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.SHARE_FB_COUNT, String.valueOf(1));
        logMessage.setValue(map);
        sendLogMessage(logMessage);
    }

    
    public static void logLoginFreeChips(int userId, int getCount){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_LOGIN_FREE_CHIPS_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.LOGIN_FREE_CHIPS, String.valueOf(getCount));
        logMessage.setValue(map);
		sendLogMessage(logMessage);
    }
    
    public static void logAchievementFreeChips(int userId, int getCount){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_ACHIEVEMENT_FREE_CHIPS_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.ACHIEVEMENT_FREE_CHIPS, String.valueOf(getCount));
        logMessage.setValue(map);
		sendLogMessage(logMessage);
    }
    
    public static void logLevelUpFreeChips(int userId, int getCount){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_LEVELUP_FREE_CHIPS_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.LEVELUP_FREE_CHIPS, String.valueOf(getCount));
        logMessage.setValue(map);
		sendLogMessage(logMessage);
    }
    
    public static void logSaleChip(int saleCount){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_SALE_CHIPS_STATIC);
        map.put(LogConstants.SALE_CHIPS, String.valueOf(saleCount));
        logMessage.setValue(map);
		sendLogMessage(logMessage);
    }
    
    public static void logDrawChip(long drawCount){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_DRAW_CHIPS_STATIC);
        map.put(LogConstants.DRAW_CHIPS, String.valueOf(drawCount));
        logMessage.setValue(map);
		sendLogMessage(logMessage);
    }
    
    public static void logDealerChips(long smallBlindBets){
    	LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(2);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_DEALER_CHIPS_STATIC);
        map.put(LogConstants.DEALER_CHIPS, String.valueOf(smallBlindBets));
        logMessage.setValue(map);
		sendLogMessage(logMessage);    	
    }
    
    public static void logInviteFriend(int userId){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(3);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_FB_FRIEND_STATIC);
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.INVITE_FRIEND_COUNT, String.valueOf(1));
        logMessage.setValue(map);
        sendLogMessage(logMessage);
    }
    
    public static void logGameTypeUserOnline(int roomId, int userId, long startTime,long endTime, long userBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(6);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_GAMETYPE_USER_STATIC);
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.USERID, String.valueOf(userId));
        map.put(LogConstants.TOTAL_BANKROLL, String.valueOf(userBankroll));
        map.put(LogConstants.START_TIME, String.valueOf(TimeUnit.MILLISECONDS.toSeconds(startTime)));
        map.put(LogConstants.END_TIME, String.valueOf(TimeUnit.MILLISECONDS.toSeconds(endTime)));
        logMessage.setValue(map);
        sendLogMessage(logMessage);
    }
    
    public static void logRoomBankroll(int roomId, long bankroll, long serviceBankroll){
        LogMessage logMessage = new LogMessage();
        Map<String, String> map = new HashMap<String, String>(4);
        map.put(LogConstants.TABLE, LogConstants.TL_CSV_ROOM_BANKROLL_STATIC);
        map.put(LogConstants.ROOM_ID, String.valueOf(roomId));
        map.put(LogConstants.TOTAL_BANKROLL, String.valueOf(bankroll));
        map.put(LogConstants.SERVICE_BANKROLL, String.valueOf(serviceBankroll));
        logMessage.setValue(map);
        sendLogMessage(logMessage);
    }
    
    
}
