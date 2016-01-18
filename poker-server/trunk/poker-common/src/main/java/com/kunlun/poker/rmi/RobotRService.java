package com.kunlun.poker.rmi;

import com.kunlun.poker.rmi.dto.PlayerDTO;

public interface RobotRService {
	//加入房间
	PlayerDTO applyJoinRoom(int roomId);
	//处理player
	void disposePlayer(PlayerDTO player, long supplyment);
	//处理金钱
	PlayerDTO syncPlayer(PlayerDTO player, long supplyment);
}
