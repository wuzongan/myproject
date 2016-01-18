package com.kunlun.poker.center.data;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.domain.Notice;


@Repository("noticeWrapper")
public interface NoticeWrapper {

    @Select("select * from notice")
    List<Notice> selectNotices();
    
    @Update("update user set noticeInfo=#{  noticeInfo} where id=#{userId}")
    void updateNoticeInfo(@Param("userId")int userId, @Param("noticeInfo")String noticeInfo);
}

