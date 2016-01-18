package com.kunlun.poker.center.domain.config;

import com.googlecode.canoe.event.EventDispatcherAdapter;

public class AttainmentConfig extends EventDispatcherAdapter<AttainmentConfig>{
    private int id;
    private int type;
    private String description;
    private String condition;
    private int prizeId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public int getPrizeId() {
        return prizeId;
    }
    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    
}
