package com.kunlun.poker.center.service;

import java.util.List;

import com.kunlun.poker.domain.Room;

public interface RoomService {
    Room getRoomById(int id);

    List<Room> getAll();
}
