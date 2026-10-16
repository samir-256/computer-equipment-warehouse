package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.warehouse.common.Range;
import com.warehouse.entity.ConnectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchQueryTest {

    @Test
    void shouldUseDefaultsForOptionalFields() {
        SearchQuery query = SearchQuery.builder().category(Category.LAPTOPS).build();

        assertEquals(Category.LAPTOPS, query.category());
        assertNull(query.id());
        assertNull(query.name());
        assertNull(query.brand());
        assertNull(query.price());
        assertNull(query.quantity());
        assertNull(query.screenSize());
        assertNull(query.connectionType());
        assertEquals(SortOrder.ascending(SortField.ID), query.sortOrder());
    }

    @Test
    void shouldKeepAllGivenValues() {
        Range<Double> price = new Range<>(10.0, 20.0);
        SearchQuery query = SearchQuery.builder()
                .category(Category.MICE)
                .id(5)
                .name("click")
                .brand("Acme")
                .price(price)
                .quantity(new Range<>(1, null))
                .connectionType(ConnectionType.WIRED)
                .sortOrder(SortOrder.descending(SortField.PRICE))
                .build();

        assertEquals(5, query.id());
        assertEquals("click", query.name());
        assertEquals("Acme", query.brand());
        assertEquals(price, query.price());
        assertEquals(new Range<>(1, null), query.quantity());
        assertEquals(ConnectionType.WIRED, query.connectionType());
        assertEquals(SortOrder.descending(SortField.PRICE), query.sortOrder());
    }

    @Test
    void shouldTrimNameAndBrand() {
        SearchQuery query = SearchQuery.builder()
                .category(Category.ALL)
                .name("  click ")
                .brand(" Acme  ")
                .build();

        assertEquals("click", query.name());
        assertEquals("Acme", query.brand());
    }

    @Test
    void shouldRequireCategory() {
        assertThrows(IllegalArgumentException.class, () -> SearchQuery.builder().build());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -50})
    void shouldRejectNonPositiveId(int id) {
        assertThrows(IllegalArgumentException.class,
                () -> SearchQuery.builder().category(Category.ALL).id(id).build());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    void shouldRejectBlankNameAndBrand(String text) {
        assertThrows(IllegalArgumentException.class,
                () -> SearchQuery.builder().category(Category.ALL).name(text).build());
        assertThrows(IllegalArgumentException.class,
                () -> SearchQuery.builder().category(Category.ALL).brand(text).build());
    }
}
