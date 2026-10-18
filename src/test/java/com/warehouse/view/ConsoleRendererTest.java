package com.warehouse.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.Table;
import com.warehouse.controller.Table.Alignment;
import com.warehouse.controller.Table.Cell;
import com.warehouse.controller.Table.Column;
import java.util.List;
import org.junit.jupiter.api.Test;

class ConsoleRendererTest {

    private static final List<Column> COLUMNS = List.of(
            new Column("ID", Alignment.RIGHT), new Column("Name", Alignment.LEFT));

    private static final List<List<Cell>> ROWS = List.of(
            List.of(Cell.of("1"), Cell.of("Alpha")),
            List.of(Cell.of("12"), Cell.of("B")));

    private static final Table TABLE = new Table("Items", COLUMNS, ROWS, List.of(Cell.of(""), Cell.of("Total")));

    private static ConsoleRenderer renderer(BorderStyle borders, boolean color) {
        return new ConsoleRenderer(borders, new Style(color));
    }

    @Test
    void asciiTableWithTitleAndFooter() {
        String expected = String.join("\n",
                " Items",
                "+----+-------+",
                "| ID | Name  |",
                "+----+-------+",
                "|  1 | Alpha |",
                "| 12 | B     |",
                "+----+-------+",
                "|    | Total |",
                "+----+-------+");

        assertEquals(expected, renderer(BorderStyle.ASCII, false).renderTable(TABLE));
    }

    @Test
    void unicodeTableUsesBoxCharacters() {
        String[] lines = renderer(BorderStyle.UNICODE, false).renderTable(TABLE).split("\n");

        assertEquals("┌────┬───────┐", lines[1]);
        assertEquals("│ ID │ Name  │", lines[2]);
        assertEquals("├────┼───────┤", lines[3]);
        assertEquals("└────┴───────┘", lines[8]);
    }

    @Test
    void tableWithoutTitleAndFooterHasNoExtraLines() {
        Table table = new Table("", COLUMNS, ROWS, List.of());

        String[] lines = renderer(BorderStyle.ASCII, false).renderTable(table).split("\n");

        assertEquals(6, lines.length);
        assertEquals("+----+-------+", lines[0]);
        assertEquals("+----+-------+", lines[5]);
    }

    @Test
    void numbersAreAlignedRightAndTextLeft() {
        String[] lines = renderer(BorderStyle.ASCII, false).renderTable(TABLE).split("\n");

        assertEquals("|  1 | Alpha |", lines[4]);
        assertEquals("| 12 | B     |", lines[5]);
    }

    @Test
    void longCellWidensTheColumn() {
        Table table = new Table("", COLUMNS,
                List.of(List.of(Cell.of("1"), Cell.of("A very long name"))), List.of());

        String[] lines = renderer(BorderStyle.ASCII, false).renderTable(table).split("\n");

        assertEquals("| ID | Name             |", lines[1]);
        assertEquals("|  1 | A very long name |", lines[3]);
    }

    @Test
    void plainOutputHasNoColorCodes() {
        assertFalse(renderer(BorderStyle.ASCII, false).renderTable(TABLE).contains("\u001B"));
    }

    @Test
    void colorOutputPaintsHeaderTitleAndBorders() {
        String text = renderer(BorderStyle.ASCII, true).renderTable(TABLE);

        assertTrue(text.contains("\u001B[1;36mID\u001B[0m") || text.contains("\u001B[1;36m ID\u001B[0m"));
        assertTrue(text.contains("\u001B[1;33m Items\u001B[0m"));
        assertTrue(text.contains("\u001B[90m+----+-------+\u001B[0m"));
    }

    @Test
    void warningCellIsRed() {
        Table table = new Table("", COLUMNS,
                List.of(List.of(Cell.warning("0"), Cell.of("x"))), List.of());

        String text = renderer(BorderStyle.ASCII, true).renderTable(table);

        assertTrue(text.contains("\u001B[31m"));
        assertFalse(renderer(BorderStyle.ASCII, false).renderTable(table).contains("\u001B"));
    }

    @Test
    void asciiBoxFramesAllLines() {
        String expected = String.join("\n",
                "+-----------+",
                "| Title     |",
                "| Longer ok |",
                "+-----------+");

        assertEquals(expected,
                renderer(BorderStyle.ASCII, false).renderBox(List.of("Title", "Longer ok")));
    }

    @Test
    void boxTitleIsPaintedWhenColorIsOn() {
        String text = renderer(BorderStyle.ASCII, true).renderBox(List.of("Title", "Line"));

        assertTrue(text.contains("\u001B[1;33mTitle\u001B[0m"));
    }
}
