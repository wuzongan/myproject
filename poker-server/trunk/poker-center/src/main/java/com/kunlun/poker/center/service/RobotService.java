package com.kunlun.poker.center.service;

import com.kunlun.poker.center.domain.Robot;

public interface RobotService {
	Robot getRobot(int robotId);
	void removeRobot(int robotId);
	Robot createRobot(int roomId);
	void syncRobotPot(long supplyment);
}
