package com.kunlun.poker.center.domain;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import com.googlecode.canoe.core.session.Role;
import com.kunlun.poker.center.domain.config.LevelConfig;
import com.kunlun.poker.center.system.ConfigData;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.ColorSuit;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.rmi.dto.PlayerDTO;
import com.kunlun.poker.util.DataUtil;

/**
 * 角色头像、昵称、游戏Id、等级、称号、经验条、当前货币数量、救济金、新手盲注
 * 
 * @author Administrator
 * 
 */
public class User extends Actor<User> implements Role {

    private int exp;
    private int fb;
    private float revenue;
    private final Map<Integer, PlayerDTO> roomPlayerMap;
    private long lastLoginTime;
    private String bestCards;
    private PokerHand pokerHand;
    private Card[] bestCardArray;
    private String noticeInfo;

	/**
     * User构造函数
     * 
     */
    public User() {
        roomPlayerMap = new ConcurrentHashMap<Integer, PlayerDTO>();
    }
    /**
     * 获取经验
     * 
     * @return the exp
     */
    public String getPortrait(){
    	setPortrait();
		return portrait;
    }
    
    public void setPortrait(){
		String firstFile = DataUtil.firstFolderAddress(id);
		String secondFile = DataUtil.secondFolderAddress(id);
		portrait= firstFile + File.separator + secondFile + File.separator +id+".jpg";
    }
    
    public int getExp() {
        return exp;
    }

    /**
     * 设置经验
     * 
     * @param exp
     *            the exp to set
     */
    public void setExp(int exp) {
        boolean flag = this.exp==0?true:false;
        if(this.exp < exp){
            this.exp = exp;
            if(!flag){
                levelUp(exp);
            }
        }
    }
    
	private void levelUp(int exp){
        if (exp == 0) {
            return;
        }
        if (ConfigData.getLevelconfigmap().isEmpty()) {
            return;
        }
        int maxLevel = ConfigData.getLevelconfigmap().size();
        if (this.level == maxLevel) {
            return;
        }
        boolean change = false;
        int newLevel = this.level + 1;
        LevelConfig levelConfig = ConfigData.getLevelConfig(newLevel);
        while (levelConfig.getExp() < exp) {
            this.setLevel(newLevel);
            change = true;
            ConfigData.givePrize(this, levelConfig.getPrizeId());
            if (newLevel != maxLevel) {
                newLevel++;
            }
            if (newLevel > maxLevel) {
                break;
            }
            levelConfig = ConfigData.getLevelConfig(newLevel);
        }
        if (change) {
            LogClient.logLevelUpFreeChips(getId(),
                            ConfigData.getPrizeConfig(levelConfig.getPrizeId())
                                    .getBankroll());
        }
        
        dispatchEvent(new UserEvent(UserEvent.LEVEL_CHANGED));
    }
    /**
     * 设置等级
     * 
     * @param level
     *            the level to set
     */
    public void setLevel(int level) {
        if (this.level != level) {
            this.level = level;
            //等级提升 主动推消息给前端
        }
    }

    /**
     * 获取货币数量
     * 
     * @return the bankroll
     */

    private long totalBankrool;

    public long getTotalBankroll() {
        if (totalBankrollChanged) {
            long value = bankroll;
            for (PlayerDTO player : roomPlayerMap.values()) {
                value += player.getBankroll();
            }
            totalBankrool = value;
            totalBankrollChanged = false;
        }

        return totalBankrool;
    }

    /**
     * 设置货币数量
     * 
     * @param bankroll
     *            the bankroll to set
     */
    public void setBankroll(long bankroll) {
    	//logger.error(this + "的钱由" + this.bankroll + "变成" + bankroll, new Exception());
        this.bankroll = bankroll;
        totalBankrollChanged = true;
    }

    /**
     * 个人抽水总量
     */
    public float getRevenue() {
        return revenue;
    }

    /**
     * 设置个人抽水
     */
    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }
    
    /***
     * 是否绑定fb
     * @return
     */
    public int getFb() {
        return fb;
    }

    public void setFb(int fb) {
        this.fb = fb;
    }

    public void setCardNum(int cardNum) {
        if(this.cardNum < cardNum){
            this.cardNum = cardNum;
        }
    }

    /**
     * 赢率
     * 
     * @return
     */
    public float getWinRate() {
        if(cardNum==0){
            return 0f;
        }
        return ((float) winCardNum) / cardNum;
    }
    
    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        if(this.lastLoginTime < lastLoginTime){
            this.lastLoginTime = lastLoginTime;
        }
    }
    
    public String getBestCards() {
        if (!StringUtils.isEmpty(bestCardArray) && bestCardArray.length == 5
                && this.pokerHand != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(this.pokerHand.ordinal() + ":");
            int size = bestCardArray.length;
            for (int idx = 0; idx < size; idx++) {
                Card card = bestCardArray[idx];
                if (card == null)
                    continue;
                sb.append(card.getSuit().simplify()).append("_")
                        .append(card.getFace());
                if (idx != size - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
        if(!StringUtils.isEmpty(bestCards)){
            String[] strArr = bestCards.split(":");
            if(strArr.length==2){
                return strArr[1];
            }
        }
        return bestCards;
    }
    
    /**
     * 最大的手牌
     * @return
     */
    public String getBestHand(){
        if (!StringUtils.isEmpty(bestCardArray) && bestCardArray.length == 5
                && this.pokerHand != null) {
            StringBuilder sb = new StringBuilder();
            int size = bestCardArray.length;
            for (int idx = 0; idx < size; idx++) {
                Card card = bestCardArray[idx];
                if (card == null)
                    continue;
                sb.append(card.getSuit().simplify()).append("_")
                        .append(card.getFace());
                if (idx != size - 1) {
                    sb.append(",");
                }
            }
            return sb.toString();
        }
        if(!StringUtils.isEmpty(bestCards)){
            String[] strArr = bestCards.split(":");
            if(strArr.length==2){
                return strArr[1];
            }
        }
        return "";
    }

    public void setBestCards(String bestCards) {
         this.bestCards = bestCards;
         if(!StringUtils.isEmpty(this.bestCards)){
             String[] strs = this.bestCards.split(":");
             if (strs.length != 2) {
                 return;
             }
             this.setPokerHand(PokerHand.getPokerHand(Integer.valueOf(strs[0])));
             strs = strs[1].split(",");
             this.bestCardArray = new Card[strs.length];
             for (int idx = 0; idx < strs.length; idx++) {
                 String[] _str = strs[idx].split("_");
                 int colorSuitId = Integer.valueOf(_str[0]);
                 int face = Integer.valueOf(_str[1]);
                 this.bestCardArray[idx] = new Card(
                         ColorSuit.getColorSuitById(colorSuitId), face);
             }
         }
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj != null && (obj instanceof User)
                && ((User) obj).getId() == id;
    }

    @Override
    public int hashCode() {
        return id;
    }
    
	public void commitChanged()
    {
    	dispatchEvent(new UserEvent(UserEvent.ATTR_CHANGED));
    }

    private volatile boolean totalBankrollChanged;

    public void putPlayer(PlayerDTO player) {
        int roomId = player.getRoomId();
        PlayerDTO oldPlayer = roomPlayerMap.get(roomId);
        if (oldPlayer != null) {
            long winBankroll = player.getBankroll() - oldPlayer.getBankroll();
            this.setSingleWinBankroll(winBankroll);
        }
        roomPlayerMap.put(player.getRoomId(), player);
        totalBankrollChanged = true;
        this.setExp(exp + player.getExp());
        this.setDealerTips(dealerTips + player.getDealerTips());
        this.setCardNum(cardNum + 1);
        if(player.isWin()){
            this.setWinCardNum(winCardNum + 1);
        }
        
        if(player.getPokerHand() != null && player.getBestCards() != null){
            this.setNewBestCardArray(player.getPokerHand(), player.getBestCards());
        }
    }
    
    public void removePlayer(PlayerDTO player) {
        roomPlayerMap.remove(player.getRoomId());
        totalBankrollChanged = true;
    }
    
    public PlayerDTO getPlayerByRoomId(int roomId){
        return roomPlayerMap.get(roomId);
    }

    private volatile long lastGetBankrollTime;

    public long getLastGetBankrollTime() {
        return lastGetBankrollTime;
    }

    public void setLastGetBankrollTime(long lastGetBankrollTime) {
        this.lastGetBankrollTime = lastGetBankrollTime;
    }

    private void setNewBestCardArray(PokerHand pokerHand, Card[] bestCardArray){
        if(this.pokerHand != null && this.bestCardArray != null){
            for(PlayerDTO pd : this.roomPlayerMap.values()){
                PokerHand newPokerHand = pd.getPokerHand();
                Card[] newBestCardArray = pd.getBestCards();
                if(newPokerHand != null && newBestCardArray != null){
                    int ret = this.pokerHand.compareTo(newPokerHand);
                    if(ret == 0){
                        ret = this.pokerHand.compare(this.bestCardArray, newBestCardArray);
                        if(ret < 0){
                            this.setPokerHand(newPokerHand);
                            this.setBestCardArray(newBestCardArray);
                        }
                    }else if(ret > 0){
                        this.setPokerHand(newPokerHand);
                        this.setBestCardArray(newBestCardArray);
                    }
                }
            }
        }else{
            this.setPokerHand(pokerHand);
            this.setBestCardArray(bestCardArray);
        }
    }
    
    public PokerHand getPokerHand() {
        return pokerHand;
    }

    public void setPokerHand(PokerHand pokerHand) {
        this.pokerHand = pokerHand;
    }

    public Card[] getBestCardArray() {
        return bestCardArray;
    }

    public void setBestCardArray(Card[] bestCardArray) {
        this.bestCardArray = bestCardArray;
    }

    /**
     * 已查看/未查看公告信息
     * @return
     */
    public String getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(String noticeInfo) {
        this.noticeInfo = noticeInfo;
    }
    
	public void buyChips(int addbankRoll){
    	dispatchEvent(new UserEvent(UserEvent.BUY_CHIPS));
    	dispatchEvent(new UserEvent(UserEvent.TOOL_TIP, addbankRoll));
    }
    
    //是否每天第一次登录
    private volatile boolean firstLoginOfEveryDay;

    public boolean isFirstLoginOfEveryDay() {
        return firstLoginOfEveryDay;
    }

    public void setFirstLoginOfEveryDay(boolean firstLoginOfEveryDay) {
        this.firstLoginOfEveryDay = firstLoginOfEveryDay;
    }
}
