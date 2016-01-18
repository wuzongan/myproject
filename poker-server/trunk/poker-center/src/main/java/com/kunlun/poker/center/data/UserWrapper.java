package com.kunlun.poker.center.data;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import com.kunlun.poker.center.domain.User;

@Repository("userWrapper")
public interface UserWrapper {
    @Update("update user set "
            + "name=#{name}, "
            + "exp=#{exp}, "
            + "level=#{level}, "
            + "bankroll=#{bankroll}, "
//            + "benefits=#{benefits}, "
            + "revenue=#{revenue}, "
            +"cardNum=#{cardNum}, "
            +"winCardNum=#{winCardNum}, "
            +"singleWinBankroll=#{singleWinBankroll}, "
            +"lastLoginTime=#{lastLoginTime}, "
            +"lastGetBankrollTime=#{lastGetBankrollTime}, "
            +"bestCards=#{bestCards},  "
            +"dealerTips=#{dealerTips} "
            + "where id=#{id}")
    void saveUser(User user);

    @Select("select * from user where id=#{id}")
    User getUserById(int id);

    @Select("select * from user where name = #{name}")
    User getUserByName(String name);
    
    @Select("select * from user where uid = #{uid}")
    User getUserByUid(String uid);
}
