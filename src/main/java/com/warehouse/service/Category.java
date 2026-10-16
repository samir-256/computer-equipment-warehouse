package com.warehouse.service;

import java.util.Arrays;
import java.util.List;

public enum Category {
    LAPTOPS(true, false),
    MONITORS(true, false),
    MICE(false, true),
    KEYBOARDS(false, true),
    ALL(true, true);

    private final boolean screenFilter;
    private final boolean connectionFilter;

    Category(boolean screenFilter, boolean connectionFilter) {
        this.screenFilter = screenFilter;
        this.connectionFilter = connectionFilter;
    }

    public boolean supportsScreenFilter() {
        return screenFilter;
    }

    public boolean supportsConnectionFilter() {
        return connectionFilter;
    }

    public List<Category> concreteTypes() {
        if (this == ALL) {
            return Arrays.stream(values()).filter(category -> category != ALL).toList();
        }
        return List.of(this);
    }
}
