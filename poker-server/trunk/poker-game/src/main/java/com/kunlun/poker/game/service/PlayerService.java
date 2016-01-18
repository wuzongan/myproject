package com.kunlun.poker.game.service;

import java.util.Collection;
import java.util.concurrent.Future;

import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.util.Callback;

public interface PlayerService {
	Player getById(int id);

	Future<Player> syncPlayer(Player player, boolean forceMakingUp,
			Callback<Player> callback);

	Future<Player> syncPlayer(Player player, long makingUp,
			Callback<Player> callback);

	void tryRush(Player player);

	void join(int sessionid, int buyIn, int way, Callback<Player> onJoin,
			Callback<Player> onTakeOver, Callback<Throwable> onError);

	void tryExit(Player player);

	boolean inRoom(Player player);

	public Collection<Player> getPlayers();
	
	public boolean giveDealerTip(Player player, long smallBets);
}
