package com.kunlun.poker.game.system;

import com.kunlun.poker.domain.Room;
import com.kunlun.poker.game.domain.Desk;
import com.kunlun.poker.game.service.DeskService;
import com.kunlun.poker.game.service.RobotService;
import com.kunlun.poker.game.service.RoomService;

public class RobotTestProcessor {

    private static final int DESK_COUNT = 200;
    private static final int DESK_ROBOT_COUNT = 5;
    private DeskService deskService;
    
    private RobotService robotService;
    
    private RoomService roomService;
    
    public void setDeskService(DeskService deskService) {
        this.deskService = deskService;
    }

    public void setRobotService(RobotService robotService) {
        this.robotService = robotService;
    }

    public void setRoomService(RoomService roomService) {
        this.roomService = roomService;
    }

    public void init(){
        Room room = roomService.getRoom();
        System.out.println("正在初始化：200个牌桌，1000个机器人，每个牌桌5个机器人。。。。。。。。。。。。。。。");
        
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        //1000个机器人，200牌桌
        for(int idx=0; idx < DESK_COUNT; idx++){
            Desk desk = deskService.fetchAndLockDesk();
            //desk.setSameInRobots(DESK_ROBOT_COUNT);
            for(int jdx=0; jdx < DESK_ROBOT_COUNT; jdx++){
                robotService.join(room, desk);
            }
        }
     
        System.out.println("机器人程序初始化完毕。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
    }
    
    
}
