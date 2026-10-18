package com.warehouse.controller;

import com.warehouse.common.Range;
import java.util.Map;
import java.util.TreeSet;

final class ArgumentParser {

    private ArgumentParser() {
    }

    static int integer(String key, String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Parameter '" + key + "' must be a whole number, but was '" + text + "'");
        }
    }

    static double decimal(String key, String text) {
        try {
            double value = Double.parseDouble(text.trim());
            if (!Double.isFinite(value)) {
                throw new NumberFormatException(text);
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Parameter '" + key + "' must be a number, but was '" + text + "'");
        }
    }

    static <T> T choice(String key, String text, Map<String, T> options) {
        T value = options.get(text.trim().toLowerCase());
        if (value == null) {
            throw new IllegalArgumentException("Parameter '" + key + "' has wrong value '" + text
                    + "'. Allowed: " + String.join(", ", new TreeSet<>(options.keySet())));
        }
        return value;
    }

    static Range<Double> decimalRange(Map<String, String> options, String minKey, String maxKey) {
        Double min = optionalDecimal(options, minKey);
        Double max = optionalDecimal(options, maxKey);
        return range(min, max, minKey, maxKey);
    }

    static Range<Integer> integerRange(Map<String, String> options, String minKey, String maxKey) {
        Integer min = options.containsKey(minKey) ? integer(minKey, options.get(minKey)) : null;
        Integer max = options.containsKey(maxKey) ? integer(maxKey, options.get(maxKey)) : null;
        return range(min, max, minKey, maxKey);
    }

    private static Double optionalDecimal(Map<String, String> options, String key) {
        return options.containsKey(key) ? decimal(key, options.get(key)) : null;
    }

    private static <T extends Comparable<T>> Range<T> range(T min, T max, String minKey, String maxKey) {
        if (min == null && max == null) {
            return null;
        }
        try {
            return new Range<>(min, max);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Parameter '" + minKey + "' must not be greater than '" + maxKey + "'");
        }
    }
}
