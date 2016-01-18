/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kunlun.poker.login.domain;

import com.googlecode.canoe.core.session.Role;

/**
 *
 * @author kl
 */

/**
 * 角色头像、昵称、游戏Id、等级、称号、经验条、当前货币数量、救济金、新手盲注
 * @author Administrator
 * 
 */
public class User implements Role{
    private int id;
    private String uid;
    private String name;
    private int portrait;
    private int bankRoll;
    private int fb;
   


    /**
     * User构造函数
     * 
     */
    public User()
    {
    }
    
    /**
     * 获取角色id
     * @return 
     */
    @Override
    public int getId() {
        return id; //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * 设置角色id
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * 获取角色名称（玩家的昵称）
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置角色名称（玩家的昵称）
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取玩家头像
     * @return the portrait
     */
    public int getPortrait() {
        return portrait;
    }

    /**
     * 设置玩家头像
     * @param portrait2 the portrait to set
     */
    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }
    
    public int getBankRoll() {
        return bankRoll;
    }

    public void setBankRoll(int bankRoll) {
        this.bankRoll = bankRoll;
    }
    
    /**
     * 昆仑sdk返回uid
     * @return
     */
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
    
    /**
     * 是否绑定fb
     * @return
     */
    public int getFb() {
        return fb;
    }

    public void setFb(int fb) {
        this.fb = fb;
    }

    /**
     * 玩家称号信息
     * @return String
     */
    @Override
    public String toString()
    {
        return "[玩家]" + name;
    }
}