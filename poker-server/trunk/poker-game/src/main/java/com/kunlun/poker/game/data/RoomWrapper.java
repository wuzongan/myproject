package com.kunlun.poker.game.data;


import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.domain.Room;


@Repository("roomWrapper")
public interface RoomWrapper {
    @Select("select * from room where id=#{id}")
    Room getRoomById(int id);
}
