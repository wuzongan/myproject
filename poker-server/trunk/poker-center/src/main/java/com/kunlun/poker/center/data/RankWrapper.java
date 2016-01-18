package com.kunlun.poker.center.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.center.domain.User;

@Repository("rankWrapper")
public interface RankWrapper {
    @Select("select * from rank")
    List<HashMap<String, Number>> selectRanks();

    @Select("<script> select * from user where id in "
            + " <foreach item=\"item\" index=\"index\" collection=\"list\" open=\"(\" separator=\",\" close=\")\"> "
            + "#{item}</foreach> </script>")
    List<User> selectUsers(List<Integer> userIds);
 
    @Insert("insert into rank(userId,type) values(#{userId}, #{type})")
    void insertRank(@Param("userId")int userId, @Param("type")int type);
    
    @Update("update rank set userId=#{newUserId} where userId=#{oldUserId} and type=#{type}")
    boolean updateRank(@Param("newUserId")int newUserId, @Param("oldUserId")int oldUserId, @Param("type")int type);
}
