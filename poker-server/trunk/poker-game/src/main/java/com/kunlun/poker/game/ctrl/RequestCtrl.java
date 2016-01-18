package com.kunlun.poker.game.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.command.CallbackEvent;
import com.googlecode.canoe.core.command.annotation.CanoeCallback;
import com.googlecode.canoe.core.command.annotation.CanoeCommand;
import com.googlecode.canoe.core.message.Request;
import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.exception.ErrorCode;
import com.kunlun.poker.exception.GameException;
import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.ActionFactory;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.domain.ShowdownState;
import com.kunlun.poker.game.domain.actions.Raise;
import com.kunlun.poker.game.service.PlayerService;
import com.kunlun.poker.game.service.RoomService;
import com.kunlun.poker.game.system.GameProtocol;
import com.kunlun.poker.game.util.ResponseUtil;
import com.kunlun.poker.logClient.LogClient;
import com.kunlun.poker.util.Callback;
import com.kunlun.poker.util.DataUtil;

@Controller
public class RequestCtrl {

	private static final Logger logger = LoggerFactory
			.getLogger(RequestCtrl.class);
	@Autowired(required = true)
	private PlayerService playerService;
	@Autowired(required = true)
	private RoomService roomService;

	@SuppressWarnings("unchecked")
	@CanoeCommand(value = GameProtocol.C_ENTER, roleRequired = false)
	public void onEnter(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Map<String, Object> data = (Map<String, Object>) request.getData();

		int autoMakingUp = (int) data.get("autoMakingUp");
		int buyIn = (int) data.get("buyIn");

		int way = DataUtil.getInt((Integer) data.get("way"), 1);

		Callback<Player> onJoin = player -> {
			player.setOnline(true);
			player.setAutoMakingUp(DataUtil.intToBoolean(autoMakingUp));
			session.setRole(player);
		};
		// 需要加判断买入值
		playerService
				.join((int) session.getId(),
						buyIn,
						way,
						onJoin,
						player -> {
							onJoin.invoke(player);
							Desk desk = player.getDesk();
							Map<String, Object> simplifiedDesk = (Map<String, Object>) desk
									.simplify();

							Seat seat = desk.getSeatByPlayer(player);
							if (seat != null) {
								simplifiedDesk.put("seat", seat.getId());

								if (desk.getRound() != null) {
									simplifiedDesk.put("cards",
											Card.simplifyAll(seat
													.getStartingHand()));
								}
							}

							simplifiedDesk.put("autoMakingUp", DataUtil
									.booleanToInt(player.isAutoMakingUp()));
							simplifiedDesk.put("autoStandingUp", DataUtil
									.booleanToInt(player.isAutoStandingUp()));
							simplifiedDesk.put("agented",
									DataUtil.booleanToInt(player.isAgented()));
							simplifiedDesk.put("type", desk.getRoom()
									.getGameType().toString());

							Response enterSelf = new Response(
									GameProtocol.S_ENTER_SELF);
							enterSelf.setScope(ResponseScope.SPECIFIED);
							enterSelf.setRecievers(Arrays.asList(player));
							enterSelf.setData(simplifiedDesk);
							responseSender.send(enterSelf);

							int countdownSeconds = desk.getCountdownSeconds();
							if (countdownSeconds > 0) {
								Map<String, Integer> responseData = new HashMap<>();
								responseData.put("seconds", countdownSeconds);
								responseData.put("total", Desk.AGENT_SECONDS);
								responseData.put("seat", desk.getActor()
										.getId());

								Response response = new Response(
										GameProtocol.S_COUNTDOWN);
								response.setScope(ResponseScope.SPECIFIED);
								response.setRecievers(Arrays.asList(player));
								response.setData(responseData);
								responseSender.send(response);
							}
						}, (e) -> {
							if (e instanceof GameException) {
								ResponseUtil.error(responseSender,
										((GameException) e).getErrorCode());
							}
						});
	}

	@CanoeCommand(value = GameProtocol.C_ACTION, roleRequired = true)
	public void onAction(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		if (desk.getActor().getPlayer() != player)
			return;

		@SuppressWarnings("unchecked")
		Map<String, Object> data = (Map<String, Object>) request.getData();
		String actionName = (String) data.get("action");
		int stake = 0;
		if (Raise.NAME.equals(actionName)) {
			stake = (Integer) data.get("stake");
		}

		Action action = ActionFactory.getInstance()
				.getAction(actionName, stake);
		desk.response(action);
	}

	@CanoeCommand(value = GameProtocol.C_SHOW_DOWN, roleRequired = true)
	public void onShowDown(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		Seat seat = desk.getSeatByPlayer(player);
		if (desk.canShowdown(seat)) {
			HashMap<String, Object> data = new HashMap<>();
			data.put("seat", seat.getId());
			data.put("showdown", ShowdownState.REQUIRED.simplify());
			data.put("cards", Card.simplifyAll(seat.getStartingHand()));

			Response response = new Response(GameProtocol.S_SHOW_DOWN);
			response.setScope(ResponseScope.SPECIFIED);
			List<Player> playersCopy = new ArrayList<>();
			playersCopy.addAll(desk.getPlayers());
			playersCopy.remove(player);
			response.setRecievers(playersCopy);
			response.setData(data);

			responseSender.send(response);
		}
	}

	@CanoeCommand(value = GameProtocol.C_SET_AUTO_MAKING_UP, roleRequired = true)
	public void onMakingUp(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		int dataInt = (Integer) request.getData();

		boolean state = DataUtil.intToBoolean(dataInt);
		player.setAutoMakingUp(state);
		Response enterSelf = new Response(GameProtocol.S_SET_AUTO_MAKING_UP,
				session);
		enterSelf.setScope(ResponseScope.SELF);
		enterSelf.setData(dataInt);
		responseSender.send(enterSelf);
	}

	@CanoeCommand(value = GameProtocol.C_CANCEL_AGENT, roleRequired = true)
	public void onCancelAgent(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		player.setAgented(false);
	}

	@CanoeCommand(value = GameProtocol.C_SET_AUTO_STANDING_UP, roleRequired = true)
	public void onStandingUp(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		int dataInt = (Integer) request.getData();
		boolean state = DataUtil.intToBoolean(dataInt);
		player.setAutoStandingUp(state);

		Response enterSelf = new Response(GameProtocol.S_SET_AUTO_STANDING_UP,
				session);
		enterSelf.setScope(ResponseScope.SELF);
		enterSelf.setData(dataInt);
		responseSender.send(enterSelf);
	}

	@CanoeCommand(value = GameProtocol.C_SEAT_DOWN, roleRequired = true)
	public void onSeatDown(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		int data = (Integer) request.getData();

		desk.trySeatDown(data, player, () -> {
			Seat seat = desk.getSeatByPlayer(player);
			Response enterSelf = new Response(GameProtocol.S_ENTER_MASS);
			enterSelf.setScope(ResponseScope.SPECIFIED);
			enterSelf.setRecievers(desk.getPlayers());
			enterSelf.setData(seat.simplify());
			responseSender.send(enterSelf);

			Response enterMessage = new Response(GameProtocol.S_SEAT_DOWN,
					session);
			enterMessage.setScope(ResponseScope.SELF);
			enterMessage.setData(data);
			responseSender.send(enterMessage);
		}, () -> {
			ResponseUtil.error(responseSender, ErrorCode.CHIPS_NOT_ENOUGH_CAN_NOT_SEAT_DOWN);
		}, () -> {
			ResponseUtil.error(responseSender, ErrorCode.CAN_NOT_SET_AUTO_MAKING_UP);
		});

	}

	@CanoeCommand(value = GameProtocol.C_STAND_UP, roleRequired = true)
	public void onStandUp(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		int data = (Integer) request.getData();
		if (desk.getRound() == null) {
			desk.standUp(player);
			Response enterSelf = new Response(GameProtocol.S_STAND_UP);
			enterSelf.setScope(ResponseScope.SPECIFIED);
			enterSelf.setRecievers(desk.getPlayers());
			enterSelf.setData(data);
			responseSender.send(enterSelf);
		}
	}

	@CanoeCommand(value = GameProtocol.C_MAKING_UP, roleRequired = true)
	public void onMakingUpBySelf(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		Desk desk = player.getDesk();
		Room room = desk.getRoom();

		int carryData = (Integer) request.getData();
		if ((carryData >= room.getMinStake())
				&& (carryData < room.getMaxStake())) {
			playerService.syncPlayer(player, carryData, (p) -> {
				afterMakingUpBySelf(responseSender, desk, true);
			});
		} else {
			afterMakingUpBySelf(responseSender, desk, false);
		}
	}

	private void afterMakingUpBySelf(ResponseSender responseSender, Desk desk,
			boolean success) {
		Response enterSelf = new Response(GameProtocol.S_MAKING_UP);
		enterSelf.setScope(ResponseScope.SPECIFIED);
		enterSelf.setData(DataUtil.booleanToInt(success));
		enterSelf.setRecievers(desk.getPlayers());
		responseSender.send(enterSelf);
	}

	@CanoeCommand(value = GameProtocol.C_RUSH, roleRequired = true)
	public void onRush(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		if (player != null) {
			playerService.tryRush(player);
		}
	}

	@CanoeCommand(value = GameProtocol.C_GIVE_DEALER_TIP, roleRequired = true)
	public void onGiveDealerTip(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		if (player != null) {
			Desk desk = player.getDesk();
			Seat seat = desk.getSeatByPlayer(player);
			Map<String, Integer> dealerTip = new HashMap<String, Integer>();
			long smallBlindBets = (long)desk.getRoom().getSmallBlindBets();
			dealerTip.put("seat", seat.getId());
			player.setDealerTips(player.getDealerTips() + smallBlindBets);
			boolean isEnough = playerService.giveDealerTip(player, smallBlindBets);
			if(isEnough){
				dealerTip.put("dealerChip",(int) smallBlindBets);
				LogClient.logDealerChips(smallBlindBets);
				Response response = new Response(GameProtocol.S_GIVE_DEALER_TIP);
				response.setScope(ResponseScope.SPECIFIED);
				response.setRecievers(desk.getPlayers());
				response.setData(dealerTip);
				responseSender.send(response);
			}
		}
	}
	
	@CanoeCommand(value = GameProtocol.C_CHANGE_DEALER, roleRequired = true)
	public void onChangeDealerTip(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		Player player = (Player) session.getRole();
		int data = (int)request.getData();
		if (player != null) {
			Desk desk = player.getDesk();
			int dealerChips = desk.changeDealer(data);
			long lastBankRoll = player.getBankroll() - dealerChips;
			if(lastBankRoll > 0){
				player.setBankroll(lastBankRoll);
				int seatId = desk.getSeatByPlayer(player).getId();
				Map<String, Integer> dealerChange = new HashMap<String, Integer>();
				dealerChange.put("seat", seatId);
				dealerChange.put("dealerId", data);
				Response response = new Response(GameProtocol.S_CHANGE_DEALER);
				response.setScope(ResponseScope.SPECIFIED);
				response.setRecievers(desk.getPlayers());
				response.setData(dealerChange);
				responseSender.send(response);
			}
		}
	}

	@CanoeCommand(value = GameProtocol.C_EXIT, roleRequired = true)
	public void onExit(Request request, ResponseSender responseSender) {
		Session session = request.getSession();
		exit(session);
		session.setRole(null);
	}

	@CanoeCallback(value = CallbackEvent.ONDISCONNECTED)
	public void onDisconnect(Session session, ResponseSender responseSender) {
		exit(session);
	}

	private Player exit(Session session) {
		Player player = (Player) session.getRole();
		if (player != null) {
			player.setOnline(false);
			playerService.tryExit(player);
			logger.debug(player + "下线了");
		}
		return player;
	}
}
