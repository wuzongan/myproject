package com.kunlun.poker.game.service;

import com.googlecode.canoe.scheduler.Scheduler;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.domain.Player;
import com.kunlun.poker.game.domain.robot.Robot;


public interface RobotService {
    
    void join(Room room, Desk desk);
    
    void delayJoin(Scheduler scheduler, Room room, Desk desk, int robotCount);
    
    int randomRobot(Room room, Desk desk, Scheduler scheduler);
    
    void checkAndClearRobot(Desk desk);
    
    Robot applyRobot(Room room);
    
    void reponse(Desk desk, Player actor);
}
