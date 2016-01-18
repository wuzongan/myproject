package com.kunlun.poker.center.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kunlun.poker.center.data.RoomWrapper;
import com.kunlun.poker.center.service.RoomService;
import com.kunlun.poker.domain.Room;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomWrapper roomWrapper;

    public RoomWrapper getRoomWrapper() {
        return roomWrapper;
    }

    public void setRoomWrapper(RoomWrapper roomWrapper) {
        this.roomWrapper = roomWrapper;
    }
    
    private Map<Integer, Room> roomMap;
    private List<Room> rooms;
    
    @Override
    public Room getRoomById(int id) {
        checkAndFetchAll();
        return roomMap.get(id);
    }

    @Override
    public List<Room> getAll() {
        checkAndFetchAll();
        if(rooms != null){
            return rooms;
        }
        return roomWrapper.getAllRoom();
    }
    
    private synchronized void checkAndFetchAll()
    {
        if(rooms != null) return;
        
        rooms = roomWrapper.getAllRoom();
        roomMap = new HashMap<>();
        for(Room room : rooms)
        {
            roomMap.put(room.getId(), room);
        }
    }
}
