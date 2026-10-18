package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import java.util.List;
import org.junit.jupiter.api.Test;

class ResponseFormatterTest {

    @Test
    void emptyListGivesNothingFoundMessage() {
        assertEquals("No products found.", ResponseFormatter.format(List.of()));
    }

    @Test
    void tableShowsHeaderRowsAndCount() {
        List<Equipment> items = List.of(
                Samples.laptop(1, "Acme", 1000.0, 2, 15.6),
                Samples.mouse(201, "Globex", 45.5, 4, ConnectionType.WIRELESS));

        String[] lines = ResponseFormatter.format(items).split("\n");

        assertEquals(4, lines.length);
        assertTrue(lines[0].trim().startsWith("ID"));
        assertTrue(lines[0].contains("Category"));
        assertTrue(lines[0].contains("Price"));
        assertTrue(lines[0].contains("Qty"));
        assertTrue(lines[1].contains("Laptop"));
        assertTrue(lines[1].contains("Laptop 1"));
        assertTrue(lines[1].contains("Acme"));
        assertTrue(lines[1].contains("1000.00"));
        assertTrue(lines[1].contains("2000.00"));
        assertTrue(lines[1].contains("screenSize=15.6"));
        assertTrue(lines[2].contains("Mouse"));
        assertTrue(lines[2].contains("45.50"));
        assertTrue(lines[2].contains("182.00"));
        assertTrue(lines[2].contains("WIRELESS"));
        assertEquals("Found 2 product(s).", lines[3]);
    }

    @Test
    void columnsAreAligned() {
        List<Equipment> items = List.of(
                Samples.laptop(1, "Acme", 1000.0, 2, 15.6),
                Samples.laptop(12, "Globex", 99.0, 10, 13.3));

        String[] lines = ResponseFormatter.format(items).split("\n");

        int brandColumn = lines[0].indexOf("Brand");
        assertEquals(brandColumn, lines[1].indexOf("Acme"));
        assertEquals(brandColumn, lines[2].indexOf("Globex"));
    }
}
