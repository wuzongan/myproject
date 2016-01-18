package com.kunlun.poker.recharge.domain;

public class RechargeParams {
    private String oid;
    private long uid;
    private float amount;
    private int coins;
    private int dtime;
    
    public String getOid() {
        return oid;
    }
    public void setOid(String oid) {
        this.oid = oid;
    }
    public long getUid() {
        return uid;
    }
    public void setUid(long uid) {
        this.uid = uid;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins) {
        this.coins = coins;
    }
    public int getDtime() {
        return dtime;
    }
    public void setDtime(int dtime) {
        this.dtime = dtime;
    }
}
