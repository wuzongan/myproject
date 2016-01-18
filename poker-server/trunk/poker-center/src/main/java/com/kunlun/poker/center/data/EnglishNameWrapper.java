package com.kunlun.poker.center.data;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import com.kunlun.poker.center.domain.EnglishName;

@Repository("englishNameWrapper")
public interface EnglishNameWrapper {
    @Select("select * from english_name where id = #{id}")
    EnglishName getEnglishNameById(int id);
    
    @Select("select count(*) from english_name where sex = #{sex}")
    int getCountBySex(int sex);
}
