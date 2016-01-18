package com.kunlun.poker.game.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.Config;
import com.kunlun.poker.domain.Room;
import com.kunlun.poker.game.data.RoomWrapper;
import com.kunlun.poker.game.service.RoomService;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
	@Autowired
	RoomWrapper roomWrapper;
	private volatile Room room;
	
	public void initRoom()
	{
        room = roomWrapper.getRoomById(Config.getInstance().getServerId());
	}

	@Override
    public Room getRoom() {
		return room;
	}

}
