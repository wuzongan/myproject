package com.kunlun.poker.game.domain;

import com.kunlun.poker.game.domain.actions.AllIn;
import com.kunlun.poker.game.domain.actions.Bet;
import com.kunlun.poker.game.domain.actions.Call;
import com.kunlun.poker.game.domain.actions.Check;
import com.kunlun.poker.game.domain.actions.Fold;
import com.kunlun.poker.game.domain.actions.Raise;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 行为工厂类
 * @author Administrator
 */
public class ActionFactory {
    private static final ActionFactory instance = new ActionFactory();

    /**
     * 返回一个行为工厂类实例
     * @return the instance
     */
    public static ActionFactory getInstance() {
        return instance;
    }
    
    private final Map<String, Action> actionMap;
    /**
     * 无界线程安全队列raises
     */
    private final ConcurrentLinkedQueue<Raise> raises;

    /**
     * 构造函数，将实例存至HashMap中
     */
    private ActionFactory() {
        raises = new ConcurrentLinkedQueue<>();

        actionMap = new HashMap<>();
        actionMap.put(AllIn.NAME, new AllIn());
        actionMap.put(Call.NAME, new Call());
        actionMap.put(Check.NAME, new Check());
        actionMap.put(Fold.NAME, new Fold());
        actionMap.put(Bet.NAME, new Bet());
    }
    
    /**
     * 根据名字来获取动作，Action只是一个接口
     * @param name
     * @return 
     */
    public Action getAction(String name)
    {
        return getAction(name, 0);
    }
    
    /**
     * 获取操作和筹码数
     * @param name
     * @param numberOfChips
     * @return 
     */
    public Action getAction(String name, long numberOfChips)
    {
        if(actionMap.containsKey(name))
            return actionMap.get(name);
        else if(Raise.NAME.equals(name))
            return getRaise(numberOfChips);
        
        return null;
    }
    
    /**
     * Action的
     * @param action 
     */
    public void recycleAction(Action action)
    {
        //判断action是否为Raise的一个实例，如果是才可以执行回收的操作
        if(action instanceof Raise)
            recycleRaise((Raise)action);
    }

    private Raise getRaise(long numberOfChips){
        Raise raise = raises.poll();
        if(raise == null)
        {
            raise = new Raise();
            raise.setStake(numberOfChips);
        }
        
        return raise;
    }
    
    private void recycleRaise(Raise raise) {
        raises.offer(raise);
    }
}
