package com.kunlun.poker.center.service.impl;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.center.data.PlayerWrapper;
import com.kunlun.poker.center.data.UserWrapper;
import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.center.service.UserService;
import com.kunlun.poker.center.system.SchedulerUtil;
import com.kunlun.poker.rmi.dto.PlayerDTO;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(UserServiceImpl.class);

	@Autowired(required = true)
	private UserWrapper userWrapper;
	@Autowired(required = true)
	private PlayerWrapper playerWrapper;

	@Autowired(required = true)
	private Scheduler scheduler;

	private volatile boolean numberOfPlayersIsDirty = false;
	private HashMap<Integer, Integer> allRoomPlayNums;

	@Override
	public void saveUser(User user) {
		SchedulerUtil.submitPersistenceTask(() -> syncSaveUser(user),
				scheduler, user.getId());
	}

	private void syncSaveUser(User user) {
		userWrapper.saveUser(user);
		user.commitChanged();
	}

	@Override
	public User getUserById(int id, boolean withPlayers) {
		User user = userWrapper.getUserById(id);

		if (withPlayers) {
			List<PlayerDTO> players = playerWrapper.findPlayers(id);
			for (PlayerDTO player : players) {
				user.putPlayer(player);
			}
		}
		return user;
	}

	public void addPlayer(PlayerDTO player) {
		scheduler.submit(() -> {
			playerWrapper.insertPlayer(player);
			numberOfPlayersIsDirty = true;
		}, scheduler, player.getId());
	}

	public void mergePlayers() {
		List<HashMap<String, Number>> playerTotals = playerWrapper
				.selectPlayerTotals();
		for (HashMap<String, Number> playerTotal : playerTotals) {
			playerWrapper.mergePlayers(playerTotal.get("id").intValue(),
					playerTotal.get("bankroll").intValue(),
					playerTotal.get("revenue").floatValue());
		}

		playerWrapper.deleteAllPlayers();
	}

	public void mergePlayer(PlayerDTO player, User user) {
		user.setBankroll(user.getBankroll() + player.getBankroll());
		user.setRevenue(user.getRevenue() + player.getRevenue());

		SchedulerUtil.submitPersistenceTask(() -> {
			syncSaveUser(user);
			playerWrapper.deletePlayer(player);
		}, scheduler, user.getId());
	}

	public void savePlayer(PlayerDTO player) {
		SchedulerUtil.submitPersistenceTask(
				() -> playerWrapper.updatePlayer(player), scheduler,
				player.getId());
	}

	@Override
	public HashMap<Integer, Integer> getAllRoomPlayerNums() {
		if (allRoomPlayNums == null || numberOfPlayersIsDirty) {
			List<HashMap<String, Number>> list = playerWrapper
					.getAllRoomPlayerNum();
			if (list != null) {
				allRoomPlayNums = new HashMap<Integer, Integer>(list.size());
				for (HashMap<String, Number> map : list) {
					int roomId = map.get("roomId").intValue();
					int playerNums = map.get("playerNums").intValue();
					allRoomPlayNums.put(roomId, playerNums);
				}
			}
		}
		return allRoomPlayNums;
	}

	@Override
	public User getUserByUid(String uid) {
		User user = userWrapper.getUserByUid(uid);
		if (user == null) {
			return null;
		}
		return user;
	}

}
