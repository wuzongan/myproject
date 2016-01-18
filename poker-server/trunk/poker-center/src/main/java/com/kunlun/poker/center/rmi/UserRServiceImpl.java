package com.kunlun.poker.center.rmi;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.canoe.core.session.Role;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.center.domain.Item;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.service.AttainmentService;
import com.kunlun.poker.center.service.ItemService;
import com.kunlun.poker.center.service.NoticeService;
import com.kunlun.poker.center.service.RankService;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.center.service.UserService;
import com.kunlun.poker.exception.ErrorCode;
import com.kunlun.poker.exception.GameException;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.rmi.UserRService;
import com.kunlun.poker.rmi.dto.PlayerDTO;
import com.kunlun.poker.server.GameSession;

@Component("userRService")
public class UserRServiceImpl implements UserRService {
	private static final Logger logger = LoggerFactory
			.getLogger(UserRServiceImpl.class);
	@Autowired
	private SessionRoleManager sessionRoleManger;
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private RoomService roomService;
	@Autowired
	private UserService userService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private AttainmentService attainmentService;
	@Autowired
	private RankService rankService;
	@Autowired
	private NoticeService noticeService;

	@Override
	public PlayerDTO applyJoinRoom(int sessionId, int buyIn, int roomId)
			throws GameException {
		Session session = sessionRoleManger.getSession(sessionId);
		if (session == null)
			return null;
		User user = (User) session.getRole();
		if (user != null) {
			// Room room = roomService.getRoomById(roomId);

			Future<PlayerDTO> future = scheduler.submit(() -> {
				PlayerDTO player = user.getPlayerByRoomId(roomId);

				if (player == null) {
					if (user.getBankroll() >= buyIn) {
						// user.setBankroll(user.getBankroll() -
						// room.getCarry());
					user.setBankroll(user.getBankroll() - buyIn);
					player = new PlayerDTO();
					player.setRoomId(roomId);
					player.setBankroll(buyIn);
					player.setId(user.getId());
					// player.setBestCards(user.getBestCards());
					player.setPokerHand(user.getPokerHand());
					player.setBestCards(user.getBestCardArray());
					player.setPortrait(user.getPortrait());
					player.setAutoPlay(false);
					user.putPlayer(player);
					userService.addPlayer(player);
					userService.saveUser(user);
				} else {
					throw new GameException(ErrorCode.CAN_NOT_IN_ROOM_BY_CHIP);
				}
			}

			player.setName(user.getName());
			return player;
		}, user.getId());

			try {
				return future.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error(e.getMessage(), e);
			}
		}

		return null;
	}

	public void disposePlayer(PlayerDTO player) {
		if (player == null)
			return;

		User user = getUserById(player.getId());
		scheduler.submit(() -> {
			userService.mergePlayer(player, user);
			user.removePlayer(player);
		}, player, player.getId());
	}

	public PlayerDTO syncPlayer(PlayerDTO player, long supplyment) {
		if (player == null)
			return null;

		int uid = player.getId();
		User user = getUserById(uid);

		Future<PlayerDTO> future = scheduler.submit(() -> {
			long actualSupplyment = supplyment;
			if (actualSupplyment != 0) {
				long carrency = user.getBankroll();

				if (carrency < actualSupplyment) {
					user.setBankroll(0);
					actualSupplyment = carrency;
				} else {
					user.setBankroll(carrency - actualSupplyment);
				}

				player.setBankroll(player.getBankroll() + actualSupplyment);
			}

			user.putPlayer(player);

			userService.savePlayer(player);
			attainmentService.trrigerGameAtt(user, player.getRoomId(),
					player.isWin());
			attainmentService.trrigerPokerHandAtt(user, player.getRoomId(),
					player.getPokerHand());

			userService.saveUser(user);
		}, player, uid);

		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}

		return null;
	}

	public void login(int sessionId, int userId) {
		User user = getUserById(userId);
		if (user != null) {
			scheduler.submit(() -> {
				Session session = sessionRoleManger.getSession(sessionId);
				if (session == null) {
					session = new GameSession(sessionId);
					sessionRoleManger.addSession(session);
				}

				session.setRole(user);
				long lastLoginTime = user.getLastLoginTime();
				user.setLastLoginTime(System.currentTimeMillis());
				attainmentService.trrigerlogin(user, lastLoginTime);
				// rankService.sendFirstRankInfo(user);
					noticeService.login(user);

					// LogClient.sendLogMessage(LogClient.buildTlUserOnlineLogMessage(userId));
					// LogClient.sendLogMessage(LogClient.buildTlUserStatisticsLogMessage(userId,
					// user.getName(), user.getFb()));

					logger.debug(user + "登陆了");
				}, user, user.getId());
		}
	}

	private User getUserById(int uid) {
		User user = (User) sessionRoleManger.getRole(uid);
		if (user == null) {
			user = userService.getUserById(uid, true);
		}

		return user;
	}

	@Override
	public void buyChip(String kluid, int golds) {
		User user = userService.getUserByUid(kluid);
		user = getUserById(user.getId());
		if (user != null) {
			long bankRoll = user.getBankroll();
			int addbankRoll = golds;
			Item item = itemService.getItemByCoin(addbankRoll);
			if (item != null) {
				user.setBankroll(bankRoll + addbankRoll);
				LogClient.logSaleChip(addbankRoll);
				userService.saveUser(user);
				user.buyChips(addbankRoll);
			}
		}
	}

	@Override
	public boolean isLogin(int userId) {
		Role role = sessionRoleManger.getRole(userId);
		if (role != null)
			return true;
		return false;
	}

	@Override
	public boolean giveDealerChip(PlayerDTO player, long supplyment) {
		int uid = player.getId();
		User user = getUserById(uid);
		long carry = user.getBankroll();
		if(carry >= supplyment){
			syncPlayer(player, supplyment);
			return true;
		}else{
			return false;
		}
	}
}
