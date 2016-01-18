package com.kunlun.poker.back.data.center;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.kunlun.poker.domain.Notice;


@Repository("noticeWrapper")
public interface NoticeWrapper {

	@Select("select * from notice order by id desc limit #{from}, #{size}")
	List<Notice> listNotices(@Param("from")int from, @Param("size")int size);
	
	@Delete("delete from notice where id=#{id}")
	void removeNotice(int id);
	
	@Insert("insert into `notice`(`id`, `title`, `content`, `createTime`) VALUES (#{id},#{title},#{content},#{createTime})")
	void addNotice(Notice notice);
	
	@Update("UPDATE `notice` SET `title`=#{title},`content`=#{content},`createTime`=#{createTime} WHERE `id`=#{id}")
	void updateNotice(Notice notice);
	
	@Select("select count(*) from `notice`")
	long countNotice();
}
