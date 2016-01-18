package com.kunlun.poker.game.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.GameType;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.domain.Simplifiable;
import com.kunlun.poker.util.DataUtil;

public class Seat implements Simplifiable<Map<String, Object>> {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(Seat.class);
	private int index;
	private Player player;
	private final Card[] startingHand;
	private CardValue cardValue;
	private volatile long stake;
	private final Desk desk;
	private ShowdownState showdownState;
	private int winCount;

	/**
	 * 初始化本桌座位数据，坐下动作
	 * 
	 * @param desk
	 * @param id
	 */
	public Seat(Desk desk, int index) {
		startingHand = new Card[desk.getRoom().getGameType()
				.getNumberOfStartingHands()];
		this.desk = desk;
		this.index = index;
	}

	public void setShowdownState(ShowdownState showDown) {
		this.showdownState = showDown;
	}

	public ShowdownState getShowdownState() {
		return showdownState;
	}

	public void clear() {
		setShowdownState(ShowdownState.DISCARDED);

		int startingHandCards = desk.getRoom().getGameType()
				.getNumberOfStartingHands();
		for (int i = 0; i < startingHandCards; i++) {
			startingHand[i] = null;
		}
		
		cardValue = null;
		winCount = 0;
	}

	/**
	 * 桌子位置
	 * 
	 * @return desk
	 */
	public Desk getDesk() {
		return desk;
	}

	/**
	 * 手牌牌型
	 * 
	 * @return cards
	 */
	public Card[] getStartingHand() {
		return startingHand;
	}

	public int getIndex() {
		return index;
	}

	/**
	 * 桌子的id
	 * 
	 * @return the id
	 */
	public int getId() {
		return index + 1;
	}

	/**
	 * 参与者
	 * 
	 * @return the Player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @param player
	 * 
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}

	/**
	 * @param stake
	 *            the stake to set
	 */
	public void setStake(long stake) {
		this.stake = stake;
	}

	/**
	 * 分界
	 * 
	 * @return the stake
	 */
	public long getStake() {
		return stake;
	}

	/**
	 * 赢的筹码
	 * 
	 * @return
	 */
	public int getWinCount() {
		return winCount;
	}

	public void setWinCount(int winCount) {
		this.winCount = winCount;
	}

	/**
	 * 押注，减去自己堆的筹码，增加座位堆得筹码 实际上的最后算钱操作都走bet流程
	 * 
	 * @param number
	 */
	public void bet(long number) {
		// @TODO: 这里需要断言
		player.setBankroll(player.getBankroll() - number);
		this.setStake(stake + number);
	}

	/**
	 * 是否弃牌
	 * 
	 * @return
	 */
	public boolean noCards() {
		return startingHand[0] == null;
	}

	/**
	 * 是否allin
	 * 
	 * @return
	 */
	public boolean isAllIn() {
		return player != null && player.getBankroll() == 0;
	}

	/**
	 * 弃牌动作
	 */
	public void fold() {
		startingHand[0] = null;
		startingHand[1] = null;
	}
	/**
	 * 生成最大的牌
	 */
	public void computeCardValue() {
		Card[] communityCards = desk.getCommunityCards();
		if (desk.getRoom().getGameType() == GameType.OMAHA) {
			cardValue = PokerHand.determinOmaha(startingHand, communityCards);
		} else {
			cardValue = PokerHand.determine(startingHand, communityCards);
		}
		player.checkAndSetPokerhand(cardValue);
		
//		if(player.isRobot()){
//		    Robot robot = (Robot) player;
//		    if(robot.cardMap.get(cardValue.getPokerHand())  == null){
//		        robot.cardMap.put(cardValue.getPokerHand(), 1);
//		    }
//		}
	}

	/**
	 * 最大牌
	 * 
	 * @return
	 */
	public CardValue getCardValue() {
		return cardValue;
	}

	@Override
	public String toString() {
		return getId() + "号位" + getPlayer() + "(" + stake + ")";
	}

	@Override
	public Map<String, Object> simplify() {
		return simplify(true);
	}

	private Map<String, Object> simplify(boolean withPlayer) {
		Map<String, Object> simplified = new HashMap<>();
		simplified.put("id", getId());
		if (player != null) {
			simplified.put("stake", stake);
			simplified.put("active", DataUtil.booleanToInt(desk.isPlaying(player)));

			if (withPlayer) {
				simplified.put("player", player.simplify());
			} else {
				simplified.put("bankroll", player.getBankroll());
			}
		}

		return simplified;
	}

	public static Map<String, Object>[] simplifyAll(Seat[] seats) {
		return simplifyAll(seats, true, true);
	}

	public static Map<String, Object>[] simplifyAll(Seat[] seats,
			boolean ignornTheEmpty) {
		return simplifyAll(seats, ignornTheEmpty, true);
	}

	@SuppressWarnings("unchecked")
	public static Map<String, Object>[] simplifyAll(Seat[] seats,
			boolean ignornTheEmpty, boolean withPlayer) {
		Map<String, Object>[] simplifiedSeats;

		if (ignornTheEmpty) {
			List<Map<String, Object>> simplifiedSeatList = new ArrayList<>();
			for (Seat seat : seats) {
				if (seat.getPlayer() != null) {
					simplifiedSeatList.add(seat.simplify(withPlayer));
				}
			}

			simplifiedSeats = new Map[simplifiedSeatList.size()];
			simplifiedSeatList.toArray(simplifiedSeats);
		} else {
			simplifiedSeats = new Map[seats.length];
			DataUtil.simplifyAll(seats, simplifiedSeats);
		}

		return simplifiedSeats;
	}

	public static void main(String[] args) {
	}
}
