/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.login.service;


import com.kunlun.poker.login.domain.User;

/**
 *
 * @author kl
 */
public interface UserService{
    public void addUser(User user);
    public User selectUserByName(String name);
    public User selectUserById(int id);
    public void updateUserName(User user);
    public User selectUserByUid(String id);
}
