package com.warehouse.controller;

import java.util.List;
import java.util.Objects;

public record Table(String title, List<Column> columns, List<List<Cell>> rows, List<Cell> footer) {

    public enum Alignment {
        LEFT,
        RIGHT
    }

    public enum Tone {
        NORMAL,
        WARNING
    }

    public record Column(String header, Alignment alignment) {

        public Column {
            Objects.requireNonNull(header, "Header must not be null");
            Objects.requireNonNull(alignment, "Alignment must not be null");
        }
    }

    public record Cell(String text, Tone tone) {

        public Cell {
            Objects.requireNonNull(text, "Text must not be null");
            Objects.requireNonNull(tone, "Tone must not be null");
        }

        public static Cell of(String text) {
            return new Cell(text, Tone.NORMAL);
        }

        public static Cell warning(String text) {
            return new Cell(text, Tone.WARNING);
        }
    }

    public Table {
        Objects.requireNonNull(title, "Title must not be null");
        if (columns == null || columns.isEmpty()) {
            throw new IllegalArgumentException("Table needs at least one column");
        }
        int width = columns.size();
        columns = List.copyOf(columns);
        rows = rows.stream().map(row -> checkWidth(row, width)).map(List::copyOf).toList();
        footer = footer.isEmpty() ? List.of() : List.copyOf(checkWidth(footer, width));
    }

    private static List<Cell> checkWidth(List<Cell> cells, int width) {
        if (cells.size() != width) {
            throw new IllegalArgumentException(
                    "Row has " + cells.size() + " cells, but the table has " + width + " columns");
        }
        return cells;
    }
}
