package com.kunlun.poker.center.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.rmi.dto.PlayerDTO;

@Repository("playerWrapper")
public interface PlayerWrapper {
    @Insert("INSERT INTO `player`(`id`, `roomId`, `bankroll`, `revenue`)"
            + " VALUES (#{id},#{roomId},#{bankroll},#{revenue})")
    void insertPlayer(PlayerDTO player);
    
    @Delete("DELETE FROM `player` where `id`=#{id} AND `roomId`=#{roomId}")
    void deletePlayer(PlayerDTO player);

    @Update("update user set bankroll = bankroll+ #{bankroll},  revenue=revenue + #{revenue} where id=#{id}")
    void mergePlayers(@Param("id") int id, @Param("bankroll") int bankroll, @Param("revenue") float revenue);
    
    @Select("SELECT id, sum(bankroll) bankroll, sum(revenue) revenue FROM `player` group by id")
    List<HashMap<String, Number>> selectPlayerTotals();
    
    @Delete("DELETE FROM `player`")
    void deleteAllPlayers();

    @Update("UPDATE `player` SET `bankroll`=#{bankroll},`revenue`=#{revenue} WHERE `id`=#{id} AND `roomId`=#{roomId}")
    void updatePlayer(PlayerDTO player);
    
    @Select("SELECT * FROM `player` WHERE `id`=#{id}")
    List<PlayerDTO> findPlayers(int userId);
    
    @Select("select  roomId,count(*)  as playerNums from player group by roomId")
    List<HashMap<String, Number>> getAllRoomPlayerNum();
}
