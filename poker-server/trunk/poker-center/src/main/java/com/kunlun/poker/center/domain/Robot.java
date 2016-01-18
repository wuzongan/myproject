package com.kunlun.poker.center.domain;

import java.util.Arrays;

import com.kunlun.poker.rmi.dto.PlayerDTO;
import com.kunlun.poker.util.RandomUtil;

public class Robot extends Actor<Robot>{
	private final PlayerDTO player;
	private int ai;
	private String bestHand;
	private String attainment;
	/**
	 * 最大牌型
	 */
	public String getBestHand() {
		return bestHand;
	}

	/**
	 * 随机生成牌面，最小为顺子
	 */
	public void setBestHand() {
		int suit;
		int card;
		int pattern = RandomUtil.random(1, 5);
		//确定牌型
		switch(pattern){
		//顺子
			case 1:
				//确定所含牌面
 				card = RandomUtil.random(1, 13);
				int cardCopy = card;
				int bestCard[] = new int[5];
				for(int i = 0; i<5; i++){
					if(card+i > 13){
						cardCopy = card;
						for(int j=0;j<(5-i);j++){
							cardCopy -= 1;
							bestCard[j+i] =  cardCopy;
						}
						break;
					}else{
						bestCard[i] = cardCopy;
						cardCopy += 1;
						
					}
				}
				bestHand = "";
				Arrays.sort(bestCard);
				int index = 0;
				for(int i : bestCard){
					suit = RandomUtil.random(1, 4);
					if(index == 0){
						bestHand = suit+"_"+i;
					}else{
						bestHand = bestHand +","+suit+"_"+i;
					}
					index++;
				}
				break;
			//同花
			case 2:
				bestHand = "";
				suit = RandomUtil.random(1, 4);
				for(int i = 0; i<5; i++){
					int cardNum = RandomUtil.random(i*2+1, (i+1)*2);
					if(i == 4){
						bestHand = bestHand+suit+"_"+cardNum;
					}else{
						bestHand = bestHand+suit+"_"+cardNum+",";
					}
				}
				break;
			//葫芦
			case 3:
				suit = RandomUtil.random(1, 4);
				int a = RandomUtil.random(1, 13);
				int b = 0;
				if(a >7){
					b = a - 5;
				}else{
					b = a + 4;
				}
				bestHand = 1+"_"+a+","+2+"_"+a+","+3+"_"+a+","+1+"_"+b+","+2+"_"+b;
				break;
			//四条
			case 4:
				a = RandomUtil.random(1, 13);
				b = RandomUtil.random(1, 13);
				if(a == b){
					b = b +1;
				}else if(b >13){
					b = b -2;
				}
				bestHand = 1+"_"+a+","+2+"_"+a+","+3+"_"+a+","+4+"_"+a+","+3+"_"+b;
				break;
			//同花顺
			case 5:
				//确定所含牌面
				card = RandomUtil.random(1, 13);
				cardCopy = card;
				int bestCardInfo[] = new int[5];
				for(int i = 0; i<5; i++){
					if(card+i > 13){
						cardCopy = card;
						for(int j=0;j<(5-i);j++){
							cardCopy -= 1;
							bestCardInfo[j+i] =  cardCopy;
						}
						break;
					}else{
						bestCardInfo[i] = cardCopy;
						cardCopy += 1;
						
					}
				}
				bestHand = "";
				Arrays.sort(bestCardInfo);
				index = 0;
				suit = RandomUtil.random(1, 4);
				for(int i : bestCardInfo){
					if(index == 0){
						bestHand = suit+"_"+i;
					}else{
						bestHand = bestHand +","+suit+"_"+i;
					}
					index++;
				}
				break;
			
			default:
					bestHand = "1_1,1_2,1_3,1_4,1_5";
		}
	}

	/**
	 * 排行榜信息
	 * @return
	 */
	public String getAttainment() {
		return attainment;
	}

	public void setAttainment(String attainment) {
		this.attainment = attainment;
	}

	public PlayerDTO getPlayer() {
		return player;
	}

	public Robot(){
		player = new PlayerDTO();
	}
	
    public void setPlayerProperty(PlayerDTO player) {
        PlayerDTO oldPlayer = player;
        if (oldPlayer != null) {
            long winBankroll = player.getBankroll() - oldPlayer.getBankroll();
            this.setSingleWinBankroll(winBankroll);
        }
        this.setDealerTips(dealerTips + player.getDealerTips());
        this.setCardNum(cardNum + 1);
        if(player.isWin()){
            this.setWinCardNum(winCardNum + 1);
        }
    }
    
    public Robot createRobot(int roomId){
    	Robot robot = new Robot();
		return robot;
    }

	public int getAi() {
		return ai;
	}

	public void setAi(int ai) {
		this.ai = ai;
	}
	
	public void setBankroll(){
		bankroll = RandomUtil.random(50,500000);
	}
	
	public long getBankroll(){
		return bankroll;
	}
	
	public void setCardNum(){
		cardNum = RandomUtil.random(200, 50000);
	}
	
	public int getCardNum(){
		return cardNum;
	}
	
	public void setWinCardNum(){
		winCardNum = RandomUtil.random(1, cardNum);
	}
	
	public int getWinCardNum(){
		return winCardNum;
	}
	
}
