package com.warehouse.view;

import com.warehouse.controller.Table;
import com.warehouse.controller.Table.Alignment;
import com.warehouse.controller.Table.Cell;
import com.warehouse.controller.Table.Column;
import com.warehouse.controller.Table.Tone;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

final class ConsoleRenderer {

    private final BorderStyle borders;
    private final Style style;

    ConsoleRenderer(BorderStyle borders, Style style) {
        this.borders = borders;
        this.style = style;
    }

    String renderTable(Table table) {
        int[] widths = widths(table);
        List<Column> columns = table.columns();
        List<String> lines = new ArrayList<>();
        if (!table.title().isBlank()) {
            lines.add(style.title(" " + table.title()));
        }
        lines.add(rule(widths, borders.topLeft(), borders.topJoin(), borders.topRight()));
        lines.add(line(columns, headerCells(columns), widths, style::header));
        lines.add(rule(widths, borders.middleLeft(), borders.cross(), borders.middleRight()));
        table.rows().forEach(row -> lines.add(line(columns, row, widths, UnaryOperator.identity())));
        if (!table.footer().isEmpty()) {
            lines.add(rule(widths, borders.middleLeft(), borders.cross(), borders.middleRight()));
            lines.add(line(columns, table.footer(), widths, style::strong));
        }
        lines.add(rule(widths, borders.bottomLeft(), borders.bottomJoin(), borders.bottomRight()));
        return String.join("\n", lines);
    }

    String renderBox(List<String> texts) {
        int width = texts.stream().mapToInt(String::length).max().orElse(0);
        String horizontal = borders.horizontal().repeat(width + 2);
        List<String> lines = new ArrayList<>();
        lines.add(style.border(borders.topLeft() + horizontal + borders.topRight()));
        for (int i = 0; i < texts.size(); i++) {
            String padded = pad(texts.get(i), width, Alignment.LEFT);
            String content = i == 0 ? style.title(padded) : padded;
            lines.add(style.border(borders.vertical()) + " " + content + " " + style.border(borders.vertical()));
        }
        lines.add(style.border(borders.bottomLeft() + horizontal + borders.bottomRight()));
        return String.join("\n", lines);
    }

    private static List<Cell> headerCells(List<Column> columns) {
        return columns.stream().map(column -> Cell.of(column.header())).toList();
    }

    private static int[] widths(Table table) {
        int[] widths = new int[table.columns().size()];
        List<List<Cell>> all = new ArrayList<>(table.rows());
        all.add(headerCells(table.columns()));
        all.add(table.footer());
        for (List<Cell> cells : all) {
            for (int i = 0; i < cells.size(); i++) {
                widths[i] = Math.max(widths[i], cells.get(i).text().length());
            }
        }
        return widths;
    }

    private String rule(int[] widths, String left, String join, String right) {
        List<String> parts = new ArrayList<>();
        for (int width : widths) {
            parts.add(borders.horizontal().repeat(width + 2));
        }
        return style.border(left + String.join(join, parts) + right);
    }

    private String line(List<Column> columns, List<Cell> cells, int[] widths, UnaryOperator<String> base) {
        StringBuilder line = new StringBuilder(style.border(borders.vertical()));
        for (int i = 0; i < cells.size(); i++) {
            Cell cell = cells.get(i);
            String padded = pad(cell.text(), widths[i], columns.get(i).alignment());
            String styled = cell.tone() == Tone.WARNING ? style.danger(padded) : base.apply(padded);
            line.append(' ').append(styled).append(' ').append(style.border(borders.vertical()));
        }
        return line.toString();
    }

    private static String pad(String text, int width, Alignment alignment) {
        String spaces = String.join("", Collections.nCopies(width - text.length(), " "));
        return alignment == Alignment.RIGHT ? spaces + text : text + spaces;
    }
}
