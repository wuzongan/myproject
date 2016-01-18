package com.kunlun.poker.center.rmi;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.center.domain.Robot;
import com.kunlun.poker.center.service.RobotService;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.rmi.RobotRService;
import com.kunlun.poker.rmi.dto.PlayerDTO;

@Component("robotRService")
public class RoborRServiceImpl implements RobotRService{
	private static final Logger logger = LoggerFactory
			.getLogger(UserRServiceImpl.class);
	@Autowired(required = true)
	private Scheduler scheduler;
	@Autowired(required = true)
	private RoomService roomService;
	@Autowired(required = true)
	private RobotService robotService;
	int buyIn = 0;
	@Override
	public PlayerDTO applyJoinRoom(int roomId) {
		Robot robot = robotService.createRobot(roomId);
		if (robot != null) {
			Room room = roomService.getRoomById(roomId);
			if(room != null){
				buyIn = room.getMaxStake();
			}
			PlayerDTO player = robot.getPlayer();
			if (player.isAutoPlay() == false) {
				if (robot.getBankroll() >= buyIn) {
					robot.setBankroll(robot.getBankroll() - buyIn);
					player = new PlayerDTO();
					player.setRoomId(roomId);
					player.setBankroll(buyIn);
					player.setId(robot.getId());
					player.setPortrait(robot.getPortrait());
					player.setAutoPlay(true);
					player.setRobotAi(robot.getAi());
					robot.setPlayerProperty(player);
				}
			}
			player.setName(robot.getName());
			return player;
		}
		return null;
	}

	//处理机器人
	@Override
	public void disposePlayer(PlayerDTO player, long supplyment) {
		if (player == null)
			return;
		int robotId = player.getId();
		scheduler.submit(() -> {
			robotService.removeRobot(robotId);
			robotService.syncRobotPot(supplyment);
		}, player, player.getId());		
	}
	
	//根据机器人id获取实例
	@SuppressWarnings("unused")
	private Robot getRobotById(int robotId) {
		Robot robot = robotService.getRobot(robotId);
		return robot;
	}

	//金币结算
	public PlayerDTO syncPlayer(PlayerDTO player, long supplyment) {
		if (player == null)
			return null;
		int robotId = player.getId();
		Robot robot = robotService.getRobot(robotId);
		Future<PlayerDTO> future = scheduler.submit(() -> {
			long actualSupplyment = supplyment;
			if (actualSupplyment != 0) {
				long carrency = robot.getBankroll();
				if (carrency < actualSupplyment) {
					robot.setBankroll(0);
					actualSupplyment = carrency;
				} else {
					robot.setBankroll(carrency - actualSupplyment);
				}
				player.setBankroll((player.getBankroll() + actualSupplyment));
			}
			robot.setPlayerProperty(player);
		}, player, robotId);
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
