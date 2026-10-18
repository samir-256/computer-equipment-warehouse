package com.warehouse.controller;

import com.warehouse.common.Range;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.BiFunction;

final class ArgumentParser {

    private ArgumentParser() {
    }

    static int integer(String key, String text) {
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Parameter '" + key + "' must be a whole number, but was '" + text + "'", e);
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
                    "Parameter '" + key + "' must be a number, but was '" + text + "'", e);
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
        return range(options, minKey, maxKey, ArgumentParser::decimal);
    }

    static Range<Integer> integerRange(Map<String, String> options, String minKey, String maxKey) {
        return range(options, minKey, maxKey, ArgumentParser::integer);
    }

    private static <T> T bound(Map<String, String> options, String key, BiFunction<String, String, T> parser) {
        return options.containsKey(key) ? parser.apply(key, options.get(key)) : null;
    }

    private static <T extends Comparable<T>> Range<T> range(
            Map<String, String> options, String minKey, String maxKey, BiFunction<String, String, T> parser) {
        T min = bound(options, minKey, parser);
        T max = bound(options, maxKey, parser);
        if (min == null && max == null) {
            return null;
        }
        try {
            return new Range<>(min, max);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Parameter '" + minKey + "' must not be greater than '" + maxKey + "'", e);
        }
    }
}
