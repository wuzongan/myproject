package com.kunlun.poker.center.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.center.data.EnglishNameWrapper;
import com.kunlun.poker.center.domain.EnglishName;
import com.kunlun.poker.center.domain.Robot;
import com.kunlun.poker.center.service.RobotService;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.util.RandomUtil;

@Service("robotService")
public class RobotServiceImpl implements RobotService{
	private int robotId;	
	private Map<Integer, Robot> robotWarehouse;
	@Autowired
	private RoomService roomService;
	@Autowired(required = true)
	private EnglishNameWrapper englishNameWrapper;
	
	private static long robotPotLimit = -1000000;
	private static long roboPotWinLimit = 1200000;
    private int robotNum;
    private long robotPot;

	public RobotServiceImpl() {
		robotWarehouse = new ConcurrentHashMap<Integer, Robot>();
		robotId = (int)Math.pow(2, 30);
		robotNum = 1;
	}
	
	public void  syncRobotPot(long supplyment){
		setRobotPot(supplyment);
	}
	
	private void setRobotPot(long supplyment){
		robotPot += supplyment;
	}
	
	public long getRobotPot(){
		return robotPot;
	}
	
	@Override
	public Robot getRobot(int robotId) {
		return robotWarehouse.get(robotId);
	}

	@Override
	public void removeRobot(int robotId) {
		robotWarehouse.remove(robotId);	
	}
	
	private int createRobotId(){
		robotId = robotId + 1;
		return robotId; 
	}
	
	private String createRobotName(){
		robotNum = robotNum + 1;
		String robotName = "Guest" + robotNum;
		return robotName;
	}
	
	@Override
	public Robot createRobot(int roomId) {
		int boyNum = englishNameWrapper.getCountBySex(0);
		int girlNum = englishNameWrapper.getCountBySex(1);
		
		Robot robot = new Robot();
		//设置Id
		robot.setId(createRobotId());
		//根据房间生成Ai
		int robotAi = calculateRobotAI(roomId);
		//设置机器人Ai
		if(robotPot <= robotPotLimit){
			robot.setAi(3);
		}else{
			if(robotPot >= roboPotWinLimit)
				robotPot = 0;
			robot.setAi(robotAi);
		}
		//根据房间生成筹码
		int robotBankRoll = calculateRobotBankRoll(roomId);
		//设置机器人的筹码
		robot.setBankroll(robotBankRoll);
		//生成性别
		int sex = RandomUtil.random(0, 1);
		//设置机器人性别
		robot.setSex(sex);
		//设置机器人的名字
		int randomName = RandomUtil.random(1, 10);
		EnglishName en;
		if(randomName >5){
			if(sex == 0){
				en = englishNameWrapper.getEnglishNameById(RandomUtil.random(1,boyNum));
				if(en != null){
					robot.setName(en.getName());
				}else{
					robot.setName(createRobotName());
				}
			}else{
				en = englishNameWrapper.getEnglishNameById(RandomUtil.random(1+boyNum,girlNum+boyNum));
				if(en != null){
					robot.setName(en.getName());
				}else{
					robot.setName(createRobotName());
				}
			}
		}else{
			robot.setName(createRobotName());
		}
		//随机头像内容
		int portraitNum = RandomUtil.random(1, 8);
		String portraitLocal = "defaultPortraits/";
		if(sex == 0){
			portraitLocal += "0/male"+portraitNum+".jpg";
		}else{
			portraitLocal += "1/female"+portraitNum+".jpg";
		}
		robot.setPortrait(portraitLocal);
		robot.setBestHand();
		//随机等级
		robot.setLevel(RandomUtil.random(1, 25));
		//生成赢取的筹码
		int winBankRoll = RandomUtil.random(50, 500000);
		winBankRoll = winBankRoll - winBankRoll%100;
		//设置生成的筹码
		robot.setSingleWinBankroll(winBankRoll);
		robot.setCardNum();
		robot.setWinCardNum();
		robotWarehouse.put(robotId, robot);
		return robot;
	}
	
	private int calculateRobotBankRoll(int roomId){
		Room room  = roomService.getRoomById(roomId);
		String robotBuyIn = room.getBuyInChipList();
		String value[] = robotBuyIn.split(",");
		return Integer.parseInt(value[4]);
	}
	private int calculateRobotAI(int roomId){
		Room room  = roomService.getRoomById(roomId);
		String robotProb = room.getRobotProb();
		String value[] = robotProb.split("_");
		int probOne = Integer.parseInt(value[0]);
		int probTwo = Integer.parseInt(value[2]);
		int AiOne = Integer.parseInt(value[1]);
		int AiTwo = Integer.parseInt(value[3]);
		int randomNum = RandomUtil.random(1, probOne + probTwo);
		if(randomNum >= probTwo){
			return AiOne;
		}else{
			return AiTwo;
		}
	}
}
