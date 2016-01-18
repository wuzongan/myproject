package com.kunlun.poker.game.service;

import java.util.List;

import com.kunlun.poker.game.domain.Desk;


public interface DeskService {
    Desk fetchAndLockDesk(Desk igorn);
    Desk fetchAndLockDesk();
    void disposeAll();
    List<Desk> getDesks();
}
