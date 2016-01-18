package com.kunlun.poker.game.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.domain.GameType;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.Seat;
import com.kunlun.poker.game.domain.robot.Robot;
import com.kunlun.poker.game.domain.robot.RobotAi;
import com.kunlun.poker.game.service.RobotService;
import com.kunlun.poker.rmi.RMIExecutor;
import com.kunlun.poker.rmi.RobotRService;
import com.kunlun.poker.rmi.dto.PlayerDTO;
import com.kunlun.poker.util.RandomUtil;

@Component("robotService")
public class RobotServiceImpl implements RobotService {
    private static final Logger logger = LoggerFactory
            .getLogger(RobotServiceImpl.class);
    
    @Autowired(required = true)
    private RobotRService robotRService;
    
    @Autowired
    private RMIExecutor rmiExecutor;
    
    public RobotServiceImpl() {
    }
    
	@Override
    public Robot applyRobot(Room room){
	    Future<Robot> future = rmiExecutor.execute(() -> {
	           PlayerDTO playerDto = this.robotRService.applyJoinRoom(room.getId());
	           Robot robot = new Robot();
	           robot.setId(playerDto.getId());
	           robot.setName(playerDto.getName());
	           robot.setAi(playerDto.getRobotAi());
	           robot.setBankroll(playerDto.getBankroll());
	           robot.setStaticBankroll(playerDto.getBankroll());
	           robot.setPortrait(playerDto.getPortrait());
//	         robot.setAi(2);
//	         robot.setBankroll(10000);
//	         robot.setStaticBankroll(10000);
	           
	           return robot;
	       } , room.getId(), null, null, 0);
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
	
	private PlayerDTO robotConvertPlayerDto(Robot robot){
	    PlayerDTO player = new PlayerDTO();
        player.setBankroll(robot.getBankroll());
        player.setName(robot.getName());
        player.setId(robot.getId());
        player.setPortrait(robot.getPortrait());
        player.setAutoPlay(true);
        player.setRobotAi(robot.getAi());
        return player;
	}
	
	private void disposePlayer(Robot robot){
	    rmiExecutor.execute(() -> {
	        robotRService.disposePlayer(robotConvertPlayerDto(robot), robot.getBankroll() - robot.getStaticBankroll());     
        } , robot.getId(), null, null, 0);
	}
    
    @Override
    public void join(Room room, Desk desk){
        desk.accept(this.applyRobot(room), false);
    }
    
    @Override
    public void delayJoin(Scheduler scheduler, Room room, Desk desk, int robotCount) {
        scheduler.submit(() -> {
            for(int idx=0;idx<robotCount;idx++){
                this.join(room, desk);
            }
        }, null, desk.getId(),  RandomUtil.random(2000, 4000), TimeUnit.MILLISECONDS);
    }

    /**
     * 加入机器人
     */
    @Override
    public int randomRobot(Room room, Desk desk, Scheduler scheduler) {
        GameType gameType = room.getGameType();
        Collection<Player> players = desk.getPlayers();
        int needRobotCount=0;
        int needSameRobot=0;
        int playerCount = players.size() + desk.getRobots().size();
        if(gameType == GameType.ROYAL && playerCount <= 2){
            needRobotCount = RandomUtil.random(1, 3);
            needSameRobot = RandomUtil.random(1, needRobotCount);
        }else if(gameType == GameType.NORMAL && playerCount <= 2){
            needRobotCount = RandomUtil.random(2, 5);
            needSameRobot = RandomUtil.random(1, needRobotCount);
        }
        int remainRobot = needRobotCount - needSameRobot;
        logger.debug("随机需要同时进入牌桌机器人数:"+needSameRobot +",后续进入的机器人数:"+remainRobot+" 每隔2~4秒进入");
        if(remainRobot > 0){
            this.delayJoin(scheduler, room, desk, remainRobot);
        }
        return needSameRobot;
    }

    /**
     * 1.  机器人输没钱
     * 2.  机器人赢钱：5倍带入金额
     * 3.  牌桌人数大于7人：随机一个机器人退出（不会影响最高难度机器人）
     */
    @Override
    public void checkAndClearRobot(Desk desk) {
        Collection<Player> robots = desk.getRobots();
        
        //真人都站起，只剩下机器人
        boolean hasPlayerPlaying = false;
        for(Seat seat : desk.getSeats()){
            if(seat.getPlayer() != null && !seat.getPlayer().isRobot()){
                hasPlayerPlaying = true;
                break;
            }
        }
        if((desk.getPlayers().isEmpty() || !hasPlayerPlaying) &&  !robots.isEmpty()){
            for(Player robot : robots){
                desk.sendOff(robot, true);
                Robot _robot = (Robot) robot;
                //robotRService.disposePlayer(robotConvertPlayerDto(_robot), _robot.getBankroll() - _robot.getStaticBankroll());
                disposePlayer(_robot);
                logger.debug("机器人:"+robot.getName()+"退出牌桌，1它的筹码数量："+robot.getBankroll());
            }
            return;
        }

        for(Player robot : robots){
            Robot _robot = (Robot) robot;
            if(_robot.getBankroll() <= 0 || _robot.getStaticBankroll() * 5 < _robot.getBankroll()){
                desk.sendOff(_robot, true);
                //robotRService.disposePlayer(robotConvertPlayerDto(_robot), _robot.getBankroll() - _robot.getStaticBankroll());
                disposePlayer(_robot);
                logger.debug("机器人:"+robot.getName()+"退出牌桌，它的筹码数量："+robot.getBankroll());
            }
        }
        
        int numberOfSittings = desk.getNumberOfSittings();
        if(numberOfSittings >= 7 || numberOfSittings == desk.getRoom().getType()){
            List<Player>  randomRobots = new ArrayList<Player>(8);
            for(Player player : robots){
                Robot robot = (Robot) player;
                if(robot.getAi()  != 3){
                    randomRobots.add(player);
                }
            }
            if(!randomRobots.isEmpty()){
                try {
                    int randomIndex = RandomUtil.random(0, randomRobots.size() - 1);
                    Robot robot = (Robot) randomRobots.get(randomIndex);
                    desk.sendOff(robot, true);
                    //robotRService.disposePlayer(robotConvertPlayerDto(robot), robot.getBankroll() - robot.getStaticBankroll());
                    disposePlayer(robot);
                    logger.debug("randomRobot的长度：" + randomRobots.size());
                    logger.debug("牌桌人数大于7人，随机淘汰的机器人："+robot.getName()+"，它的ai等级:"+ robot.getAi());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        
    }

    @Override
    public void reponse(Desk desk, Player actor) {
        if(actor != null && actor.isRobot()){
           Robot robot = (Robot) actor;
           RobotAi robotAi = RobotAi.valueOf(robot.getAi());
           robotAi.process(desk);
        }
    }
    
}
