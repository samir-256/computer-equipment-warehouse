package com.warehouse.dao.source;

import java.util.Arrays;

public final class CsvLine {

    private static final String DELIMITER = ";";

    private final String[] fields;

    private CsvLine(String[] fields) {
        this.fields = fields;
    }

    public static CsvLine of(String line) {
        if (line == null) {
            throw new IllegalArgumentException("Line must not be null");
        }
        return new CsvLine(line.split(DELIMITER, -1));
    }

    public int size() {
        return fields.length;
    }

    public String text(int index) {
        return fields[checked(index)].trim();
    }

    public int integer(int index) {
        String value = text(index);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Column " + (index + 1) + " must be an integer, but was '" + value + "'", e);
        }
    }

    public double decimal(int index) {
        String value = text(index);
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Column " + (index + 1) + " must be a number, but was '" + value + "'", e);
        }
    }

    public <T extends Enum<T>> T enumValue(int index, Class<T> type) {
        String value = text(index);
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Column " + (index + 1) + " must be one of "
                            + Arrays.toString(type.getEnumConstants()) + ", but was '" + value + "'", e);
        }
    }

    private int checked(int index) {
        if (index < 0 || index >= fields.length) {
            throw new IllegalArgumentException("Column " + (index + 1) + " does not exist");
        }
        return index;
    }
}
