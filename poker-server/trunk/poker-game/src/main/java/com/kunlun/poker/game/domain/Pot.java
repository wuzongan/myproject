package com.kunlun.poker.game.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.ColorSuit;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.domain.Simplifiable;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.util.DataUtil;

/**
 * 底池
 *
 * @author Administrator
 */
public class Pot implements Simplifiable<Integer> {

	private final HashMap<Seat, Long> contributionMap;
	private volatile int stake;
	private static final Comparator<Seat> cardsSeatComparator = new CardsSeatComparator();
	private final Comparator<Seat> StakeSeatComparator;
	private static final Logger logger = LoggerFactory.getLogger(Pot.class);
	private List<Seat> lastWinSeats;

	public List<Seat> getLastWinSeats() {
		return lastWinSeats;
	}

	/*
	 * 构造函数初始化一个Hashmap<Seat, Pot>
	 */
	public Pot() {
		contributionMap = new HashMap<>();
		StakeSeatComparator = new StakeSeatComparator(contributionMap);
	}

	/**
	 * 返回底池堆数
	 *
	 * @return the stake
	 */
	public int getStake() {
		return stake;
	}

	/**
	 * 贡献筹码
	 *
	 * @param seat
	 * @param stake
	 *            the stake to set
	 */
	public void capture(Seat seat, long stake) {
		long addend;
		long seatStake = seat.getStake();
		if (seatStake > stake) {
			seat.setStake(seatStake - stake);
			addend = stake;
		} else {
			seat.setStake(0);
			addend = seatStake;
		}

		Long origStake = contributionMap.get(seat);
		if (origStake != null) {
			contributionMap.put(seat, origStake + addend);
		} else {
			contributionMap.put(seat, addend);
		}
		this.stake += addend;

	}

	public void capture(Seat seat) {
		capture(seat, seat.getStake());
	}

	/**
	 * 清空操作
	 */
	public void clear() {
		stake = 0;
		contributionMap.clear();
		lastWinSeats = null;
	}

	public boolean isEmpty() {
		return contributionMap.isEmpty();
	}

	/**
	 * 分筹码
	 */
	public void divideStake() {
		// 按照牌的大小排序
		List<Seat> seats = new ArrayList<>(contributionMap.keySet());
		seats.sort(cardsSeatComparator);

		List<Seat> winSeats = new ArrayList<>();
		Seat maxSeat = seats.remove(0);
		winSeats.add(maxSeat);

		// 判断是否出现其他人牌型相同的情况
		for (Seat seat : seats) {
			if (cardsSeatComparator.compare(seat, maxSeat) != 0) {
				break;
			}
			winSeats.add(seat);
		}
		int equationalStake = stake / winSeats.size();
		for (Seat seat : winSeats) {
			Player player = seat.getPlayer();
			player.setWin(true);
			player.setBankroll(player.getBankroll() + equationalStake);
			seat.setWinCount(equationalStake);
			logger.debug(player + "获得筹码" + equationalStake);
			
//			if(player.isRobot()){
//			    Robot robot = (Robot) player;
//			    robot.winCount++;
//			    
//			}
		}
		lastWinSeats = winSeats;
	}

	/**
	 * 收取服务费，亮牌
	 */
	public void serviceCharge(float coefficient) {
		List<Seat> seats = new ArrayList<>(contributionMap.keySet());
		seats.sort(StakeSeatComparator);

		long firstStake = contributionMap.get(seats.get(0));
		long totalStake = 0;
		if (seats.size() == 1) {
			return;
		} else {
			long secondStake = contributionMap.get(seats.get(1));
			long dutyFree = firstStake - secondStake;
			for (Seat seat : seats) {
				// 退出座位的不参与服务费计算
				if (seat.getPlayer() != null) {
					Desk desk = seat.getDesk();
					Long seatStake = contributionMap.get(seat);
					if (seat == seats.get(0)) {
						seat.getPlayer().setRevenue(
								(seatStake - dutyFree) * coefficient);
						long serviceCharge = (int) Math
								.floor((seatStake - dutyFree) * coefficient);
						stake = (int) (stake - Math
								.floor((seatStake - dutyFree) * coefficient));

						desk.setServiceBankroll(desk.getServiceBankroll()
								+ serviceCharge);
						totalStake = +serviceCharge;
						logger.debug("抽取玩家" + seat + "服务费为" + serviceCharge);
						//desk.systemServiceBankrolls = desk.systemServiceBankrolls + serviceCharge;
					} else {
						seat.getPlayer().setRevenue(stake * coefficient);
						int serviceCharge = (int) Math.floor(seatStake
								* coefficient);
						stake = (int) (stake - Math.floor(seatStake
								* coefficient));

						desk.setServiceBankroll(desk.getServiceBankroll()
								+ serviceCharge);
						totalStake = +serviceCharge;
						logger.debug("抽取玩家" + seat + "服务费为" + serviceCharge);
						//desk.systemServiceBankrolls = desk.systemServiceBankrolls + serviceCharge;
					}
				}
			}
		}
		LogClient.logDrawChip(totalStake);
	}

	@Override
	public Integer simplify() {
		return stake;
	}

	public static Integer[] simpliyfyAll(Pot[] pots, int number) {
		Integer[] simplifiedPots = new Integer[number];
		DataUtil.simplifyAll(pots, simplifiedPots);
		return simplifiedPots;
	}

	public List<Seat> getPotSeat() {
		return new ArrayList<>(contributionMap.keySet());
	}

	public boolean isContributed(Seat seat) {
		return contributionMap.containsKey(seat);
	}

	public static void main(String[] args) {
		System.out.println(
				PokerHand.ONE_PAIR.compare(
						new Card[] {
								new Card(ColorSuit.SPADE, 9), 
								new Card(ColorSuit.HEART, 9),
								new Card(ColorSuit.HEART, 1),
								new Card(ColorSuit.SPADE, 8),
								new Card(ColorSuit.DIAMOND, 7) },

		new Card[] { new Card(ColorSuit.SPADE, 9),
				new Card(ColorSuit.HEART, 9), new Card(ColorSuit.CLUB, 11),
				new Card(ColorSuit.SPADE, 8), new Card(ColorSuit.HEART, 5) }));
	}
}

/**
 *
 * @author kl
 */
class StakeSeatComparator implements Comparator<Seat> {

	private final HashMap<Seat, Long> contributionMap;

	StakeSeatComparator(HashMap<Seat, Long> contributionMap) {
		this.contributionMap = contributionMap;
	}

	@Override
	public int compare(Seat o1, Seat o2) {
		return (int)(contributionMap.get(o2) - contributionMap.get(o1));
	}
}

/**
 *
 * @author kl
 */
class CardsSeatComparator implements Comparator<Seat> {

	@Override
	public int compare(Seat seat1, Seat seat2) {
		if (seat1.noCards()) {
			return 1;
		}

		if (seat2.noCards()) {
			return -1;
		}

		// 1号和2号比牌的结果
		return seat2.getCardValue().compareTo(seat1.getCardValue());
	}
}
