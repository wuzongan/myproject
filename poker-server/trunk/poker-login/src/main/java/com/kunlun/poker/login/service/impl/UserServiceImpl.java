/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.login.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kunlun.poker.login.data.UserWrapper;
import com.kunlun.poker.login.domain.User;
import com.kunlun.poker.login.service.UserService;

/**
 *
 * @author kl
 */
@Service("userService")
public class UserServiceImpl implements UserService{
    @Autowired(required=true)
    private UserWrapper userWrapper;

    public UserWrapper getUserWrapper() {
        return userWrapper;
    }

    public void setUserWrapper(UserWrapper userWrapper) {
        this.userWrapper = userWrapper;
    }

    @Override
    public void addUser(User user) {
         userWrapper.addUser(user);
    }

    @Override
    public User selectUserByName(String name){
        return userWrapper.selectUserByName(name);
    }

    @Override
    public User selectUserById(int id) {
        return userWrapper.selectUserById(id);
    }
    
    @Override
    public User selectUserByUid(String uid){
        return userWrapper.selectUserByUid(uid);
    }

    @Override
    public void updateUserName(User user) {
        userWrapper.saveUserName(user);
    }
}
