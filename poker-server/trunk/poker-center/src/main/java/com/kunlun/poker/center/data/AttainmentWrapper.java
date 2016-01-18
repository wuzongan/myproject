package com.kunlun.poker.center.data;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import com.kunlun.poker.center.domain.UserAttainment;

@Repository("attainmentWrapper")
public interface AttainmentWrapper {

    @Select("select * from user_attainment where userId=#{userId}")
    UserAttainment selectUserAttainment(int userId);
    
    @Update("update user_attainment set attainmentIds=#{attainmentIds} where userId=#{userId}")
    boolean updateAttainment(@Param("userId")int userId, @Param("attainmentIds")String attainmentId);
    
    @Insert("insert into user_attainment(userId,attainmentIds) values(#{userId}, #{attainmentIds})")
    void insertAttainment(@Param("userId")int userId, @Param("attainmentIds")String attainmentId);
}
