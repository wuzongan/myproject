package com.kunlun.poker.center.service;

import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.domain.config.AttainmentConfig;
import com.kunlun.poker.domain.PokerHand;

public interface AttainmentService {
    //每日登陆
    static final int ATT_EVEREYDAYLOGIN = 1;
    //上传头像
    static final int ATT_UPLOADICON = 2;
    //链接fb账户
    static final int ATT_LINKFB = 3;
    //邀请1名FB好友
    static final int ATT_INVITEFBFIREND =4;
    //分享一次FB
    static final int ATT_SHAREFB = 5;
    //每隔4小时领取一次筹码
    static final int ATT_FOURINTERVARGETREWARD = 6;
    //赢得一局xx游戏
    static final int ATT_WINAGAME = 7;
    //玩一局xx游戏
    static final int ATT_PLAYAGAME = 8;
    //玩了xx局游戏
    static final int ATT_PLAYGAME = 9;
    //牌型成就
    static final int ATT_POKERHAND = 10;
    
    //登录
    void trrigerlogin(User user, long lastLoginTime);
    //游戏成就
    void trrigerGameAtt(User user, int roomId, boolean isWin);
    //免费筹码
    boolean trrigerGetbankrollAtt(User user);
    //牌型成就
    void trrigerPokerHandAtt(User user, int roomId, PokerHand pokerHand);
    //邀请好友成就
    void trrigerInviterFriendAtt(User user);
    
    void exit(User user);
    
    int getNextTimeOfGetBankroll();
    
    int getNextTimeOfGetBankroll(User user);
    
    AttainmentConfig getAttainmentConfig(int type);
    
    String getAttainmentStr(int userId);
    
    void stop();
}
