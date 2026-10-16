package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Laptop;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class SortOrderTest {

    private static final Laptop CHEAP = Samples.laptop(3, "Globex", 500.0, 4, 14.0);
    private static final Laptop MIDDLE_FIRST = Samples.laptop(1, "Acme", 800.0, 1, 15.6);
    private static final Laptop MIDDLE_SECOND = Samples.laptop(2, "Acme", 800.0, 2, 13.3);

    private static List<Integer> sortedIds(SortOrder order) {
        List<Equipment> items = new ArrayList<>(List.of(CHEAP, MIDDLE_SECOND, MIDDLE_FIRST));
        items.sort(order.comparator());
        return items.stream().map(Equipment::getId).toList();
    }

    @Test
    void shouldSortByIdAscendingAndDescending() {
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.ascending(SortField.ID)));
        assertEquals(List.of(3, 2, 1), sortedIds(SortOrder.descending(SortField.ID)));
    }

    @Test
    void shouldSortByPriceAndKeepIdOrderForTies() {
        assertEquals(List.of(3, 1, 2), sortedIds(SortOrder.ascending(SortField.PRICE)));
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.descending(SortField.PRICE)));
    }

    @Test
    void shouldSortByQuantity() {
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.ascending(SortField.QUANTITY)));
        assertEquals(List.of(3, 2, 1), sortedIds(SortOrder.descending(SortField.QUANTITY)));
    }

    @Test
    void shouldSortByBrandIgnoringCase() {
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.ascending(SortField.BRAND)));
        assertEquals(List.of(3, 1, 2), sortedIds(SortOrder.descending(SortField.BRAND)));
    }

    @Test
    void shouldSortByName() {
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.ascending(SortField.NAME)));
        assertEquals(List.of(3, 2, 1), sortedIds(SortOrder.descending(SortField.NAME)));
    }

    @Test
    void shouldSortByTotalCost() {
        assertEquals(List.of(1, 2, 3), sortedIds(SortOrder.ascending(SortField.TOTAL_COST)));
        assertEquals(List.of(3, 2, 1), sortedIds(SortOrder.descending(SortField.TOTAL_COST)));
    }

    @Test
    void shouldCreateOrdersWithDirection() {
        assertFalse(SortOrder.ascending(SortField.ID).descending());
        assertTrue(SortOrder.descending(SortField.ID).descending());
    }

    @Test
    void shouldRejectNullField() {
        assertThrows(NullPointerException.class, () -> new SortOrder(null, false));
    }
}
