package com.kunlun.poker.center.service;

import java.util.List;

import com.kunlun.poker.center.domain.Item;

public interface ItemService {
    List<Item> getAllItem();
    Item getItemById(int gpid);
    Item getItemByCoin(int chips);
}
