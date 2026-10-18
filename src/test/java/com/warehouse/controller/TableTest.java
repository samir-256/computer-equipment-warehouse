package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.Table.Alignment;
import com.warehouse.controller.Table.Cell;
import com.warehouse.controller.Table.Column;
import java.util.List;
import org.junit.jupiter.api.Test;

class TableTest {

    private static final List<Column> COLUMNS = List.of(
            new Column("A", Alignment.LEFT), new Column("B", Alignment.RIGHT));

    @Test
    void acceptsRowsAndFooterOfRightWidth() {
        Table table = new Table("Title", COLUMNS, List.of(List.of(Cell.of("1"), Cell.of("2"))),
                List.of(Cell.of(""), Cell.of("3")));

        assertEquals(1, table.rows().size());
        assertEquals(2, table.footer().size());
    }

    @Test
    void footerMayBeEmpty() {
        Table table = new Table("Title", COLUMNS, List.of(), List.of());

        assertTrue(table.footer().isEmpty());
    }

    @Test
    void rejectsRowWithWrongWidth() {
        assertThrows(IllegalArgumentException.class,
                () -> new Table("Title", COLUMNS, List.of(List.of(Cell.of("1"))), List.of()));
    }

    @Test
    void rejectsFooterWithWrongWidth() {
        assertThrows(IllegalArgumentException.class,
                () -> new Table("Title", COLUMNS, List.of(), List.of(Cell.of("1"))));
    }

    @Test
    void rejectsTableWithoutColumns() {
        assertThrows(IllegalArgumentException.class, () -> new Table("Title", List.of(), List.of(), List.of()));
    }

    @Test
    void rejectsNullParts() {
        assertThrows(NullPointerException.class, () -> new Column(null, Alignment.LEFT));
        assertThrows(NullPointerException.class, () -> new Column("A", null));
        assertThrows(NullPointerException.class, () -> Cell.of(null));
        assertThrows(NullPointerException.class, () -> new Table(null, COLUMNS, List.of(), List.of()));
    }
}
