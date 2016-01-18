package com.kunlun.poker.game.service.impl;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.Config;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.service.DeskService;
import com.kunlun.poker.game.service.PlayerService;
import com.kunlun.poker.logClient.LogClient;

@Service("logRecordTimeTask")
public class LogRecordTimeTask implements Runnable {
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private PlayerService playerService;
	@Autowired
	private DeskService deskService;

	@Override
	public void run() {
		int totalBankroll = 0;
		int roomId = (int) Config.getInstance().getServerId();
		Collection<Player> players = playerService.getPlayers();
		for (Player player : players) {
			totalBankroll += player.getBankroll();
		}

		for (Desk desk : deskService.getDesks()) {
			totalBankroll += desk.getWorkingBankroll();
		}

		if (players.size() != 0) {
			int averageChips = totalBankroll / players.size();
			LogClient.logRoomChip(roomId,totalBankroll, averageChips);
		}
		scheduler.submit(this, null, "logRecord", 0, 5, TimeUnit.MINUTES);
	}

}
