package com.warehouse.common;

public final class Holder<T> {

    private final String name;
    private T value;

    public Holder(String name) {
        this.name = name;
    }

    public synchronized void init(T newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        if (value == null) {
            value = newValue;
        }
    }

    public synchronized T get() {
        if (value == null) {
            throw new IllegalStateException(name + " is not initialized");
        }
        return value;
    }
}
