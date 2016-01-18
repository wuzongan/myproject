package com.kunlun.poker.recharge.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.recharge.service.PictureService;
import com.kunlun.poker.rmi.UserRService;

@Service("pictureService")
public class PictureServiceImpl implements PictureService{
    @Autowired
    private UserRService userRService;
    
	@Override
	public boolean isLogin(int uid) {
		return userRService.isLogin(uid);
	}
 
}
