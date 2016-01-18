package com.kunlun.poker.back.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.back.data.center.NoticeWrapper;
import com.kunlun.poker.back.service.NoticeService;
import com.kunlun.poker.domain.Notice;
import com.kunlun.poker.rmi.GmService;

@Service("noticeService")
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeWrapper noticeWrapper;

	@Autowired
	private GmService gmService;
	
	@Override
	public void addNotice(Notice notice) {
		noticeWrapper.addNotice(notice);
		gmService.notifyNotice();
	}

	@Override
	public void removeNotice(int id) {
		noticeWrapper.removeNotice(id);
		gmService.notifyNotice();
	}

	@Override
	public void updateNotice(Notice notice) {
		noticeWrapper.updateNotice(notice);
		gmService.notifyNotice();
	}

	@Override
	public List<Notice> listNotice(int from, int size) {
		return noticeWrapper.listNotices(from, size);
	}

	@Override
	public int countNotice() {
		return (int)noticeWrapper.countNotice();
	}

}
