package com.kunlun.poker.center.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.center.data.ItemWrapper;
import com.kunlun.poker.center.domain.Item;
import com.kunlun.poker.center.service.ItemService;

@Service("itemService")
public class ItemServiceImpl implements ItemService{
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory
            .getLogger(UserServiceImpl.class);
    @Autowired(required = true)
    private ItemWrapper itemWrapper;

    
    @Override
    public List<Item> getAllItem() {
        return itemWrapper.getAllItem();
    }

    @Override
    public Item getItemById(int gpid) {
        return itemWrapper.getItemById(gpid) ;
    }

    @Override
    public Item getItemByCoin(int chips) {
        return itemWrapper.getItemByCoin(chips);
    }

}
