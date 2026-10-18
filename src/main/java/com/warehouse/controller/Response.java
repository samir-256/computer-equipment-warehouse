package com.warehouse.controller;

import java.util.Optional;

public interface Response {

    String responseString();

    boolean isOk();

    boolean isOut();

    default Optional<Table> table() {
        return Optional.empty();
    }
}
