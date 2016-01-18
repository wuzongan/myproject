package com.kunlun.poker.back.domain;

public  abstract class AbstactLog<T extends Number> {

    protected T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
    
    abstract public void merge(AbstactLog<T> log);
}
