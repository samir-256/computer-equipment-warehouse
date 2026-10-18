package com.warehouse.controller;

public final class RequestFactory {

    private RequestFactory() {
    }

    public static Request create(String text) {
        return new RequestImpl(text);
    }
}
