package com.kunlun.poker.game.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.googlecode.canoe.core.message.Response;
import com.googlecode.canoe.core.message.ResponseScope;
import com.googlecode.canoe.core.message.ResponseSender;
import com.googlecode.canoe.core.session.Session;
import com.googlecode.canoe.core.session.SessionRoleManager;
import com.googlecode.canoe.event.Event;
import com.googlecode.canoe.event.EventUtil;
import com.googlecode.canoe.event.anno.EventListener;
import com.kunlun.poker.domain.Card;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.DeskEvent;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.PlayerEvent;
import com.kunlun.poker.game.domain.Pot;
import com.kunlun.poker.game.domain.Round;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.domain.ShowdownState;
import com.kunlun.poker.game.system.GameProtocol;
import com.kunlun.poker.util.DataUtil;

@Controller("eventCtrl")
public class EventCtrl {
	@Autowired
	private ResponseSender responseSender;
	@Autowired
	private SessionRoleManager roleManager;

	public EventCtrl() {
		EventUtil.gatherListeners(this);
	}

	@EventListener(event = DeskEvent.ENTER)
	public void onEnterGame(Event<Desk> event) {
		DeskEvent deskEvent = (DeskEvent) event;
		Desk desk = deskEvent.getTarget();
		Player player = deskEvent.getPlayer();
		player.setEnterGameTime(System.currentTimeMillis());
		Seat seat = deskEvent.getSeat();
		List<Player> others = new ArrayList<>(desk.getPlayers());
		others.remove(player);

		Response enterMass = new Response(GameProtocol.S_ENTER_MASS);
		enterMass.setScope(ResponseScope.SPECIFIED);
		enterMass.setData(seat.simplify());
		enterMass.setRecievers(others);
		responseSender.send(enterMass);

		Map<String, Object> simplifiedDesk = desk.simplify();
		simplifiedDesk.put("seat", seat.getId());
		if (desk.getRound() != null) {
			simplifiedDesk.put("cards",
					Card.simplifyAll(seat.getStartingHand()));
		}
		simplifiedDesk.put("autoMakingUp",
				DataUtil.booleanToInt(player.isAutoMakingUp()));
		simplifiedDesk.put("autoStandingUp",
				DataUtil.booleanToInt(player.isAutoStandingUp()));
		simplifiedDesk.put("agented", 
				DataUtil.booleanToInt(player.isAgented()));
		simplifiedDesk.put("type", 
				desk.getRoom().getGameType().toString());

		Response enterSelf = new Response(GameProtocol.S_ENTER_SELF);
		enterSelf.setScope(ResponseScope.SPECIFIED);
		enterSelf.setRecievers(Arrays.asList(player));
		enterSelf.setData(simplifiedDesk);
		responseSender.send(enterSelf);

		int countdownSeconds = desk.getCountdownSeconds();
		if (countdownSeconds > 0) {
			Map<String, Integer> data = new HashMap<>();
			data.put("seconds", countdownSeconds);
			data.put("total", Desk.AGENT_SECONDS);
			data.put("seat", desk.getActor().getId());

			Response response = new Response(GameProtocol.S_COUNTDOWN);
			response.setScope(ResponseScope.SPECIFIED);
			response.setRecievers(Arrays.asList(player));
			response.setData(data);
			responseSender.send(response);
		}
	}

	@EventListener(event = DeskEvent.GAME_START)
	public void onGameStart(Event<Desk> event) {
		Desk desk = event.getTarget();

		Seat[] seats = desk.getSeats();
		Map<String, Object> deskInfo = new HashMap<>();
		deskInfo.put("dealer", desk.getDealer().getId());
		deskInfo.put("smallBlind", desk.getSmallBlind().getId());
		deskInfo.put("bigBlind", desk.getBigBlind().getId());
		deskInfo.put("startState", DataUtil.booleanToInt(desk.isInitial()));
		deskInfo.put("seats", Seat.simplifyAll(seats, true, false));

		Response response = new Response(GameProtocol.S_GAME_START);
		response.setScope(ResponseScope.SPECIFIED);
		response.setRecievers(desk.getPlayers());
		response.setData(deskInfo);
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.ACTION)
	public void onAction(Event<Desk> event) {
		Desk desk = event.getTarget();

		Response response = new Response(GameProtocol.S_ACTION);
		response.setScope(ResponseScope.SPECIFIED);
		response.setRecievers(desk.getPlayers());
		response.setData(DataUtil.createMap(new Object[] { "action",
				(((DeskEvent) event).getAction()).getName(), "seat",
				desk.getActor().getId(), "stake", desk.getActor().getStake(),
				"bankroll", desk.getActor().getPlayer().getBankroll() }));

		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.TURN_ROUND)
	public void onTurnRound(Event<Desk> event) {
		Desk desk = event.getTarget();

		Collection<Player> players = desk.getPlayers();
		Set<Player> receivers = new HashSet<>(players);

		Round round = desk.getRound();
		Map<String, Object> data = new HashMap<>();
		data.put("round", desk.getRound().simplify());
		data.put("actor", desk.getActor().getId());
		
		if (round != Round.PRE_FLOP) {
			data.put("communityCards",
					Card.simplifyAll(desk.getCommunityCards()));
		}

		Response response = new Response(GameProtocol.S_TURN_ROUND);
		response.setScope(ResponseScope.SPECIFIED);
		response.setRecievers(desk.getPlayers());
		response.setData(data);

		responseSender.send(response);

		if(round == Round.PRE_FLOP)
		{
			Seat[] seats = desk.getSeats();
	
			for (Seat seat : seats) {
				Player player = seat.getPlayer();
				if (player != null && !seat.noCards() && player.isOnline()) {
					response = new Response(GameProtocol.S_DEAL_HAND);
					response.setRecievers(Arrays.asList(player));
					response.setData(Card.simplifyAll(seat.getStartingHand()));
	
					responseSender.send(response);
	
					receivers.remove(player);
				}
			}
	
			if (receivers.size() != 0) {
				response = new Response(GameProtocol.S_DEAL_HAND);
				response.setRecievers(receivers);
				responseSender.send(response);
			}
		}
	}

	@EventListener(event = DeskEvent.SHOW_DOWN)
	public void onShowdown(Event<Desk> event) {
		Seat seatInfo = ((DeskEvent) event).getSeat();
		Desk desk = event.getTarget();
		Player player = seatInfo.getPlayer();

		HashMap<String, Object> simplifiedShowdown = new HashMap<>();
		simplifiedShowdown.put("seat", seatInfo.getId());
		simplifiedShowdown.put("showdown", seatInfo.getShowdownState()
				.simplify());
		simplifiedShowdown.put("cards",
				Card.simplifyAll(seatInfo.getStartingHand()));

		Response response = new Response(GameProtocol.S_SHOW_DOWN);

		if (seatInfo.getShowdownState() == ShowdownState.OPTIONAL) {
			response.setScope(ResponseScope.SPECIFIED);
			List<Player> players = new ArrayList<>();
			players.add(player);
			response.setRecievers(players);
			response.setData(simplifiedShowdown);
		} else {
			List<Player> playersCopy = new ArrayList<>();
			playersCopy.addAll(desk.getPlayers());
			playersCopy.remove(player);
			response.setScope(ResponseScope.SPECIFIED);
			response.setRecievers(playersCopy);
			response.setData(simplifiedShowdown);
		}
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.DISPOSE_POTS)
	public void onDisposePots(Event<Desk> event) {
		Desk desk = event.getTarget();

		Response response = new Response(GameProtocol.S_DISPOSE_POTS);
		response.setRecievers(desk.getPlayers());
		response.setData(Pot.simpliyfyAll(desk.getPots(),
				desk.getNumberOfPots()));
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.GAME_OVER)
	public void onGameOver(Event<Desk> event) {
		Desk desk = event.getTarget();

		int numberOfPots = desk.getNumberOfPots();
		Pot[] pots = desk.getPots();
		@SuppressWarnings("unchecked")
		Map<String, Object>[] simplifiedPots = new Map[numberOfPots];
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<Integer, Object> simplifiedAllWinSeats = new HashMap();
		
		for (int i = 0; i < numberOfPots; i++) {
			Pot pot = pots[i];
			Map<String, Object> simplifiedPot = new HashMap<String, Object>();
			List<Seat> winSeats = pot.getLastWinSeats();
			int numberOfWinners = winSeats.size();
			Integer[] simplifiedWinSeats = new Integer[numberOfWinners];
			for (int j = 0; j < numberOfWinners; j++) {
				simplifiedWinSeats[j] = winSeats.get(j).getId();
			}
			simplifiedPot.put("stake", pot.getStake());
			simplifiedPot.put("winSeats", simplifiedWinSeats);
			simplifiedPot.put("potStake", desk.getPotStakes()[i]);
			simplifiedPots[i] = simplifiedPot;
			
			for(Seat seat : winSeats){
				if(seat.getStartingHand() != null){
					Card[] cards = seat.getStartingHand();
					if(!simplifiedAllWinSeats.containsKey(seat.getId()))
						simplifiedAllWinSeats.put(seat.getId(), Card.simplifyAll(cards));
				}
			}
		}

		Map<String, Object> data = new HashMap<>();
		data.put("communityCards", Card.simplifyAll(desk.getCommunityCards()));
		data.put("pots", simplifiedPots);
		data.put("seats", Seat.simplifyAll(desk.getSeats()));
		data.put("clearSeconds", ((DeskEvent) event).getSeconds());
		
		if(desk.getNumberOfLivePlayers() == 1){
			simplifiedAllWinSeats.clear();
			data.put("winner", simplifiedAllWinSeats);
		}else{
			data.put("winner", simplifiedAllWinSeats);
		}
		

		Response response = new Response(GameProtocol.S_SHUT_DOWN);
		response.setRecievers(desk.getPlayers());
		response.setData(data);
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.COUNTDOWN)
	public void onCountdown(Event<Desk> event) {
		Desk desk = event.getTarget();

		Map<String, Integer> data = new HashMap<>();
		data.put("seconds", desk.getCountdownSeconds());
		data.put("total", Desk.AGENT_SECONDS);
		data.put("seat", desk.getActor().getId());

		Response response = new Response(GameProtocol.S_COUNTDOWN);
		response.setRecievers(desk.getPlayers());
		response.setData(data);
		responseSender.send(response);
	}

	@EventListener(event = PlayerEvent.SWITCH_AGENT)
	public void onSwitchAgent(Event<Player> event) {
		Player player = event.getTarget();
		Response response = new Response(GameProtocol.S_SWITCH_AGENT);
		response.setRecievers(Arrays.asList(player));
		response.setData(DataUtil.booleanToInt(player.isAgented()));
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.STAND_UP)
	public void onStandUp(Event<Desk> event) {
		Desk desk = event.getTarget();
		Seat seat = ((DeskEvent) event).getSeat();
		int data = seat.getId();
		Response response = new Response(GameProtocol.S_STAND_UP);
		response.setScope(ResponseScope.SPECIFIED);
		response.setRecievers(desk.getPlayers());
		response.setData(data);
		responseSender.send(response);
	}

	@EventListener(event = DeskEvent.EXIT)
	public void onExit(Event<Desk> event) {
		Response response = new Response(GameProtocol.S_EXIT);
		response.setScope(ResponseScope.SPECIFIED);
		Collection<Player> players = new ArrayList<Player>(event.getTarget()
				.getPlayers());

		Player player = ((DeskEvent) event).getPlayer();
		players.add(player);
		response.setRecievers(players);
		response.setData(player.getId());
		responseSender.send(response);

		Session session = roleManager.getSession(player);
		if(session != null)	
			session.setRole(null);
	}

	@EventListener(event = DeskEvent.RUSH)
	public void onRush(Event<Desk> event) {
		DeskEvent deskEvent = (DeskEvent) event;
		Response response = new Response(GameProtocol.S_RUSH);
		response.setScope(ResponseScope.SPECIFIED);
		response.setRecievers(Arrays.asList(deskEvent.getPlayer()));
		responseSender.send(response);
	}

}
