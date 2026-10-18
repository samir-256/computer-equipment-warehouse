package com.warehouse.controller;

import com.warehouse.entity.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

final class ResponseFormatter {

    static final String NOTHING_FOUND = "No products found.";

    private static final String[] HEADERS = {"ID", "Category", "Name", "Brand", "Price", "Qty", "Total", "Details"};
    private static final boolean[] RIGHT_ALIGNED = {true, false, false, false, true, true, true, false};

    private ResponseFormatter() {
    }

    static String format(List<Equipment> items) {
        if (items.isEmpty()) {
            return NOTHING_FOUND;
        }
        List<String[]> rows = new ArrayList<>();
        rows.add(HEADERS);
        items.forEach(item -> rows.add(row(item)));
        int[] widths = widths(rows);
        StringBuilder text = new StringBuilder();
        for (String[] row : rows) {
            text.append(line(row, widths)).append('\n');
        }
        text.append("Found ").append(items.size()).append(" product(s).");
        return text.toString();
    }

    private static String[] row(Equipment item) {
        return new String[] {
            String.valueOf(item.getId()),
            item.getClass().getSimpleName(),
            item.getName(),
            item.getBrand(),
            String.format(Locale.US, "%.2f", item.getPrice()),
            String.valueOf(item.getQuantity()),
            String.format(Locale.US, "%.2f", item.totalCost()),
            item.details()
        };
    }

    private static int[] widths(List<String[]> rows) {
        int[] widths = new int[HEADERS.length];
        for (String[] row : rows) {
            for (int column = 0; column < row.length; column++) {
                widths[column] = Math.max(widths[column], row[column].length());
            }
        }
        return widths;
    }

    private static String line(String[] row, int[] widths) {
        StringBuilder line = new StringBuilder();
        for (int column = 0; column < row.length; column++) {
            String pattern = RIGHT_ALIGNED[column] ? "%" + widths[column] + "s" : "%-" + widths[column] + "s";
            line.append(String.format(pattern, row[column]));
            if (column < row.length - 1) {
                line.append("  ");
            }
        }
        return line.toString().stripTrailing();
    }
}
