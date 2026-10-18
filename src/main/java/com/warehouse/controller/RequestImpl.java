package com.warehouse.controller;

record RequestImpl(String requestString) implements Request {

    RequestImpl {
        if (requestString == null) {
            throw new IllegalArgumentException("Request text must not be null");
        }
    }
}
