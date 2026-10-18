package com.warehouse.controller;

record ResponseImpl(String responseString, boolean isOk, boolean isOut) implements Response {

    static ResponseImpl ok(String text) {
        return new ResponseImpl(text, true, false);
    }

    static ResponseImpl error(String text) {
        return new ResponseImpl(text, false, false);
    }

    static ResponseImpl exit(String text) {
        return new ResponseImpl(text, true, true);
    }

    static ResponseImpl fatal(String text) {
        return new ResponseImpl(text, false, true);
    }
}
