package com.warehouse.controller;

import java.util.Optional;

record ResponseImpl(String responseString, boolean isOk, boolean isOut, Optional<Table> table)
        implements Response {

    static ResponseImpl ok(String text) {
        return new ResponseImpl(text, true, false, Optional.empty());
    }

    static ResponseImpl ok(String text, Table table) {
        return new ResponseImpl(text, true, false, Optional.of(table));
    }

    static ResponseImpl error(String text) {
        return new ResponseImpl(text, false, false, Optional.empty());
    }

    static ResponseImpl exit(String text) {
        return new ResponseImpl(text, true, true, Optional.empty());
    }

    static ResponseImpl fatal(String text) {
        return new ResponseImpl(text, false, true, Optional.empty());
    }
}
