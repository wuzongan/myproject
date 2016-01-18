package com.kunlun.poker.center.service;

import java.util.List;

import com.kunlun.poker.center.domain.User;
import com.kunlun.poker.domain.Notice;

public interface NoticeService {

    List<Notice> loadNotices(boolean isBroadcast);
    
    List<Notice> getNotices();
    
    void notifyNewNotice(User user);
    
    boolean whetheRead(User user, int noticeId);
    
    boolean whetheDel(User user, int noticeId);
    
    boolean queryNoticeContent(User user, int noticeId);
    
    boolean delNotice(User user, int noticeId);
    
    void login(User user);
    
    void exit(User user);
    
    void stop();
}
