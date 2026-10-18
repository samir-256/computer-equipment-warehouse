package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.controller.Table.Alignment;
import com.warehouse.controller.Table.Tone;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import com.warehouse.service.Category;
import java.util.List;
import org.junit.jupiter.api.Test;

class ProductTableBuilderTest {

    private static final List<Equipment> ITEMS = List.of(
            Samples.laptop(1, "Acme", 1000.0, 2, 15.6),
            Samples.mouse(201, "Globex", 45.5, 0, ConnectionType.WIRELESS));

    private static List<String> texts(List<Table.Cell> cells) {
        return cells.stream().map(Table.Cell::text).toList();
    }

    @Test
    void columnsHaveHeadersAndAlignment() {
        Table table = ProductTableBuilder.build(Category.ALL, ITEMS);

        assertEquals(List.of("ID", "Category", "Name", "Brand", "Price", "Qty", "Total", "Details"),
                table.columns().stream().map(Table.Column::header).toList());
        assertEquals(Alignment.RIGHT, table.columns().get(0).alignment());
        assertEquals(Alignment.LEFT, table.columns().get(2).alignment());
        assertEquals(Alignment.RIGHT, table.columns().get(4).alignment());
    }

    @Test
    void rowsShowProductData() {
        Table table = ProductTableBuilder.build(Category.ALL, ITEMS);

        assertEquals(2, table.rows().size());
        assertEquals(List.of("1", "Laptop", "Laptop 1", "Acme", "1000.00", "2", "2000.00"),
                texts(table.rows().get(0)).subList(0, 7));
        assertTrue(texts(table.rows().get(0)).get(7).contains("screenSize=15.6"));
        assertEquals(List.of("201", "Mouse", "Mouse 201", "Globex", "45.50", "0", "0.00"),
                texts(table.rows().get(1)).subList(0, 7));
        assertTrue(texts(table.rows().get(1)).get(7).contains("WIRELESS"));
    }

    @Test
    void zeroQuantityIsMarkedAsWarning() {
        Table table = ProductTableBuilder.build(Category.ALL, ITEMS);

        assertEquals(Tone.NORMAL, table.rows().get(0).get(5).tone());
        assertEquals(Tone.WARNING, table.rows().get(1).get(5).tone());
    }

    @Test
    void footerSumsQuantityAndTotalCost() {
        Table table = ProductTableBuilder.build(Category.ALL, ITEMS);

        List<String> footer = texts(table.footer());
        assertEquals("Total", footer.get(2));
        assertEquals("2", footer.get(5));
        assertEquals("2000.00", footer.get(6));
    }

    @Test
    void titleNamesTheCategory() {
        assertEquals("All products", ProductTableBuilder.title(Category.ALL));
        assertEquals("Laptops", ProductTableBuilder.title(Category.LAPTOPS));
        assertEquals("Mice", ProductTableBuilder.title(Category.MICE));
        assertEquals("Keyboards", ProductTableBuilder.title(Category.KEYBOARDS));
        assertEquals("Monitors", ProductTableBuilder.title(Category.MONITORS));
    }
}
