 package com.kunlun.poker.center.rmi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kunlun.poker.center.service.NoticeService;
import com.kunlun.poker.rmi.GmService;

@Component("gmService")
public class GmServiceImpl implements GmService {
    
    @Autowired
    private NoticeService noticeService;

    @Override
    public void notifyNotice() {
        noticeService.loadNotices(true);
    }


}
