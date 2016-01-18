package com.kunlun.poker.domain;

public class SettingItem {
    private int id;
    private SettingValueType type;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SettingValueType getType() {
        return type;
    }

    public void setType(SettingValueType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
