package com.kunlun.poker.game.domain.robot;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kunlun.poker.domain.CardValue;
import com.kunlun.poker.domain.PokerHand;
import com.kunlun.poker.domain.RobotAiConstants;
import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.ActionFactory;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.Round;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.domain.actions.AllIn;
import com.kunlun.poker.game.domain.actions.Bet;
import com.kunlun.poker.game.domain.actions.Call;
import com.kunlun.poker.game.domain.actions.Check;
import com.kunlun.poker.game.domain.actions.Fold;
import com.kunlun.poker.game.domain.actions.Raise;
import com.kunlun.poker.util.RandomUtil;

/***
 * @author ljx
 */
public class RobotAiProcessor {

    private static final Logger logger = LoggerFactory
        .getLogger(RobotAiProcessor.class);
    
    /**
     * 第二种机器人的处理逻辑
     * 暂时不抽取冗余代码，查看日志输出代码排查流程是否有问题
     * @param desk
     */
    public static void processTwo(Desk desk){
        Round round = desk.getRound();
        Seat[] seats = desk.getSeats();
        Seat tmpSeat = null;
        List<CardValue> tmpCardValues = null;
        {
            //第一步选取 玩家手牌最大的
            int numberOfCommunityCards = 0;
            if(round == Round.FLOP){
                numberOfCommunityCards = 3;
            }else if(round == Round.TURN){
                numberOfCommunityCards = 4;
            }else if(round == Round.RIVER){
                numberOfCommunityCards = 5;
            }
            for(Seat seat : seats){
                Player player = seat.getPlayer();
                if(player != null && !player.isRobot() && !seat.noCards()){
                    List<CardValue> cardValues = PokerHand.listen(seat.getStartingHand(), desk.getCommunityCards(), numberOfCommunityCards);
                    if(tmpCardValues == null && tmpSeat == null){
                        tmpSeat = seat;
                        tmpCardValues = cardValues;
                    }else{
                        if(tmpCardValues.get(0).compareTo(cardValues.get(0)) < 0){
                            tmpSeat = seat;
                            tmpCardValues = cardValues;
                        }
                    }
                }
            }

            //牌桌上没有玩家了
            if(tmpSeat == null || tmpSeat.getPlayer() == null){
                //TODO 暂时默认处理
                //RobotAi.TEST.process(desk);
                tmpSeat = desk.getUpPlayingSeat(desk.getActor().getIndex(), true);
                //System.out.println("tmpSeat-------------------------------：" + tmpSeat.getPlayer().getName());
                tmpCardValues = PokerHand.listen(tmpSeat.getStartingHand(), desk.getCommunityCards(), numberOfCommunityCards);
            }
        }
        
        if(tmpSeat != null){
            logger.debug("最大玩家："+tmpSeat.getPlayer().getName()+", 牌型："+ tmpCardValues.get(0).getPokerHand());
        }
        
        Action action = null;
        Seat actor = desk.getActor();
        List<CardValue> robotCardValues=null;
        if(round == Round.PRE_FLOP || round == Round.FLOP  || round == Round.TURN){
            int numberOfCommunityCards = 0;
            if(round == Round.FLOP){
                numberOfCommunityCards = 3;
            }else if(round == Round.TURN){
                numberOfCommunityCards = 4;
            }
            
            logger.debug("当前第几轮：" + round + ", 公共牌数："+numberOfCommunityCards);
            try {
                robotCardValues = PokerHand.listen(actor.getStartingHand(), desk.getCommunityCards(), numberOfCommunityCards);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if(robotCardValues == null){
                return;
            }
            CardValue robotCardValue = robotCardValues.get(0);
            CardValue playerCardValue = tmpCardValues.get(0);
            
            boolean flag = false;
            if(round == Round.FLOP || round == Round.TURN){
                /** npc牌是否成型*/
                boolean robotMolding = robotCardValue.getPokerHand().compareTo(PokerHand.STRAIGHT) <= 0;
                /** player牌是否成型*/
                boolean playerMolding = playerCardValue.getPokerHand().compareTo(PokerHand.STRAIGHT) <= 0;
                if(robotMolding && playerMolding){
                    if(robotCardValue.compareTo(playerCardValue) >= 0){
                        long betCount = desk.getMaxBets() - actor.getStake();
                        if(betCount == 0 && desk.getMaxBets() == 0){
                            action = ActionFactory.getInstance().getAction(Bet.NAME);
                            logger.debug("双方牌型成型时，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                        }else if(betCount == 0 && desk.getMaxBets() != 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("双方牌型成型时，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }
                        else if(betCount > 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("双方牌型成型时，牌桌筹码数大于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }
                        if(!action.isValid(actor)){
                            action = ActionFactory.getInstance().getAction(AllIn.NAME);
                            logger.debug("双方牌型成型时，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                        }
                    }else{
                        action = ActionFactory.getInstance().getAction(Fold.NAME);
                    }
                    flag = true;
                }else if(robotMolding){
                    float playerPokerHandWeight = 0f;
                    for(int idx=1; idx<tmpCardValues.size(); idx++){
                        CardValue cardValue = tmpCardValues.get(idx);
                        if(robotCardValue.compareTo(cardValue) < 0){
                            if(round == Round.FLOP){
                                playerPokerHandWeight +=  RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];            
                            }else if(round == Round.TURN){
                                playerPokerHandWeight += RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];                                   
                            }
                        }
                    }
                    //玩家提高后的牌可能大于npc
                    if(playerPokerHandWeight > 0f){
                        long betCount = desk.getMaxBets()  - actor.getStake();
                        if(betCount == 0 && desk.getMaxBets() == 0){
                            action = ActionFactory.getInstance().getAction(Bet.NAME);
                            logger.debug("机器人牌型成型时并且玩家提高后的牌大于机器人，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                        }else if(betCount ==0 && desk.getMaxBets() != 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("机器人牌型成型时并且玩家提高后的牌大于机器人，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }else if(betCount > 0){
                            action = ActionFactory.getInstance().getAction(Call.NAME);
                            logger.debug("机器人牌型成型时并且玩家提高后的牌大于机器人，牌桌筹码数大于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Call动作");
                        }
                        if(!action.isValid(actor)){
                            action = ActionFactory.getInstance().getAction(AllIn.NAME);
                            logger.debug("机器人牌型成型时并且玩家提高后的牌大于机器人，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                        }
                    }else{
                        long betCount = desk.getMaxBets()  - actor.getStake();
                        if(betCount == 0 && desk.getMaxBets() == 0){
                            action = ActionFactory.getInstance().getAction(Bet.NAME);
                            logger.debug("机器人牌型成型时并且玩家提高后的牌小于机器人，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                        }else if(betCount == 0 && desk.getMaxBets() != 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("机器人牌型成型时并且玩家提高后的牌小于机器人，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }else if(betCount > 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("机器人牌型成型时并且玩家提高后的牌小于机器人，牌桌筹码数大于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Rasie动作");
                        }
                        if(!action.isValid(actor)){
                            action = ActionFactory.getInstance().getAction(AllIn.NAME);
                            logger.debug("机器人牌型成型时并且玩家提高后的牌小于机器人，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                        }
                    }
                    flag = true;
                }else if(playerMolding){
                    action = ActionFactory.getInstance().getAction(Fold.NAME);
                    flag = true;
                }
                
            }
            
            //机器人与玩家比牌通用代码
            if(!flag){
                int result = robotCardValue.getPokerHand().compareTo(playerCardValue.getPokerHand());
                if(result == 0){
                    result = robotCardValue.getPokerHand().compare(robotCardValue.getCards(), playerCardValue.getCards());
                    if(result >= 0){//机器人大
                        long betCount = desk.getMaxBets() - actor.getStake();
                        if(betCount == 0 && desk.getMaxBets() == 0){
                            action = ActionFactory.getInstance().getAction(Bet.NAME);
                            logger.debug("牌型相同时机器人牌面大，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                        }else if(betCount == 0 && desk.getMaxBets() != 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("牌型相同时机器人牌面大，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }else if(betCount > 0){
                            action = ActionFactory.getInstance().getAction(Call.NAME);
                            logger.debug("牌型相同时机器人牌面大，牌桌筹码数大于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Call动作");
                        }
                        if(!action.isValid(actor)){
                             action = ActionFactory.getInstance().getAction(AllIn.NAME);
                             logger.debug("牌型相同时，机器人牌面大时，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                        }
                    }else{
                        int maxStake =  (int) (desk.getRoom().getBigBlindBets() * 2f);
                        if(tmpSeat.getStake() > maxStake){
                            action =  ActionFactory.getInstance().getAction(Fold.NAME);
                        }else{
                            if(desk.getMaxBets()  - actor.getStake() == 0){
                                action = ActionFactory.getInstance().getAction(Check.NAME);
                                logger.debug("牌型相同时机器人牌面小，牌桌筹码数等于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Check动作");
                            }else if(desk.getMaxBets()  - actor.getStake() > 0){
                                action = ActionFactory.getInstance().getAction(Call.NAME);
                                logger.debug("牌型相同时机器人牌面小，牌桌筹码数大于机器人当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Call动作");
                            }
                            if(!action.isValid(actor)){
                                action = ActionFactory.getInstance().getAction(AllIn.NAME);
                                logger.debug("牌型相同时，机器人牌面小，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                            }
                        }
                    }
                }else if(result < 0){ //机器人大
                    float playerPokerHandWeight = 0f;
                    for(int idx=1; idx < tmpCardValues.size(); idx++){
                        CardValue cardValue = tmpCardValues.get(idx);
                        if(robotCardValue.compareTo(cardValue) < 0){
                            //System.out.println("权重数组:RobotAiConstants.TWO_WEIGHT的size:"+RobotAiConstants.TWO_WEIGHT.length+", cardValue:"+cardValue+"的权重索引："+cardValue.getPokerHandWeightIndex());
                            if(round == Round.PRE_FLOP){
                                playerPokerHandWeight += RobotAiConstants.TWO_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.FLOP){
                                playerPokerHandWeight += RobotAiConstants.FIVE_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.TURN){
                                playerPokerHandWeight += RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }
                        }
                    }
                    
                    if(playerPokerHandWeight > 0f){
                        float robotPokerHandWeight = 0f;
                        for(int idx=1; idx< robotCardValues.size(); idx++){
                            CardValue cardValue = robotCardValues.get(idx);
                            if(round == Round.PRE_FLOP){
                                robotPokerHandWeight += RobotAiConstants.TWO_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.FLOP){
                                robotPokerHandWeight += RobotAiConstants.FIVE_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.TURN){
                                robotPokerHandWeight += RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }
                        }
                        
                        if(robotPokerHandWeight >= playerPokerHandWeight){
                            long betCount = desk.getMaxBets()  - actor.getStake();
                            if(betCount == 0 && desk.getMaxBets() == 0){
                                action = ActionFactory.getInstance().getAction(Bet.NAME);
                                logger.debug("机器人大时，机器人提高概率大于玩家提高概率并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                            }else if(betCount == 0 && desk.getMaxBets() != 0){
                                action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                                logger.debug("机器人大时，机器人提高概率大于玩家提高概率并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                            }else if(betCount > 0){
                                action = ActionFactory.getInstance().getAction(Call.NAME);
                                logger.debug("机器人大时，机器人提高概率大于玩家提高概率并且牌桌筹码数大于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Call动作");
                            }
                            if(!action.isValid(actor)){
                                action = ActionFactory.getInstance().getAction(AllIn.NAME);
                                logger.debug("机器人大时，机器人提高概率大于玩家提高概率，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                            }
                        }else{
//                            int maxStake = (int)(desk.getRoom().getBigBlindBets() * 10f);
//                            if(tmpSeat.getStake() > maxStake){
//                                action = ActionFactory.getInstance().getAction(Fold.NAME);
//                                //System.out.println("机器人大时，机器人提高概率小于玩家提高概率,但是maxStake="+ maxStake+"，玩家下过的注:"+tmpSeat.getStake()+">maxStake,当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Fold动作");
//                            }else{
                                if(desk.getMaxBets()  - actor.getStake() == 0){
                                    action = ActionFactory.getInstance().getAction(Check.NAME);
                                    logger.debug("机器人大时，机器人提高概率小于玩家提高概率并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Check动作");
                                }else if(desk.getMaxBets()  - actor.getStake() > 0){
                                    action = ActionFactory.getInstance().getAction(Call.NAME);
                                    logger.debug("机器人大时，机器人提高概率小于玩家提高概率并且牌桌筹码数大于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Call动作");
                                }
                                if(!action.isValid(actor)){
                                    action = ActionFactory.getInstance().getAction(AllIn.NAME);
                                    logger.debug("机器人大时，机器人提高概率小于玩家提高概率，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                                }
//                            }
                        }
                        
                    }else{//提高后的牌还是小于NPC当前的牌，一直加注到AllIn
                        long betCount = desk.getMaxBets()  - actor.getStake();
                        if(betCount == 0 && desk.getMaxBets() == 0){
                            action = ActionFactory.getInstance().getAction(Bet.NAME);
                            logger.debug("机器人大时，玩家提高后的牌还是小于机器人当前牌并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Bet动作");
                        }else if(betCount == 0 && desk.getMaxBets() != 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("机器人大时，玩家提高后的牌还是小于机器人当前牌并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }else if(betCount > 0){
                            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                            logger.debug("机器人大时，玩家提高后的牌还是小于机器人当前牌并且牌桌筹码数大于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Raise动作");
                        }
                        if(!action.isValid(actor)){//如果钱不够，那就AllIn
                            action = ActionFactory.getInstance().getAction(AllIn.NAME);
                            logger.debug("机器人大时，玩家提高后的牌还是小于机器人当前牌，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定AllIn动作");
                        }
                    }
                }else if(result > 0){ //机器人小
                    float robotPokerHandWeight = 0f;
                    for(int idx=1; idx< robotCardValues.size(); idx++){
                        CardValue cardValue = robotCardValues.get(idx);
                        if(cardValue.compareTo(playerCardValue) > 0){
                            if(round == Round.PRE_FLOP){
                                robotPokerHandWeight += RobotAiConstants.TWO_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.FLOP){
                                robotPokerHandWeight += RobotAiConstants.FIVE_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.TURN){
                                robotPokerHandWeight += RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }
                        }
                    }
                    
                    if(robotPokerHandWeight > 0f){
                        float playerPokerHandWeight = 0f;
                        for(int idx=1; idx< tmpCardValues.size(); idx++){
                            CardValue cardValue = tmpCardValues.get(idx);
                            if(round == Round.PRE_FLOP){
                                playerPokerHandWeight += RobotAiConstants.TWO_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.FLOP){
                                playerPokerHandWeight += RobotAiConstants.FIVE_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }else if(round == Round.TURN){
                                playerPokerHandWeight += RobotAiConstants.SIX_WEIGHT[cardValue.getPokerHandWeightIndex()];
                            }
                        }
                        
                        if(robotPokerHandWeight >= playerPokerHandWeight){
                            int maxStake = (int)(desk.getRoom().getBigBlindBets() * 1f);
                            if(tmpSeat.getStake() > maxStake){
                                action = ActionFactory.getInstance().getAction(Fold.NAME);
                            }else{
                                if(desk.getMaxBets()  - actor.getStake() == 0){
                                    action = ActionFactory.getInstance().getAction(Check.NAME);
                                    logger.debug("机器人小时，机器人提高概率大与玩家提高概率并且牌桌筹码数等于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备Check动作");
                                }else if(desk.getMaxBets()  - actor.getStake() > 0){
                                    action = ActionFactory.getInstance().getAction(Call.NAME);
                                    logger.debug("机器人小时，机器人提高概率大与玩家提高概率并且牌桌筹码数大于当前可下的筹码数，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 准备执行Call动作");
                                }
                                if(!action.isValid(actor)){
                                    action = ActionFactory.getInstance().getAction(AllIn.NAME);
                                    logger.debug("机器人小时，机器人提高概率大与玩家提高概率，当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定执行AllIn动作");
                                }
                            }
                        }else{
                            action = ActionFactory.getInstance().getAction(Fold.NAME);
                        }
                    }else{//提高后的牌还是小于玩家的当前牌
                        action = ActionFactory.getInstance().getAction(Fold.NAME);
                    }
                }
            }
            
        }else if(round == Round.RIVER){
            int numberOfCommunityCards = 5;
            logger.debug("当前第几轮：" + round + ", 公共牌数："+numberOfCommunityCards);
            robotCardValues = PokerHand.listen(actor.getStartingHand(), desk.getCommunityCards(), numberOfCommunityCards);
            CardValue robotCardValue = robotCardValues.get(0);
            CardValue playerCardValue = tmpCardValues.get(0);
            if(robotCardValue.compareTo(playerCardValue) >= 0){
                long betCount = desk.getMaxBets()  - actor.getStake();
                if(betCount == 0 && desk.getMaxBets() == 0){
                    action = ActionFactory.getInstance().getAction(Bet.NAME);
                    logger.debug("当前第几轮："+ round +"并且牌桌筹码数等于当前可下的筹码数,机器人：" + actor.getPlayer().getName() + ", 准备执行Bet动作");
                }else if(betCount == 0 && desk.getMaxBets() != 0){
                    action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                    logger.debug("当前第几轮："+ round +"并且牌桌筹码数等于当前可下的筹码数,机器人：" + actor.getPlayer().getName() + ", 准备执行Raise动作");
                }else if(betCount> 0){
                    action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
                    logger.debug("当前第几轮："+ round +"并且牌桌筹码数等于大于可下的筹码数,机器人：" + actor.getPlayer().getName() + ", 准备执行Raise动作");
                }
                if(!action.isValid(actor)){
                    action = ActionFactory.getInstance().getAction(AllIn.NAME);
                    logger.debug("当前第几轮："+ round +",机器人：" + actor.getPlayer().getName() + ", 确定执行AllIn动作");
                }
            }else{
                action = ActionFactory.getInstance().getAction(Fold.NAME);
            }
        }
        
        if(robotCardValues != null){
            logger.debug("触发action："+action.getName()+ ", 机器人："+desk.getActor().getPlayer().getName()+", 牌型：" + robotCardValues.get(0).getPokerHand());
        }
        desk.response(affectAction(action, actor, desk));
    }
    
    private static Action doSpecialAction(Desk desk, Seat actor){
        Action action = null;
        long betCount = desk.getMaxBets() - actor.getStake();
        if(betCount == 0 && desk.getMaxBets() == 0){
            action = ActionFactory.getInstance().getAction(Bet.NAME);
        }else if(betCount ==0 && desk.getMaxBets() != 0){
            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
        }else if(betCount > 0){
            action = ActionFactory.getInstance().getAction(Raise.NAME, desk.getMaxBets() * RandomUtil.random(2, 5));
        }
        if(!action.isValid(actor)){
            action = ActionFactory.getInstance().getAction(AllIn.NAME);
        }
        return action;
    }
    
    /***
     *  10%的概率影响 action
     * @param action
     * @param actor
     * @param desk
     * @return
     */
    private static Action affectAction(Action action, Seat actor, Desk desk){
        logger.debug("机器人"+actor.getPlayer().getName()+"被影响之前的动作：" + action );
        boolean isAffect = false;
        isAffect = RandomUtil.shake(0.1f);
        if(action.getName().equalsIgnoreCase(Call.NAME)){
            if(isAffect){
                isAffect = RandomUtil.shake(0.2f);
                if(isAffect){
                    action = ActionFactory.getInstance().getAction(AllIn.NAME);
                }else{
                    action = doSpecialAction(desk, actor);
                }
            }
        }else if(action.getName().equalsIgnoreCase(Check.NAME)){
            if(isAffect){
                isAffect = RandomUtil.shake(0.1f);
                if(isAffect){
                    action = ActionFactory.getInstance().getAction(AllIn.NAME);
                }else{
                    action = doSpecialAction(desk, actor);
                }
            }
        }else if(action.getName().equalsIgnoreCase(Fold.NAME)){
            if(isAffect){
                isAffect = RandomUtil.shake(0.1f);
                if(isAffect){
                    action = doSpecialAction(desk, actor);
                }else{
                    if(desk.getMaxBets()  - actor.getStake() == 0){
                        action = ActionFactory.getInstance().getAction(Check.NAME);
                    }else if(desk.getMaxBets()  - actor.getStake() > 0){
                        action = ActionFactory.getInstance().getAction(Call.NAME);
                    }
                    if(!action.isValid(actor)){
                        action = ActionFactory.getInstance().getAction(AllIn.NAME);
                    }
                }
            }
        }
        logger.debug("机器人"+actor.getPlayer().getName()+"影响之后的动作：" + action );
        return action;
    }
    
}
