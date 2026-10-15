package com.warehouse.entity;

import java.util.regex.Pattern;

final class Validation {

    private Validation() {
    }

    static int positive(int value, String field) {
        if (value <= 0) {
            throw new IllegalArgumentException(field + " must be positive, but was " + value);
        }
        return value;
    }

    static double positive(double value, String field) {
        if (!Double.isFinite(value) || value <= 0) {
            throw new IllegalArgumentException(field + " must be positive, but was " + value);
        }
        return value;
    }

    static int notNegative(int value, String field) {
        if (value < 0) {
            throw new IllegalArgumentException(field + " must not be negative, but was " + value);
        }
        return value;
    }

    static String notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    static String matches(String value, Pattern pattern, String field) {
        String text = notBlank(value, field);
        if (!pattern.matcher(text).matches()) {
            throw new IllegalArgumentException(field + " has wrong format: " + text);
        }
        return text;
    }

    static <E> E notNull(E value, String field) {
        if (value == null) {
            throw new IllegalArgumentException(field + " must not be null");
        }
        return value;
    }
}
