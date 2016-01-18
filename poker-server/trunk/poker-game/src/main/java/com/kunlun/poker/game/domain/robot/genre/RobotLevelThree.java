package com.kunlun.poker.game.domain.robot.genre;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.Deck;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.ActionFactory;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Round;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.domain.actions.AllIn;
import com.kunlun.poker.game.domain.actions.Bet;
import com.kunlun.poker.game.domain.actions.Call;
import com.kunlun.poker.game.domain.actions.Check;
import com.kunlun.poker.game.domain.actions.Fold;
import com.kunlun.poker.game.domain.actions.Raise;



public class RobotLevelThree {
	private static final Logger logger = LoggerFactory
	        .getLogger(RobotLevelThree.class);
	
	public void process(Desk desk){
		if(desk.getActor().getPlayer().isRobot()){
			calculateAction(desk);
		}
	}
	
	public void calculateAction(Desk desk){
		ActionFactory actionFactory = ActionFactory.getInstance();
		Seat actor = desk.getActor();
		int bigBlindChips = desk.getRoom().getBigBlindBets();
		crudeNum = calculateCard(desk);
		if(isRobotBest(desk)){
			if(isRobotSelfBest(desk)){
				if((desk.getMaxBets() - actor.getStake()) < 5*bigBlindChips){
					if(desk.getMaxBets() == 0){
						if(desk.getRound() == Round.PRE_FLOP){
							Action action = actionFactory.getAction(Check.NAME);
							if (!action.isValid(actor)) {
								action = actionFactory.getAction(AllIn.NAME);
							}
							desk.response(action);
						}else{
							Action action = actionFactory.getAction(Bet.NAME);
							if (!action.isValid(actor)) {
								action = actionFactory.getAction(AllIn.NAME);
							}
							desk.response(action);
						}
					}else{
						if(actor.getPlayer().getBankroll() > (desk.getMaxBets()+5*bigBlindChips)){
							Action action = actionFactory.getAction(Raise.NAME, desk.getMaxBets()+5*bigBlindChips);
							if (!action.isValid(actor)) {
								action = actionFactory.getAction(AllIn.NAME);
							}
							desk.response(action);
						}else{
							Action action = actionFactory.getAction(AllIn.NAME );
							desk.response(action);
						}
					}
				}else{
					Action action = actionFactory.getAction(AllIn.NAME );
					desk.response(action);
				}
			}else{
				if(desk.getMaxBets() == 0){
					Action action = actionFactory.getAction(Check.NAME);
					if (!action.isValid(actor)) {
						action = actionFactory.getAction(Fold.NAME);
					}
					desk.response(action);
				}else{
					if(desk.getMaxBets() == actor.getStake()){
						Action action = actionFactory.getAction(Check.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}else{
						if(actor.getPlayer().getBankroll() > desk.getMaxBets()){
						    Action action = actionFactory.getAction(Call.NAME);
						    if (!action.isValid(actor)) {
							    action = actionFactory.getAction(AllIn.NAME);
						    }
						    desk.response(action);
						}else{
							Action action = actionFactory.getAction(AllIn.NAME);
							desk.response(action);
						}
					}
				}
			}
		}else{
			if(desk.getMaxBets() == 0){
				Action action = actionFactory.getAction(Check.NAME);
				if (!action.isValid(actor)) {
					action = actionFactory.getAction(Fold.NAME);
				}
				desk.response(action);	
			}else{
				if(desk.getMaxBets() - actor.getStake() == 0){
					Action action = actionFactory.getAction(Check.NAME);
					if (!action.isValid(actor)) {
						action = actionFactory.getAction(Fold.NAME);
					}
					desk.response(action);
				}else if(actor.getStake() <= 2*bigBlindChips){
					if(desk.getMaxBets() -actor.getStake() <= 2*bigBlindChips){
						Action action = actionFactory.getAction(Call.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Fold.NAME);
						desk.response(action);
					}
				}else{
					Action action = actionFactory.getAction(Fold.NAME);
					desk.response(action);
				}
			}
		}
	}
	
	int crudeNum = 0;
	public boolean isRobotBest(Desk desk){
		if(crudeNum == 1){
			return true;
		}else if(crudeNum == 2){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean isRobotSelfBest(Desk desk){
		if(crudeNum == 1){
			return false;
		}else if(crudeNum == 2){
			return true;
		}else{
			return false;
		}
	}
	
	private List<Seat> robotList = new ArrayList<Seat>();
	private List<Seat> playerList = new ArrayList<Seat>();
	
	public int calculateCard(Desk desk){
		Seat[] seats = desk.getSeats();
		Deck deck = desk.getDeck();
		Card[] haveCommCards = desk.getCommunityCards();
		Card[] communityCards =new  Card[5];
		int index = 0;
		int indexJ = 0;
		if(haveCommCards[0] == null){
			for(int i = 0; i <5; i++){
				communityCards[i] = deck.getByOffset(i);
			}
		}else{
			for(Card card : haveCommCards){
				if(card != null){
					communityCards[index] = card;
					index ++;
				}
			}

			for(int j = index; j<5; j++){
				communityCards[j] = deck.getByOffset(indexJ);
				indexJ ++;
			}
		}
		
		

		
		logger.debug("公共牌:     "+communityCards);
		
		for(Seat seat : seats){
			if(seat.getPlayer() != null && !seat.noCards()){
				if(seat.getPlayer().isRobot()){
					robotList.add(seat);
				}else{
					playerList.add(seat);
				}
			}
		}
		
		CardValue lastCardRobot = null;
		Seat lastSeatRobot = null;
		for(Seat robotSeat : robotList){
			Card[] startCards = robotSeat.getStartingHand();
			List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 5);
			CardValue cardValue = cardValues.get(0);
			if(lastCardRobot != null){
				int ref = cardValue.compareTo(lastCardRobot);
				if(ref == 0){
					lastCardRobot = cardValue;
					lastSeatRobot = robotSeat;
				}else if(ref >0){
					lastCardRobot = cardValue;
					lastSeatRobot = robotSeat;
				}
			} else{
				lastCardRobot = cardValue;
				lastSeatRobot = robotSeat;
			}
		}
		
		CardValue lastCardPlayer = null;
		@SuppressWarnings("unused")
		Seat lastSeatPlayer;
		for(Seat playerSeat : playerList){
			Card[] startCards = playerSeat.getStartingHand();
			List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 5);
			CardValue cardValue = cardValues.get(0);
			logger.debug(playerSeat.getPlayer().getName() + "    手牌：　　"+startCards+"   公共牌：    " +communityCards+"    最大牌型：    "+cardValue.getCards());
			if(lastCardPlayer != null){
				int ref = cardValue.compareTo(lastCardPlayer);
				if(ref == 0){
					lastCardPlayer = cardValue;
					lastSeatPlayer = playerSeat;
				}else if(ref >0){
					lastCardPlayer = cardValue;
					lastSeatPlayer = playerSeat;
				}
			} else{
				lastCardPlayer = cardValue;
				lastSeatPlayer = playerSeat;
			}
		}
		
		//0是人，1机器人（不是自己）,2是自己
		int ref = 0;
		if(lastCardPlayer != null){
			ref = lastCardPlayer.compareTo(lastCardRobot);
		}else{
			ref = -1;
		}
		
		if(ref == 0){
			if(desk.getActor() == lastSeatRobot){
				logger.debug("机器人最大（自己）");
				return 2;
			}
			logger.debug("人最大");
			return 0;
		}else if(ref > 0){
			logger.debug("人最大");
			return 0;
		}else{
			if(desk.getActor() == lastSeatRobot){
				logger.debug("机器人最大（自己）");
				return 2;
			}else{
				Card[] startCards = desk.getActor().getStartingHand();
				List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 5);
				CardValue cardValue = cardValues.get(0);
				int refRobot = cardValue.compareTo(lastCardRobot);
				if(refRobot == 0){
					logger.debug("机器人最大");
					return 1;
				}else if(refRobot > 0){
					logger.debug("机器人最大（自己）");
					return 2;
				}else{
					logger.debug("机器人最大");
					return 1;
				}
			}
		}
	}
}
