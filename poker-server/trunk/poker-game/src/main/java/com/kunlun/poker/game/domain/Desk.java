package com.kunlun.poker.game.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.mina.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.canoe.event.EventDispatcherAdapter;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.Deck;
import com.kunlun.poker.domain.GameType;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.domain.Simplifiable;
import com.kunlun.poker.game.domain.actions.AllIn;
import com.kunlun.poker.game.domain.actions.Check;
import com.kunlun.poker.game.domain.actions.Fold;
import com.kunlun.poker.game.service.PlayerService;
import com.kunlun.poker.game.service.RobotService;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.service.SettingService;
import com.kunlun.poker.util.RandomUtil;

public class Desk extends EventDispatcherAdapter<Desk> implements
		Simplifiable<Map<String, Object>> {

	public static final int NUMBER_OF_COMMUNITY_CARDS = 5;

	public static final int START_SECONDS = 2;
	public static final int AGENT_SECONDS = 15;
	public static final float DIVIDE_SECONDS = 2.5f;
	public static final int SHUTDOWN_SECONDS = 4;
	public static final float DISPOSE_POT_DELAY = .5f;
	public static final float DISPOSE_POT_SECONDS = 1.5f;
	public static final int DEAL_CARD_SECONDS = 1;
	public static final float ACTION_DURATION = .5f;
	public static final int COMPARE_TIME = 1;
	public static final float PRE_FLOP_DELAY = 3.5f;
	public static final int ANIMATION_SINGLE_TIME = 4;

	private static final Logger logger = LoggerFactory.getLogger(Desk.class);
	private static final AtomicInteger deskIdGenerator = new AtomicInteger();

	public Desk(Room room) {
		locked = new AtomicBoolean();
		this.room = room;
		id = room.getId() << 8 | deskIdGenerator.incrementAndGet();

		deck = new Deck(room.getGameType() == GameType.ROYAL);
		players = new ConcurrentHashSet<>();
		robots = new ConcurrentHashSet<>();

		int numberOfSeats = room.getType();

		seats = new Seat[numberOfSeats];
		pots = new Pot[numberOfSeats];
		potStakes = new int[numberOfSeats];

		for (int i = 0; i < numberOfSeats; i++) {
			seats[i] = new Seat(this, i);
			pots[i] = new Pot();
		}

		communityCards = new Card[NUMBER_OF_COMMUNITY_CARDS];
		playings = new ArrayList<Player>();
	}

	private Scheduler scheduler;
	private PlayerService playerService;

	private RobotService robotService;
	private Collection<Player> robots;
	private int sameInRobots;

//	 //临时代码
//	 //系统抽成筹码数
//	 public int systemServiceBankrolls;
//	 //玩了多少局
//	 public int systemPlayCount=-1;
//	
//	 public void consoleRobotLog(){
//	     System.out.println("系统抽成筹码数--：" + systemServiceBankrolls);
//	
//	 }
//	 public void setSameInRobots(int robotCount){
//	     this.sameInRobots = robotCount;
//	 }

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void setPlayerService(PlayerService playerService) {
		this.playerService = playerService;
	}

	public void setRobotService(RobotService robotService) {
		this.robotService = robotService;
	}

	public Collection<Player> getRobots() {
		return robots;
	}

	private int id;
	private long gameId;
	private final Room room;
	private final Collection<Player> players;
	private final List<Player> playings;
	private final Seat[] seats;
	private final Card[] communityCards;

	private final Deck deck;
	private int numberOfLivePlayers;
	private int numberOfBoundPlayers;
	private int numberOfAllInPlayers;

	private Round round;
	private Seat dealer;
	private Seat actor;
	private Seat smallBlind;
	private Seat bigBlind;
	private Seat maxBetsSeat;
	private long maxBets;
	private boolean running;
	private boolean playing;

	private final Pot[] pots;
	private final int[] potStakes;
	private int potIndex;
	private int timeRecord;
	private boolean initial;
	private final AtomicBoolean locked;
	// 此属性为荷官属性
	@SuppressWarnings("unused")
	private int dealerIndex;

	public boolean isInitial() {
		return initial;
	}

	/**
	 * @return the round
	 */
	public Round getRound() {
		return round;
	}

	public int getId() {
		return id;
	}

	public long getGameId() {
		return gameId;
	}

	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * @return the seats
	 */
	public Seat[] getSeats() {
		return seats;
	}

	/**
	 * @return the pots
	 */
	public Pot[] getPots() {
		return pots;
	}

	/**
	 * 发送总的池子筹码
	 * 
	 * @return
	 */
	public int[] getPotStakes() {
		return potStakes;
	}

	/**
	 * @return the communityCards
	 */
	public Card[] getCommunityCards() {
		return communityCards;
	}

	/**
	 * @return the dealer
	 */
	public Seat getDealer() {
		return dealer;
	}

	/**
	 * @return the actor
	 */
	public Seat getActor() {
		return actor;
	}

	/**
	 * @return the smallBlind
	 */
	public Seat getSmallBlind() {
		return smallBlind;
	}

	/**
	 * @return the bigBlind
	 */
	public Seat getBigBlind() {
		return bigBlind;
	}

	/**
	 * @return the maxBetsSeat
	 */
	public Seat getMaxBetsSeat() {
		return maxBetsSeat;
	}

	/**
	 * @return the deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * 是否可以亮牌,被动触发
	 *
	 * @param seat
	 * @return
	 */
	public boolean canShowdown(Seat seat) {
		int timePassed = (int) System.currentTimeMillis();
		return seat.getShowdownState() == ShowdownState.OPTIONAL
				|| isTimeActive(timeRecord, timePassed);
	}

	public int getCountdownSeconds() {
		if (lastTask != null && lastTask instanceof AgentTask
				&& !lastFuture.isDone()) {
			return (int) lastFuture.getDelay(TimeUnit.SECONDS);
		}

		return 0;
	}

	/**
	 * @return the maxBets
	 */
	public long getMaxBets() {
		return maxBets;
	}

	public int getNumberOfPots() {
		if (pots[potIndex].isEmpty()) {
			return potIndex;
		} else {
			return potIndex + 1;
		}
	}

	private Seat getNearestActiveSeat(int from) {
		Seat seat = seats[from];
		if (!isActive(seat)) {
			seat = getNextActiveSeat(from);
		}
		return seat;
	}

	private Seat getNextActiveSeat(int from) {
		int index = -1;
		for (int i = from + 1, length = seats.length; i < length; i++) {
			if (isActive(seats[i])) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			for (int i = 0; i < from; i++) {
				if (isActive(seats[i])) {
					index = i;
					break;
				}
			}
		}

		return index > -1 ? seats[index] : null;
	}

	/**
	 * 判断是否存活
	 * 
	 * @return the active
	 */
	private boolean isActive(Seat seat) {
		return isPlaying(seat)
				&& (round == null || (!seat.noCards() && !seat.isAllIn()));
	}

	public Seat getNextPlayingSeat(int from) {
		int index = -1;
		for (int i = from + 1, length = seats.length; i < length; i++) {
			if (isPlaying(seats[i])) {
				index = i;
				break;
			}
		}

		if (index == -1) {
			for (int i = 0; i < from; i++) {
				if (isPlaying(seats[i])) {
					index = i;
					break;
				}
			}
		}

		return index > -1 ? seats[index] : null;
	}

	public Seat getUpPlayingSeat(int from, boolean robot) {
		int index = -1;
		for (int idx = from - 1; idx > 0; idx--) {
			if (isPlaying(seats[idx])
					&& (robot && robots.contains(seats[idx].getPlayer()))
					&& !seats[idx].noCards()) {
				index = idx;
				break;
			}
		}
		if (index == -1) {
			for (int idx = from, length = seats.length; idx < length; idx++) {
				if (isPlaying(seats[idx])
						&& (robot && robots.contains(seats[idx].getPlayer()))
						&& !seats[idx].noCards()) {
					index = idx;
					break;
				}
			}
		}
		return index > -1 ? seats[index] : null;
	}

	private boolean isPlaying(Seat seat) {
		Player player = seat.getPlayer();

		return player != null && isPlaying(player);
	}

	public Seat getSeatByPlayer(Player player) {
		for (Seat seat : seats) {
			if (seat.getPlayer() == player) {
				return seat;
			}
		}
		return null;
	}

	private volatile int numberOfSittings;

	public int getNumberOfSittings() {
		return numberOfSittings;
	}

	public void response(Action action) {
		response(action, false);
	}

	/**
	 * 叫注回应，主要的round转换，和行为回应的调用都在这个方法
	 *
	 * @param action
	 */
	private void response(Action action, boolean agented) {
		if (getRound() == null
				|| !(agented || (lastTask != null && (lastTask instanceof AgentTask)))) {
			return;
		}

		if (action.isValid(actor)) {
			logger.debug(actor + "[" + action + "]");
			removeLastDelayedFuture();
			action.execute(actor);
			if (action instanceof Fold) {
				numberOfLivePlayers --;
				numberOfBoundPlayers --;
			} else {
				if (action instanceof AllIn)
					numberOfAllInPlayers++;

				if (maxBetsSeat == null || actor.getStake() > maxBets) {
					if (actor.getStake() > maxBets) {
						maxBets = actor.getStake();
					}

					if (!(action instanceof AllIn)) {
						maxBetsSeat = actor;
					} else if (maxBetsSeat != null) {
						maxBetsSeat = null;
					}
				}
			}

			dispatchEvent(new DeskEvent(DeskEvent.ACTION, action));

			Seat nextActor;
			// 只剩下一个人，不是allin状态
			if (numberOfLivePlayers == 1) {
				disposePots(() -> {
					// 选择性亮
					for (Seat seat : seats) {
						if (!seat.noCards()) {
							seat.setShowdownState(ShowdownState.OPTIONAL);
						}
					}
					divideStake();
					finishGame(ANIMATION_SINGLE_TIME);
				});
				// 只剩下一个不是allin的
			} else if ( numberOfBoundPlayers == 1 || ((nextActor = getNextActiveSeat(actor.getIndex())) == null)
					|| (maxBetsSeat != null && numberOfLivePlayers
							- numberOfAllInPlayers == 1)) {
				disposePots(() -> {
					int dealTimesRemain = round.getDealTimesRemain();
					// 必须亮牌
					if (dealTimesRemain > 1) {
						for (Seat seat : seats) {
							if (!seat.noCards()) {
								seat.setShowdownState(ShowdownState.REQUIRED);
							}
						}
					}

					while ((round = round.next()) != null) {
						round.preprocess(this);
					}

					finishGame(DEAL_CARD_SECONDS * dealTimesRemain
							+ COMPARE_TIME + ANIMATION_SINGLE_TIME);
				});
				// 没有全部allin
			} else if (nextActor == maxBetsSeat) {
				disposePots(() -> {
					round = round.next();
					maxBets = 0;
					actor = getNearestActiveSeat(smallBlind.getIndex());
					maxBetsSeat = null;
					numberOfBoundPlayers = numberOfLivePlayers - numberOfAllInPlayers;
					logger.debug("进入[" + getRound() + "]阶段");
					round.preprocess(this);
					// 被动亮牌
					if (round.next() == null) {
						for (int i = 0; i < getNumberOfPots(); i++) {
							for (Seat seat : seats) {
								if (!seat.noCards()) {
									seat.setShowdownState(ShowdownState.REQUIRED);
								}
							}
						}

						finishGame(COMPARE_TIME + ANIMATION_SINGLE_TIME
								* getNumOfWinner());
					} else {
						dispatchEvent(new DeskEvent(DeskEvent.TURN_ROUND));
//						 if(players.size() ==0 && robots.size() == 5){
//						     agent();
//						 }else
						     delayAgent(DEAL_CARD_SECONDS);
					}
				});
			} else {
				actor = nextActor;
				logger.debug("表态者变为" + actor);
//				 if(players.size() ==0 && robots.size() == 5){
//				     agent();
//				 }else
				     delayAgent(ACTION_DURATION);
			}
		} else {
			logger.debug(actor + action.toString() + "失败");
		}
	}

	/**
	 * 进入桌子操作
	 *
	 * @param player
	 */
	public void accept(Player player, boolean rush) {
		if (rush) {
			dispatchEvent(new DeskEvent(DeskEvent.RUSH, player));
		}
		for (Seat seat : seats) {
			if (seat.getPlayer() == null) {
				seatDown(player, seat,
						() -> {
							player.setDesk(this);

							if (player.isRobot())
								robots.add(player);
							else
								players.add(player);

							dispatchEvent(new DeskEvent(DeskEvent.ENTER, seat,
									player));
						});
				break;
			}
		}
	}

	public void sendOff(Player player, boolean forExit) {
		standUp(player);
		player.setDesk(null);
		if (player.isRobot())
			robots.remove(player);
		else
			players.remove(player);
		if (forExit)
			dispatchEvent(new DeskEvent(DeskEvent.EXIT, player));
	}

	/**
	 * 清理一些全局变量的内容，清理筹码池等操作
	 */
	private void clear() {
		for (Pot pot : getPots()) {
			pot.clear();
		}
		for (Seat seat : seats) {
			seat.clear();
		}
		for (@SuppressWarnings("unused")
		int potStake : getPotStakes()) {
			potStake = 0;
		}

		for (int i = 0, length = communityCards.length; i < length; i++) {
			communityCards[i] = null;
		}

		potIndex = 0;
		numberOfLivePlayers = 0;
		numberOfAllInPlayers = 0;
		numberOfBoundPlayers = 0;
	}

	private final List<Future<Player>> futures = new ArrayList<>();

	private void disposePlayers() {
		playing = false;

		for (Player player : players) {
			if (!player.isOnline()) {
				playerService.tryExit(player);
			} else if (playings.contains(player)) {
				futures.add(playerService.syncPlayer(player, false,
						this::onSyncedPlayer));
			}
		}

		playings.clear();
		
		this.robotService.checkAndClearRobot(this);
		logger.debug("牌桌还剩下的真人玩家-------------：" + players.size());
		if (getRoom().getGameType() != GameType.OMAHA) {
			sameInRobots = this.robotService.randomRobot(getRoom(), this,
					scheduler);
		}

		 //临时代码
//		 if(players.size() == 0 && robots.size() == 5){
//    		 systemPlayCount++;
//    		
//    		 if(systemPlayCount >0 && systemPlayCount%100==0){
//        		 for(Player player : robots){
//            		 Robot robot = (Robot) player;
//            		 robot.robotBankrollInfo.add(robot.getBankroll());
//        		 }
//    		 }
//    		 for(Player player : robots){
//        		 Robot robot = (Robot) player;
//        		 if(robot.getBankroll() <= 0){
//            		 robot.setBankroll(10000);
//            		 robot.autoPolishings++;
//        		 }
//    		 }
//    		 System.out.println("机器人玩了----------------------------："+ systemPlayCount);
//    		 if(systemPlayCount == 1000){
//        		 for(Player player : robots){
//        		 Robot robot = (Robot) player;
//        		 System.out.println(robot.toString());
//        		 }
//        		 consoleRobotLog();
//        		 this.robotService.checkAndClearRobot(this);
//    		 }
		
//		 }
//		
//		 if(players.size() == 0 && robots.size() == 5){
//		     nextGame();
//		 }else
		     executeTask(this::nextGame, SHUTDOWN_SECONDS);
	}

	private void onSyncedPlayer(Player player) {
		player.setWin(false);

		if (player.isAgented() || player.isAutoStandingUp()
				|| player.getBankroll() < room.getMinStake()) {
			standUp(player);
		} else if (player.isAutoRushing()) {
			playerService.tryRush(player);
		}

		logger.debug(player + "处理完成");
	}

	/** 抽水量 */
	private long serviceBankroll;

	public long getServiceBankroll() {
		return serviceBankroll;
	}

	public void setServiceBankroll(long serviceBankroll) {
		this.serviceBankroll = serviceBankroll;
	}

	private void recordSomeLog() {
		LogClient.logRoomBankroll(getRoom().getId(), getTotalBankroll(),
				getServiceBankroll());
	}

	private void finishGame(float extraSeconds) {
		logger.debug(potStakes + "这一局的筹码池未抽水");
		float seconds = DIVIDE_SECONDS * getNumOfWinner() + extraSeconds;
		dispatchEvent(new DeskEvent(DeskEvent.GAME_OVER, seconds));
		List<Seat> failSeats = new ArrayList<Seat>();
		recordSomeLog();

		for (Seat seat : seats) {
			if (!seat.noCards()) {
				ShowdownState showdownState = seat.getShowdownState();
				if (showdownState.simplify() == ShowdownState.OPTIONAL
						.simplify()) {
					dispatchEvent(new DeskEvent(DeskEvent.SHOW_DOWN, seat));
				} else if (showdownState.simplify() == ShowdownState.REQUIRED
						.simplify()) {
					Pot[] pots = this.getPots();
					for (Pot pot : pots) {
						List<Seat> winSeat = pot.getLastWinSeats();
						if (winSeat != null) {
							if (!winSeat.contains(seat))
								if (failSeats.isEmpty()) {
									dispatchEvent(new DeskEvent(
											DeskEvent.SHOW_DOWN, seat));
									failSeats.add(seat);
								} else {
									if (!failSeats.contains(seat)) {
										dispatchEvent(new DeskEvent(
												DeskEvent.SHOW_DOWN, seat));
										failSeats.add(seat);
									}
								}
						}
					}
				}
			}
		}

		timeRecord = (int) System.currentTimeMillis();
		round = null;

//		 if(players.size() == 0 && robots.size() == 5){
//		     disposePlayers();
//		 }else
		     executeTask(this::disposePlayers, seconds);
		logger.debug(seconds + "秒后尝试进入下一局");
	}

	private long getTotalBankroll() {
		long totalBankroll = 0;
		for (Seat seat : seats) {
			if (seat != null && seat.getPlayer() != null) {
				totalBankroll += seat.getPlayer().getBankroll();
			}
		}
		return totalBankroll;
	}

	/**
	 * 开始方法
	 */
	private void tryStart() {
		if (numberOfSittings <= 1 || (robots.size() < sameInRobots)) {
			running = false;

			if (sameInRobots != 0 && players.size() >= 1
					&& getRoom().getGameType() != GameType.OMAHA) {
				this.robotService.join(getRoom(), this);
			}

			return;
		}

		playing = true;
		if (!running) {
			running = true;
			initial = true;
		} else {
			initial = false;
		}

		for (Seat seat : seats) {
			Player player = seat.getPlayer();
			if (player == null)
				continue;

			playings.add(player);
		}

		gameId = (((long) id) << 32) | (System.currentTimeMillis() / 1000);
		numberOfLivePlayers = numberOfSittings;
		// 函数式方式的，判断dealer是否为空，如果是则把第一个位置上的人设置为dealer，如果不是则将现在dealer玩家的下一个玩家设置为dealer
		dealer = dealer == null ? getNextPlayingSeat(0)
				: getNextPlayingSeat(dealer.getIndex());
		// 获取下一个已经坐下的座位号
		smallBlind = getNextPlayingSeat(dealer.getIndex());
		bigBlind = getNextPlayingSeat(smallBlind.getIndex());

		// 新手盲注下注操作
		for (Seat seat : seats) {
			Player player = seat.getPlayer();
			if (player == null)
				continue;

			if (player.isNewbie()) {
				player.setNewbie(false);
				seat.bet(room.getBigBlindBets());
			} else if (seat == smallBlind) {
				smallBlind.bet(room.getSmallBlindBets());
			} else if (seat == bigBlind) {
				bigBlind.bet(room.getBigBlindBets());
			}
		}

		logger.debug("游戏开始, 庄家" + dealer + ", 小盲" + smallBlind + ", 大盲"
				+ bigBlind);

		dispatchEvent(new DeskEvent(DeskEvent.GAME_START));

//		 if(players.size() == 0 && robots.size() == 5){
//		     startPreFlop();
//		 }else
		     executeTask(this::startPreFlop, PRE_FLOP_DELAY);
	}

	private void startPreFlop() {
		round = Round.PRE_FLOP;
		actor = getNextPlayingSeat(bigBlind.getIndex());
		maxBets = getRoom().getBigBlindBets();
		maxBetsSeat = null;
		numberOfBoundPlayers = numberOfLivePlayers;
		getRound().preprocess(Desk.this);

		dispatchEvent(new DeskEvent(DeskEvent.TURN_ROUND));
//		 if(players.size() == 0 && robots.size() == 5){
//		     agent();
//		 }else
		     delayAgent(START_SECONDS);

	}

	/**
	 * 分池操作
	 */
	private void disposePots(Runnable onDisposed) {
		if (maxBets == 0) {
			executeTask(onDisposed, ACTION_DURATION);
		} else {
			executeTask(() -> {
				doDispose(onDisposed);
			}, DISPOSE_POT_DELAY + ACTION_DURATION);
		}
	}

	private void doDispose(Runnable onDisposed) {
		// 设置座位副本；
		Seat[] seatsCopy = new Seat[seats.length];
		// 设置下注了的玩家数初始值为0
		int numberOfValidSeats = 0;

		// 循环一次座位，玩家一次放入数组中，索引号+1
		for (Seat seat : seats) {
			if (seat.getPlayer() != null && seat.getStake() != 0) {
				seatsCopy[numberOfValidSeats] = seat;
				numberOfValidSeats++;
			}
		}

		// 将这个座位按照降序排序，排序方式就是暗按照筹码数
		Arrays.sort(seatsCopy, (Seat o1, Seat o2) -> {
			if (o1 == null) {
				return 1;
			}
			if (o1.noCards()) {
				if (o2 == null) {
					return -1;
				}
				return 1;
			}
			if (o1.isAllIn()) {
				if (o2.isAllIn()) {
					return (int) (o1.getStake() - o2.getStake());
				}
			} else {
				if (o2.isAllIn()) {
					return 1;
				}
			}
			return -1;
		});

		// allin由小到大，投注的人，和弃牌的人
		for (int i = 0; i < numberOfValidSeats; i++) {
			Seat seat = seatsCopy[i];

			long stake = seat.getStake();
			// 判断是否allin
			if (stake == 0) {
				continue;
			}

			Pot pot = pots[potIndex];
			if (seat.isAllIn()) {
				for (int j = i; j < numberOfValidSeats; j++) {
					pot.capture(seatsCopy[j], stake);
				}
				logger.debug("筹码池" + potIndex + "筹码数为" + pot.getStake());
				potStakes[potIndex] = pot.getStake();
				potIndex++;
			} else {
				for (int j = i; j < numberOfValidSeats; j++) {
					pot.capture(seatsCopy[j]);
				}
				potStakes[potIndex] = pot.getStake();
				logger.debug("筹码池" + potIndex + "筹码数为" + pot.getStake());
				break;
			}
		}

		dispatchEvent(new DeskEvent(DeskEvent.DISPOSE_POTS));
		executeTask(onDisposed, DISPOSE_POT_SECONDS);
	}

	public void divideStake() {
		// 判断round为摊牌阶段
		if (round == Round.SHOWDOWN) {
			// 循环座位实例
			for (Seat seat : seats) {
				// 判断该座位玩家有手牌
				if (!seat.noCards() && seat.getPlayer() != null) {
					// 生成最大的牌
					seat.computeCardValue();
					logger.debug(seat.getPlayer() + "为" + seat.getCardValue());
				}
			}
		}

		// 分池操作
		for (int i = 0; i <= potIndex; i++) {
			Pot pot = pots[i];
			if (pot.isEmpty()) {
				break;
			}

			pot.serviceCharge(getRoom().getCoefficient());
			pot.divideStake();
		}
	}

	public void standUp(Player player) {
		Seat seat = getSeatByPlayer(player);
		if (seat != null) {
			// 显示站起信息
			logger.debug("玩家" + seat + "站起进行观看");
			// 清空这个座位的参与者
			seat.setPlayer(null);
			numberOfSittings--;
			dispatchEvent(new DeskEvent(DeskEvent.STAND_UP, seat));
		}
	}

	/**
	 * 坐下消息
	 *
	 * @param seatId
	 * 
	 * @param player
	 */
	public void trySeatDown(int seatId, Player player, Runnable onSuccess,
			Runnable lackOfBankroll, Runnable onOccupied) {
		trySeatDown(seatId, player, onSuccess, lackOfBankroll, onOccupied,
				false);
	}

	/**
	 * 坐下消息
	 *
	 * @param seatId
	 * 
	 * @param player
	 */
	private void trySeatDown(int seatId, Player player, Runnable onSuccess,
			Runnable lackOfBankroll, Runnable onOccupied, boolean retry) {
		Seat seat = seats[seatId - 1];
		if (seat.getPlayer() != null) {
			onOccupied.run();
		} else if (this.getSeatByPlayer(player) == null) {
			if (player.getBankroll() < room.getCarry()) {
				if (!retry) {
					playerService.syncPlayer(
							player,
							true,
							(p) -> {
								trySeatDown(seatId, player, onSuccess,
										lackOfBankroll, onOccupied, true);
							});
				} else {
					lackOfBankroll.run();
				}
			} else {
				seatDown(player, seat, onSuccess);
			}
		}
	}

	private void seatDown(Player player, Seat seat, Runnable onSuccess) {
		seat.setPlayer(player);
		player.setNewbie(true);
		player.setAgented(false);
		seat.setShowdownState(ShowdownState.DISCARDED);
		numberOfSittings++;

		logger.debug(seat + "加入牌桌" + getId());
		if (onSuccess != null)
			onSuccess.run();

		if (!running) {
		    //TODO 压测机器人测试，暂时注释
			if (sameInRobots == 0 && getRoom().getGameType() != GameType.OMAHA) {
				sameInRobots = robotService.randomRobot(getRoom(), this,
						scheduler);
			}
			tryStart();
		}
	}

	public boolean isPlaying() {
		return playing;
	}

	public boolean isPlaying(Player player) {
		return playing && playings.contains(player);
	}

	private void delayAgent(float delaySeconds) {
		logger.debug(delaySeconds + "秒后开始进入托管倒计时");
		executeTask(this::agent, delaySeconds);
	}

	private void agent() {
		if (actor.getPlayer().isAgented()) {
			doAgent();
		} else {
//			 if(players.size() == 0 && robots.size() == 5){
//			     robotService.reponse(this, actor.getPlayer());
//			 }
//			 else
			executeTask(new AgentTask(), AGENT_SECONDS);
			if (actor.getPlayer().isRobot()) {
				int seconds = RandomUtil.random(1, 4);
				// executeTask(new RobotTask(), seconds);
				scheduler.submit(new RobotTask(), null, id, seconds * 1000l,// seconds
																			// *
																			// 1000l,
						TimeUnit.MILLISECONDS);
				logger.debug(actor.toString() + "将于" + seconds + "秒后会操作");
			} else {
				logger.debug(actor.toString() + "将于" + AGENT_SECONDS + "秒后会被托管");
			}
			dispatchEvent(new DeskEvent(DeskEvent.COUNTDOWN));
		}
	}

	/**
	 * 判断是否为托管，如果是托管，则开始托管流程若不是则继续普通操作
	 *
	 * @param action
	 */
	private void doAgent() {
		ActionFactory actionFactory = ActionFactory.getInstance();

		Action action = actionFactory.getAction(Check.NAME);
		if (!action.isValid(actor)) {
			action = actionFactory.getAction(Fold.NAME);
		}

		response(action, true);
	}

	class AgentTask implements Runnable {
		@Override
		public void run() {
			actor.getPlayer().setAgented(true);
			logger.debug(actor + "被托管了");
			doAgent();
		}
	}

	class RobotTask implements Runnable {
		@Override
		public void run() {
			robotService.reponse(Desk.this, actor.getPlayer());
		}
	}

	private void nextGame() {
		for (Future<Player> future : futures) {
			try {
				future.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error(e.getMessage(), e);
				continue;
			}
		}
		futures.clear();

		clear();
		tryStart();
	}

	@Override
	public Map<String, Object> simplify() {
		Map<String, Object> simplified = new HashMap<>();

		simplified.put("seats", Seat.simplifyAll(seats));

		if (playing) {
			simplified.put("dealer", dealer.getId());
			simplified.put("smallBlind", smallBlind.getId());
			simplified.put("bigBlind", bigBlind.getId());

			if (round != null) {
				simplified.put("communityCards",
						Card.simplifyAll(communityCards));
				simplified.put("pots",
						Pot.simpliyfyAll(pots, getNumberOfPots()));
				simplified.put("round", round.simplify());
				simplified.put("actor", actor.getId());
			}
		}

		return simplified;
	}

	public Collection<Player> getPlayers() {
		return players;
	}

	public boolean isTimeActive(int timeRecord, int timePassed) {
		return timeRecord - timePassed < 0 || round == null;
	}

	public int getWorkingBankroll() {
		int workingBankroll = 0;
		if (round == null)
			return 0;

		for (int i = 0; i <= potIndex; i++) {
			Pot pot = pots[potIndex];
			workingBankroll += pot.getStake();
		}

		for (Seat seat : seats) {
			workingBankroll += seat.getStake();
		}

		return workingBankroll;
	}

	public boolean lock() {
		return locked.compareAndSet(false, true);
	}

	public boolean unlock() {
		return locked.compareAndSet(true, false);
	}

	public int getNumberOfLivePlayers() {
		return numberOfLivePlayers;
	}

	private boolean disposing = false;

	public void dispose() {
		disposing = true;
		for (Player player : players) {
			player.setOnline(false);
		}

		Runnable lastTask = this.lastTask;
		if (lastTask != null && removeLastDelayedFuture()) {
			lastTask.run();
		} else {
			disposePlayers();
		}
	}

	private volatile ScheduledFuture<?> lastFuture;
	private volatile Runnable lastTask;

	private void executeTask(Runnable task, float seconds) {
		if (disposing) {
			task.run();
			lastFuture = null;
		} else {
			long milliSeconds = (long) (seconds * 1000);
			lastFuture = scheduler.submit(task, null, id, milliSeconds,
					TimeUnit.MILLISECONDS);
			lastTask = task;
		}
	}

	private boolean removeLastDelayedFuture() {
		boolean res = false;
		if (lastFuture != null) {
			lastFuture.cancel(true);
			lastFuture = null;
			lastTask = null;
		}
		return res;
	}

	private int getNumOfWinner() {
		int numOfWinner = 0;
		for (int i = 0; i <= potIndex; i++) {
			Pot pot = pots[i];
			if (pot.isEmpty()) {
				break;
			}

			if (pot.getLastWinSeats() != null)
				numOfWinner += pot.getLastWinSeats().size();
		}
		return numOfWinner;
	}

	public int changeDealer(int type) {
		SettingService settingService = SettingService.getInstance();
		int dealerChips = settingService.getDealerChips(type);
		dealerIndex = type;
		return dealerChips;
	}

}
