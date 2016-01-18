package com.kunlun.poker.login.data;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.login.domain.User;
@Repository("userWrapper")
public interface UserWrapper {
    @Insert( "insert into user (name, bankroll, uid, fb) values (#{name}, #{bankRoll}, #{uid }, #{fb})")
    @SelectKey(statement="select LAST_INSERT_ID()", keyProperty="id", before=false, resultType=int.class)
    int addUser(User user);

    @Select("select id, name, portrait from user where name=#{name}")
    User selectUserByName(String name);
    
    @Select("select id, name, portrait from user where id=#{id}")
    User selectUserById(int id);
    
    @Select("select id, name, portrait from user where uid=#{uid}")
    User selectUserByUid(String uid);
    
    @Update("update user set "
            + "name=#{name} "
            + "where id=#{id}")
    void saveUserName(User user);
}
