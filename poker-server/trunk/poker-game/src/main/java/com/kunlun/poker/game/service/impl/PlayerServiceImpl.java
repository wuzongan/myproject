package com.kunlun.poker.game.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.Config;
import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.exception.ErrorCode;
import com.kunlun.poker.exception.GameException;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.service.DeskService;
import com.kunlun.poker.game.service.PlayerService;
import com.kunlun.poker.game.service.RoomService;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.rmi.RMIExecutor;
import com.kunlun.poker.rmi.UserRService;
import com.kunlun.poker.rmi.dto.PlayerDTO;
import com.kunlun.poker.util.Callback;

@Component("playerService")
public class PlayerServiceImpl implements PlayerService {

	private static final Logger logger = LoggerFactory
			.getLogger(PlayerServiceImpl.class);
	private final Map<Integer, Player> playerMap;

	@Autowired(required = true)
	private RoomService roomService;

	@Autowired(required = true)
	private DeskService deskService;

	@Autowired(required = true)
	private UserRService userRService;
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private RMIExecutor rmiExecutor;

	public PlayerServiceImpl() {
		// idGenerator = new AtomicInteger();
		playerMap = new ConcurrentHashMap<>();
	}

	/**
	 * 加入房间,房间内无此玩家，则新增一个到房间中，每次加入房间则直接加入桌子
	 *
	 * @param room
	 * @return
	 */
	@Override
	public void join(int sessionId, int buyIn, int way,
			Callback<Player> onJoin, Callback<Player> onTakeOver,
			Callback<Throwable> onFail) {
		final Desk desk = deskService.fetchAndLockDesk();

		if (desk != null) {
			rmiExecutor.execute( () -> userRService.applyJoinRoom(sessionId,
					buyIn, Config.getInstance().getServerId()), sessionId, 
					playerDTO -> {
				try {
					int uid = playerDTO.getId();

					Player player = getById(uid);
					if (player == null) {
						player = new Player();
						player.setId(uid);
						player.setName(playerDTO.getName());
						player.setBankroll(playerDTO.getBankroll());
						player.setPortrait(playerDTO.getPortrait());

						PokerHand pokerHand = playerDTO.getPokerHand();
						if (pokerHand != null) {
							player.checkAndSetPokerhand(new CardValue(
									pokerHand, playerDTO.getBestCards()));
						}
						playerMap.put(uid, player);

						onJoin.invoke(player);
						desk.accept(player, false);

						LogClient.logUserStatistics(playerDTO.getRoomId(),
								player.getId(), way);
					} else {
						onTakeOver.invoke(player);
					}
				} finally {
					desk.unlock();
				}
			}, onFail, desk.getId());
		} else {
			onFail.invoke(new GameException(ErrorCode.ROOM_IS_FULL));
		}
	}

	@Override
	public Player getById(int userId) {
		return playerMap.get(userId);
	}

	@Override
	public boolean inRoom(Player player) {
		return getById(player.getId()) != null;
	}

	/**
	 * 换张桌子
	 *
	 * @param player
	 */
	@Override
	public void tryRush(Player player) {
		Desk desk = player.getDesk();
		if (desk == null)
			return;

		if (!desk.isPlaying(player)) {
			final Desk newDesk = deskService.fetchAndLockDesk(desk);

			// 判断是否（如果新的桌子是空的）
			if (newDesk != null) {
				// 送走参与者
				desk.sendOff(player, false);
				// 接受参与者
				scheduler.submit(() -> {
					try {
						newDesk.accept(player, true);
						player.setAutoRushing(false);
					} finally {
						desk.unlock();
					}
				}, null, desk.getId());
			}
		} else {
			player.setAutoRushing(true);
		}
	}

	/**
	 * 离开游戏桌,离开房间
	 *
	 * @param player
	 */
	@Override
	public void tryExit(Player player) {
		Desk desk = player.getDesk();

		boolean ready = false;
		if (desk == null) {
			ready = true;
		} else if (!desk.isPlaying(player)) {
			desk.sendOff(player, true);
			ready = true;

			LogClient.logGameTypeUserOnline(desk.getRoom().getId(),
					player.getId(), player.getEnterGameTime(),
					System.currentTimeMillis(), player.getBankroll());
		}

		if (ready) {
			rmiExecutor.execute(
					() -> userRService.disposePlayer(createPLayerDTO(player)),
					player.getId(), () -> {
						playerMap.remove(player.getId(), player);
						logger.debug(player + "离开房间");
					}, null, desk == null ? 0 : desk.getId());
		}
	}

	/**
	 * 同步玩家信息，必要的话自动补齐
	 */
	@Override
	public Future<Player> syncPlayer(Player player, boolean forceMakingUp,
			Callback<Player> callback) {
		long supplyment;
		if(player.getBankroll() <= player.getDesk().getRoom().getBigBlindBets())
			if (forceMakingUp || (player.isAutoMakingUp() && !player.isAgented())) {
				supplyment = Math.max(
						roomService.getRoom().getCarry() - player.getBankroll(), 0);
			} else {
				supplyment = 0;
			}
		else{
			supplyment = 0;
		}

		return syncPlayer(player, supplyment, callback);
	}
	
	@Override
	public Future<Player> syncPlayer(Player player, long makingUp,
			Callback<Player> callback) {
		FutureTask<Player> future = new FutureTask<Player>(() -> {
			if (callback != null) {
				callback.invoke(player);
			}
			return player;
		});

		rmiExecutor.execute(() -> userRService.syncPlayer(
				createPLayerDTO(player), makingUp), player.getId(), (p) -> {
			player.setBankroll(p.getBankroll());
			future.run();
		}, (e) -> {
			logger.error(e.getMessage(), e);
		}, player.getDesk().getId());

		return future;
	}
	
	public boolean giveDealerTip(Player player,long smallBet){
		PlayerDTO playerDTO = createPLayerDTO(player);
		return userRService.giveDealerChip(playerDTO, smallBet);
	}

	private PlayerDTO createPLayerDTO(Player player) {
		PlayerDTO playerDTO = new PlayerDTO();
		playerDTO.setId(player.getId());
		playerDTO.setRoomId(Config.getInstance().getServerId());
		playerDTO.setBankroll(player.getBankroll());
		playerDTO.setDealerTips(player.getDealerTips());
		playerDTO.setRevenue(player.getRevenue());
		playerDTO.setExp(player.getExp());
		playerDTO.setWin(player.isWin());
		playerDTO.setPortrait(player.getPortrait());
		CardValue cardValue = player.getBestCardValue();
		if(cardValue != null){
			playerDTO.setPokerHand(cardValue.getPokerHand());
			playerDTO.setBestCards(cardValue.getCards());
		}
		return playerDTO;
	}

	public Collection<Player> getPlayers() {
		return new ArrayList<Player>(playerMap.values());
	}
}
