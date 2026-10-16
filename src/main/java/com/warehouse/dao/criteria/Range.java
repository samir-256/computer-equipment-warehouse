package com.warehouse.dao.criteria;

import java.util.Objects;

public record Range<T extends Comparable<T>>(T from, T to) {

    public Range {
        if (from == null && to == null) {
            throw new IllegalArgumentException("Range needs at least one bound");
        }
        if (from != null && to != null && from.compareTo(to) > 0) {
            throw new IllegalArgumentException("Range start " + from + " must not be greater than end " + to);
        }
    }

    public boolean isIn(T value) {
        Objects.requireNonNull(value, "Value must not be null");
        boolean afterStart = from == null || from.compareTo(value) <= 0;
        boolean beforeEnd = to == null || value.compareTo(to) <= 0;
        return afterStart && beforeEnd;
    }
}
