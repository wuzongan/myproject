package com.kunlun.poker.center.domain.rank;

import com.kunlun.poker.center.domain.User;

public abstract class Rank<T extends Number, E extends Rank<T, E>> implements
Comparable<E> {
    private final User user;
    private final T value;

    public T getValue() {
        return value;
    }

    public Rank(User user) {
        this.user = user;
        value = initValue();
    }

    public User getUser() {
        return user;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass() == obj.getClass() && user.equals(((Rank<T, E>)obj).user);
    }

    abstract protected T initValue();
}
