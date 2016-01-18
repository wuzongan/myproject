package com.kunlun.poker.center.data;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.domain.Room;

@Repository("roomWrapper")
public interface RoomWrapper {
    @Select("select * from room")
    List<Room> getAllRoom();
}
