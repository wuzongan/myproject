package com.kunlun.poker.game.domain.robot;

import com.kunlun.poker.game.domain.Action;
import com.kunlun.poker.game.domain.ActionFactory;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Round;
import com.kunlun.poker.game.domain.actions.Bet;
import com.kunlun.poker.game.domain.actions.Call;
import com.kunlun.poker.game.domain.actions.Check;
import com.kunlun.poker.game.domain.actions.Fold;
import com.kunlun.poker.game.domain.robot.genre.RobotLevelOne;
import com.kunlun.poker.game.domain.robot.genre.RobotLevelThree;
import com.kunlun.poker.util.RandomUtil;

/***
 *  三种机器人类
 * @author ljx
 */
public enum RobotAi {
    
    ONE{
        @Override
        public void process(Desk desk) {
        	RobotLevelOne robotOne = new  RobotLevelOne();
        	robotOne.process(desk);
        }
        
    },
    
    TWO{
        @Override
        public void process(Desk desk) {
           RobotAiProcessor.processTwo(desk);
        }
        
    },
    
    THREE{
        @Override
        public void process(Desk desk) {
        	RobotLevelThree robotThree = new  RobotLevelThree();
        	robotThree.process(desk);
        }
        
    },
    
    /**
     * 测试
     */
    TEST{
        @Override
        public void process(Desk desk) {
            
            if(desk.getActor().getPlayer().isRobot()){
                Round round = desk.getRound();
                String actionName = Fold.NAME;
                if(round == Round.PRE_FLOP){
                    actionName = Call.NAME;
                }else if(round == Round.FLOP
                         || round == Round.TURN
                         || round == Round.RIVER){
                    actionName = Bet.NAME;
                    if(RandomUtil.shake(0.5f)){
                        actionName = Check.NAME;
                    }
                }
                
                Action action = ActionFactory.getInstance().getAction(actionName, desk.getRoom().getSmallBlindBets());
                System.out.println("触发action："+action.getName()+ ", 机器人："+desk.getActor().getPlayer().getName());
                desk.response(action);
                System.out.println("机器人："+ desk.getActor().getPlayer().getName()+", 下注："+ desk.getRoom().getSmallBlindBets());
            
            }
        }
    }
    
    ;
    
    
    public static RobotAi valueOf(int index){
        switch (index) {
        case 1:
            return ONE;
        case 2:
            return TWO;
        case 3:
            return THREE;
         default:
             return TEST;
        }
        
    }
    
    abstract public void process(Desk desk);
}
