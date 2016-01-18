package com.kunlun.poker.game.domain.robot.genre;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.Card;
import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.ColorSuit;
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
import com.kunlun.poker.game.domain.robot.ActionProbability;
import com.kunlun.poker.util.RandomUtil;

public class RobotLevelOne {
    private static final Logger logger = LoggerFactory
            .getLogger(RobotLevelOne.class);
    
	public void process(Desk desk){
		if(desk.getActor().getPlayer().isRobot()){
			calculateAction(desk);
		}
	}
	
	/**
	 *	计算叫注的结果
	 */
	private void calculateAction(Desk desk){
		float prop = calculateProb(desk);
		ActionFactory actionFactory = ActionFactory.getInstance();
		System.out.println(prop);
		Seat actor = desk.getActor();
		int bigBlindChips = desk.getRoom().getBigBlindBets();
		if(desk.getRound() == Round.PRE_FLOP){
			if((prop >= ActionProbability.SCOPE_PROB_ONE) && (prop <= ActionProbability.SCOPE_PROB_TWO)){
				int reserveBankRoll = bigBlindChips*ActionProbability.CALL_PROB_ONE_CASE;
				if((desk.getMaxBets() - actor.getStake()) < reserveBankRoll){
					if((desk.getMaxBets() - actor.getStake()) == 0){
						Action action = actionFactory.getAction(Check.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Call.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}
				}else{
					desk.response(ActionFactory.getInstance().getAction("fold"));
				}
			}
			else if(prop>ActionProbability.SCOPE_PROB_TWO && prop<=ActionProbability.SCOPE_PROB_THREE){
				int reserveBankRoll = bigBlindChips*ActionProbability.CALL_PROB_FLOP_TWO_CASE;
				if((desk.getMaxBets() - actor.getStake()) < reserveBankRoll){
					if((desk.getMaxBets() - actor.getStake()) == 0){
						Action action = actionFactory.getAction(Check.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Call.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}
				}else{
					desk.response(ActionFactory.getInstance().getAction("fold"));
				}
			}
			else if(prop>ActionProbability.SCOPE_PROB_THREE && prop<=ActionProbability.SCOPE_PROB_FOUR){
				if(actor.getPlayer().getBankroll() > desk.getMaxBets()*2){
					Action action = actionFactory.getAction(Raise.NAME, 
                            desk.getMaxBets()*2);
					if (!action.isValid(actor)) {
						action = actionFactory.getAction(AllIn.NAME);
					}
					desk.response(action);
				}else{
					desk.response(ActionFactory.getInstance().getAction("allin"));
				}
			}else{
				if(actor == desk.getMaxBetsSeat()){
					Action action = actionFactory.getAction(Check.NAME);
					if (!action.isValid(actor)) {
						action = actionFactory.getAction(AllIn.NAME);
					}
					desk.response(action);
				}else{
					desk.response(ActionFactory.getInstance().getAction("fold"));
				}
			}
		}else{
			if(prop<=ActionProbability.SCOPE_PROB_ONE){
				int reserveBankRoll =bigBlindChips*ActionProbability.CALL_PROB_ONE_CASE;
				if((desk.getMaxBets() - actor.getStake()) < reserveBankRoll){
					if((desk.getMaxBets() - actor.getStake()) == 0){
						Action action = actionFactory.getAction(Check.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Call.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(Fold.NAME);
						}
						desk.response(action);
					}
				}else{
					desk.response(ActionFactory.getInstance().getAction("fold"));
				}
			}
			else if(prop>ActionProbability.SCOPE_PROB_ONE && prop<=ActionProbability.SCOPE_PROB_TWO){
				int reserveBankRoll =bigBlindChips*ActionProbability.CALL_PROB_FLOP_TWO_CASE;
				if((desk.getMaxBets() - actor.getStake()) < reserveBankRoll){
					if((desk.getMaxBets() - actor.getStake()) == 0){
						Action action = actionFactory.getAction(Bet.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Call.NAME);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}
				}else{
					desk.response(ActionFactory.getInstance().getAction("fold"));
				}
			}
			else if(prop>ActionProbability.SCOPE_PROB_TWO && prop<=ActionProbability.SCOPE_PROB_FOUR){
				int maxBets = bigBlindChips*RandomUtil.random(ActionProbability.FIRST_BIG_BLIND_RISE_LOW, 
						ActionProbability.FIRST_BIG_BLIND_RISE_HIGH);
				if(actor.getPlayer().getBankroll() > desk.getMaxBets()*2 && actor.getPlayer().getBankroll() > maxBets){
					if(desk.getMaxBets() - actor.getStake() == 0)
					{
						Action action = actionFactory.getAction(Bet.NAME, maxBets);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}else{
						Action action = actionFactory.getAction(Raise.NAME, 
                                desk.getMaxBets()*2);
						if (!action.isValid(actor)) {
							action = actionFactory.getAction(AllIn.NAME);
						}
						desk.response(action);
					}
				}else{
					desk.response(ActionFactory.getInstance().getAction("allin"));
				}
			}
		}
	}
	
	/**
	 * 计算手牌概率
	 * @param desk
	 */
	private float calculateProb(Desk desk){
		Card[] startCards =  desk.getActor().getStartingHand();
		Card[] communityCards = desk.getCommunityCards();
		
		if(desk.getRound() == Round.PRE_FLOP)
			return getStartHandProp(startCards);
		
		else if(desk.getRound() == Round.FLOP ){
			List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 3);
			CardValue cardValue = cardValues.get(0);
			PokerHand pokerHand = cardValue.getPokerHand();
			Card[] overCards = cardValue.getCards();
			return getFlopTurnProp(pokerHand, startCards, communityCards, overCards);
		}
		
		if(desk.getRound() == Round.TURN){
			try{
				List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 4);
				CardValue cardValue = cardValues.get(0);
				PokerHand pokerHand = cardValue.getPokerHand();
				Card[] overCards = cardValue.getCards();
				return getFlopTurnProp(pokerHand, startCards, communityCards, overCards);
			}catch(Exception e){
				logger.error(e.getMessage(),e);
			}
		}
		
		if(desk.getRound() == Round.RIVER){
			List<CardValue> cardValues = PokerHand.listen(startCards, communityCards, 5);
			CardValue cardValue = cardValues.get(0);
			PokerHand pokerHand = cardValue.getPokerHand();
			Card[] overCards = cardValue.getCards();
			return getFlopRiverProp(pokerHand, startCards, communityCards, overCards);
		}
		
		return 0;
	}
	
	/**
	 * 计算是否含有A
	 * @param startCards
	 * @param communityCards
	 * @return
	 */
	private boolean isInA(Card[] startCards, Card[] communityCards){
		for (int i = 0; i < startCards.length; i++) {
			if(startCards[i].getFace() == 1)
				return true;
		}
		for (int i = 0; i < communityCards.length; i++) {
			if (communityCards[i].getFace() == 1) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 起手牌的概率
	 * @param startCards
	 * @return
	 */
	private float getStartHandProp(Card[] startCards){
		int firstFace = startCards[0].getFace();
		int twoFace = startCards[1].getFace();
		ColorSuit firstSuit = startCards[0].getSuit();
		ColorSuit twoSuit = startCards[1].getSuit();
		//对子牌
		if(firstFace == twoFace){
			if(firstFace == 1)
				return ActionProbability.PRE_FLOP_AA;
			if(firstFace == 13)
				return ActionProbability.PRE_FLOP_KK;
			if(firstFace == 12)
				return ActionProbability.PRE_FLOP_QQ;
			if(firstFace == 11)
				return ActionProbability.PRE_FLOP_JJ;
			if(firstFace == 10)
				return ActionProbability.PRE_FLOP_TT;
			return ActionProbability.PRE_FLOP_PAIR;
		//非对子牌 
		}else {
			//A牌面特定牌
			if(firstFace == 1 || twoFace ==1)
				return ActionProbability.PRE_FLOP_A_OTHER;
			if(firstSuit == twoSuit)
				return ActionProbability.PRE_FLOP_FLUSH;
			if(Math.abs(firstFace -twoFace) <4){
				if((firstFace>10  ||  firstFace==1)&&(twoFace>10 || twoFace==1))
					return ActionProbability.PRE_FLOP_TUP_CONNECTORS;
				return ActionProbability.PRE_FLOP_CONNECTORS;
			}
			return ActionProbability.PRE_FLOP_OTHER;
		}
	}
	
	/**
	 * 翻牌转牌概率
	 * @param pokerHand
	 * @param startCards
	 * @param communityCards
	 * @return
	 */
	private float  getFlopTurnProp(PokerHand pokerHand, Card[] startCards, Card[] communityCards, 
			Card[] overCards){
		if(pokerHand == PokerHand.THREE_OF_A_KIND)
			return ActionProbability.OTHER_THREE_OF_A_KIND;
		if(pokerHand == PokerHand.FULL_HOUSE)
			return ActionProbability.OTHER_FULL_HOUSE;
		if(pokerHand == PokerHand.FOUR_OF_A_KIND)
			return ActionProbability.OTHER_FOUR_OF_A_KIND;
		if(pokerHand == PokerHand.ONE_PAIR)
			return ActionProbability.OTHER_ONE_PAIR;
		if(pokerHand == PokerHand.TWO_PAIR)
			return ActionProbability.OTHER_TWO_PAIR;
		if(pokerHand == PokerHand.FLUSH)
			return ActionProbability.OTHER_FLUSH;
		if(pokerHand == PokerHand.STRAIGHT)
			return ActionProbability.OTHER_STRAIGHT;
		if(pokerHand == PokerHand.STRAIGHT_FLUSH && isInA(startCards, communityCards))
			return ActionProbability.OTHER_ROYAL_FLUSH;
		if(pokerHand == PokerHand.STRAIGHT_FLUSH)
			return ActionProbability.OTHER_STRAIGHT_FLUSH;
		if(pokerHand == PokerHand.HIGH_CARD)
			return ActionProbability.OTHER_HIGH_CARD;
		return ActionProbability.OTHER_HIGH_CARD;
	}
	
	/**
	 * 河牌概率
	 * @param pokerHand
	 * @param startCards
	 * @param communityCards
	 * @return
	 */
	private float getFlopRiverProp(PokerHand pokerHand, Card[] startCards, Card[] communityCards, 
			Card[] overCards){
		if(pokerHand == PokerHand.THREE_OF_A_KIND)
			return ActionProbability.OTHER_THREE_OF_A_KIND;
		if(pokerHand == PokerHand.FULL_HOUSE)
			return ActionProbability.OTHER_FULL_HOUSE;
		if(pokerHand == PokerHand.FOUR_OF_A_KIND)
			return ActionProbability.OTHER_FOUR_OF_A_KIND;
		if(pokerHand == PokerHand.ONE_PAIR)
			return ActionProbability.OTHER_ONE_PAIR;
		if(pokerHand == PokerHand.TWO_PAIR)
			return ActionProbability.OTHER_TWO_PAIR;
		if(pokerHand == PokerHand.FLUSH)
			return ActionProbability.OTHER_FLUSH;
		if(pokerHand == PokerHand.STRAIGHT)
			return ActionProbability.OTHER_STRAIGHT;
		if(pokerHand == PokerHand.STRAIGHT_FLUSH && isInA(startCards, communityCards))
			return ActionProbability.OTHER_ROYAL_FLUSH;
		if(pokerHand == PokerHand.STRAIGHT_FLUSH)
			return ActionProbability.OTHER_STRAIGHT_FLUSH;
		if(pokerHand == PokerHand.HIGH_CARD)
			return ActionProbability.OTHER_HIGH_CARD;
		return ActionProbability.OTHER_HIGH_CARD;
	}
}
