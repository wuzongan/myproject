package com.kunlun.poker.center.data;

import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.center.domain.Item;

@Repository("itemWrapper")
public interface ItemWrapper {
    
    @Select("select * from item")
    List<Item> getAllItem();
    
    @Select("select * from item where id=#{id}")
    Item getItemById(int id);

    @Select("select * from item where chips = #{coin}")
    Item getItemByCoin(int coin);
}
