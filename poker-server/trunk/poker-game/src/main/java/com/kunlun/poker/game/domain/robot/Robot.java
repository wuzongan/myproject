package com.kunlun.poker.game.domain.robot;

import com.kunlun.poker.game.domain.Player;

public class Robot extends Player {

    private int ai;
    
    private long staticBankroll;
    
//    //临时代码
//    //自动补齐次数
//    public int autoPolishings;
//    //赢牌次数
//    public int winCount;
//    //牌型情况
//    public final Map<PokerHand, Integer> cardMap = new HashMap<PokerHand, Integer>();
//    //每隔100局以后每个NPC的筹码情况
//    public final List<Long> robotBankrollInfo = new ArrayList<Long>();

    public Robot() {
        this.setRobot(true);
    }
    
    public int getAi() {
        return ai;
    }

    public void setAi(int ai) {
        this.ai = ai;
    }

    public long getStaticBankroll() {
        return staticBankroll;
    }

    public void setStaticBankroll(long staticBankroll) {
        this.staticBankroll = staticBankroll;
    }
    
//    public String cardMapToString(){
//        StringBuilder sb = new StringBuilder();
//        for(Map.Entry<PokerHand, Integer> entry : cardMap.entrySet()){
//            sb.append("牌型：").append(entry.getKey()).append("出现过");
//        }
//        return sb.toString();
//    }
//    
//    public String listToString(){
//        StringBuilder sb = new StringBuilder();
//        int index =0;
//        for(long bankroll : robotBankrollInfo){
//            sb.append(bankroll);
//            if(index != 9){
//                sb.append("@");
//            }
//            index++;
//        }
//        return sb.toString();
//    }

//    @Override
//    public String toString() {
//        return "Robot [ai=" + ai + ", staticBankroll=" + staticBankroll
//                + ", autoPolishings(自动补齐次数)=" + autoPolishings + ", winCount(赢牌次数)="
//                + winCount + ", cardMap(牌型获得情况)=" + cardMapToString() +
//               ",robotBankrollInfo(每隔100局以后每个NPC的筹码情况)="+ listToString() + "]";
//    }
    
}
