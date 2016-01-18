package com.kunlun.poker.rmi;

import com.kunlun.poker.exception.GameException;
import com.kunlun.poker.rmi.dto.PlayerDTO;

public interface UserRService {
    PlayerDTO applyJoinRoom(int sessionId, int buyIn, int roomId) throws GameException;
    void disposePlayer(PlayerDTO player);
    PlayerDTO syncPlayer(PlayerDTO player, long supplyment);
    void login(int sessionId, int userId);
    boolean isLogin(int userId);
    //前期订制的，只能根据返回的金币数去匹配商城里的物品
    void buyChip(String uid, int golds);
    boolean giveDealerChip(PlayerDTO player, long supplyment);
}
