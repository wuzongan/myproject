package com.kunlun.poker.game.domain;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.event.EventDispatcherAdapter;
import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.Simplifiable;

/**
 * 参与者
 *
 * @author Administrator
 */
public class Player extends EventDispatcherAdapter<Player> implements Role,
		Simplifiable<Map<String, Object>> {
	private static final Logger logger = LoggerFactory.getLogger(Player.class);
	private int id;
	private String name;
	private long bankroll;
	private float revenue;
	private boolean newbie;
	private boolean agented;
	private boolean online;
	private boolean autoMakingUp;
	private boolean autoRushing;
	private boolean autoStandingUp;
	private Desk desk;
	private int exp;
	private boolean win;
	private CardValue bestCardValue;
	private long dealerTips;
	private long enterGameTime;
	private boolean robot;
	private String portrait;

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	@Override
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 获取资金
	 *
	 * @return the bankroll
	 */
	public long getBankroll() {
		return bankroll;
	}

	/**
	 * @param bankroll
	 *            the bankroll to set
	 */
	public void setBankroll(long stake) {
		this.bankroll = stake;
	}

	/**
	 * 获取新手盲注
	 *
	 * @return
	 */
	public boolean isNewbie() {
		return newbie;
	}

	/**
	 * 设置新手盲注
	 *
	 * @param newbie
	 */
	public void setNewbie(boolean newbie) {
		this.newbie = newbie;
	}

	/**
	 * 返回是否托管状态
	 *
	 * @return
	 */
	public boolean isAgented() {
		return agented;
	}

	/**
	 * 设置托管状态
	 *
	 * @param agented
	 */
	public void setAgented(boolean agented) {
		if (this.agented != agented) {
			this.agented = agented;

			dispatchEvent(new PlayerEvent(PlayerEvent.SWITCH_AGENT));
		}
	}

	@Override
	public String toString() {
		return "[玩家]" + getName() + "<" + bankroll + ">";
	}

	/**
	 * @return the online
	 */
	public boolean isOnline() {
		return online;
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

	/**
	 * @param online
	 *            the online to set
	 */
	public void setOnline(boolean online) {
		if (!online) {
			setAgented(true);
			logger.debug(toString() + "离线");
		}

		this.online = online;
	}

	@Override
	public Map<String, Object> simplify() {
		Map<String, Object> simplified = new HashMap<>();
		simplified.put("id", getId());
		simplified.put("name", getName());
		simplified.put("bankroll", bankroll);
		simplified.put("portrait", getPortrait());
		System.out.println("Player的头像"+ getPortrait());

		return simplified;
	}

	public boolean isAutoMakingUp() {
		return autoMakingUp;
	}

	public void setAutoMakingUp(boolean autoMakingUp) {
		this.autoMakingUp = autoMakingUp;
	}

	public boolean isAutoStandingUp() {
		return autoStandingUp;
	}

	public void setAutoStandingUp(boolean autoStandingUp) {
		this.autoStandingUp = autoStandingUp;
	}

	public Desk getDesk() {
		return desk;
	}

	public void setDesk(Desk desk) {
		this.desk = desk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean isWin) {
		this.win = isWin;
	}

	public void checkAndSetPokerhand(CardValue newCardValue) {
		if (newCardValue == null)
			return;
		if (getBestCardValue() == null || getBestCardValue().compareTo(newCardValue) < 0) {
			bestCardValue = newCardValue;
		}
	}

	/**
	 * 给荷官的小费总计
	 * 
	 * @return
	 */
	public long getDealerTips() {
		return dealerTips;
	}

	public void setDealerTips(long dealerTips) {
		this.dealerTips = dealerTips;
	}

	public long getEnterGameTime() {
		return enterGameTime;
	}

	public void setEnterGameTime(long enterGameTime) {
		this.enterGameTime = enterGameTime;
	}

	/**
	 * 判断带入是否充足
	 *
	 */
	public boolean isCarryEnought() {
		return getBankroll() >= desk.getRoom().getCarry();
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && (obj.getClass() == getClass())
				&& ((Player) obj).getId() == id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	public boolean isAutoRushing() {
		return autoRushing;
	}

	public void setAutoRushing(boolean autoRushing) {
		this.autoRushing = autoRushing;
	}

	public boolean isRobot() {
		return robot;
	}

	public void setRobot(boolean robot) {
		this.robot = robot;
	}

	public CardValue getBestCardValue() {
		return bestCardValue;
	}
}
