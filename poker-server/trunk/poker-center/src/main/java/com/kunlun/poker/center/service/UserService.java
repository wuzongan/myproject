package com.kunlun.poker.center.service;

import java.util.Map;

import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.rmi.dto.PlayerDTO;

public interface UserService {
    void saveUser(User user);
    User getUserById(int id,  boolean withPlayers);
    User getUserByUid(String uid);
    void addPlayer(PlayerDTO player);
    void mergePlayer(PlayerDTO player, User user);
    void savePlayer(PlayerDTO player);
    void mergePlayers();
    Map<Integer,Integer> getAllRoomPlayerNums();
}
