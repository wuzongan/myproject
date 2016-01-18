package com.kunlun.poker.log;

public class LogMessage {
    private Object value;
    public LogMessage(){}
    public LogMessage(Object value) {
        this.value = value;
    }
    public Object getValue() {
        return value;
    }
    public void setValue(Object value) {
        this.value = value;
    }
    

}
