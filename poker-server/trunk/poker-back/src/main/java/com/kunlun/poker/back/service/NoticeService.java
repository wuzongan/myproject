package com.kunlun.poker.back.service;

import java.util.List;

import com.kunlun.poker.domain.Notice;

public interface NoticeService {
	void addNotice(Notice notice);
	void removeNotice(int id);
	void updateNotice(Notice notice);
	List<Notice> listNotice(int from, int size);
	int countNotice();
}
