package com.kunlun.poker.domain;

public enum AttrEnum {
    LEVEL(1),
    CURRENT_EXP(2),
    MAX_EXP(3),
    BANKROLL(4),
    ;
    private int type;
    AttrEnum(int type){
        this.type =  type;
    }
    public int getType(){
        return type;
    }
}
