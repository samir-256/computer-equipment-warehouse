package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.common.Range;
import com.warehouse.entity.ConnectionType;
import com.warehouse.service.Category;
import com.warehouse.service.SearchQuery;
import com.warehouse.service.SortField;
import com.warehouse.service.SortOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SearchQueryFactoryTest {

    private static SearchQuery find(String text) {
        return SearchQueryFactory.forFind(CommandLine.parse(text));
    }

    private static SearchQuery list(String text) {
        return SearchQueryFactory.forList(CommandLine.parse(text));
    }

    @Test
    void findWithOnlyCategoryUsesDefaults() {
        SearchQuery query = find("find laptops");

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
    void findReadsEveryFilter() {
        SearchQuery query = find("find all id=5 name=\"XPS 13\" brand=Dell minprice=100.5 maxprice=900 "
                + "minqty=1 maxqty=7 minscreen=13 maxscreen=15.6 connection=Wired sort=total:desc");

        assertEquals(Category.ALL, query.category());
        assertEquals(5, query.id());
        assertEquals("XPS 13", query.name());
        assertEquals("Dell", query.brand());
        assertEquals(new Range<>(100.5, 900.0), query.price());
        assertEquals(new Range<>(1, 7), query.quantity());
        assertEquals(new Range<>(13.0, 15.6), query.screenSize());
        assertEquals(ConnectionType.WIRED, query.connectionType());
        assertEquals(SortOrder.descending(SortField.TOTAL_COST), query.sortOrder());
    }

    @Test
    void openRangesAreAllowed() {
        SearchQuery query = find("find mice minprice=10 maxqty=3");

        assertEquals(new Range<>(10.0, null), query.price());
        assertEquals(new Range<>(null, 3), query.quantity());
    }

    @ParameterizedTest
    @ValueSource(strings = {"id", "name", "brand", "price", "quantity", "total"})
    void everySortFieldIsKnown(String field) {
        assertEquals(false, find("find all sort=" + field).sortOrder().descending());
        assertEquals(true, find("find all sort=" + field + ":DESC").sortOrder().descending());
        assertEquals(false, find("find all sort=" + field + ":asc").sortOrder().descending());
    }

    @ParameterizedTest
    @ValueSource(strings = {"laptops", "monitors", "mice", "keyboards", "all", "LAPTOPS", "Mice"})
    void everyCategoryIsKnown(String category) {
        assertEquals(category.toUpperCase(), find("find " + category).category().name());
    }

    @ParameterizedTest
    @ValueSource(strings = {"find", "find phones", "find all mice", "find all id=abc", "find all id=1.5",
            "find all id=0", "find all id=-3", "find all minprice=cheap", "find all minprice=NaN",
            "find all maxprice=Infinity", "find all minqty=2.5", "find all minscreen=big",
            "find all minprice=500 maxprice=100", "find all minqty=9 maxqty=1",
            "find all connection=bluetooth", "find all sort=color", "find all sort=price:up",
            "find all sort=price:desc:x", "find all sort=:desc", "find all colour=red"})
    void findRejectsBadInput(String text) {
        assertThrows(IllegalArgumentException.class, () -> find(text));
    }

    @Test
    void listReadsCategoryAndSort() {
        SearchQuery query = list("list monitors sort=name");

        assertEquals(Category.MONITORS, query.category());
        assertEquals(SortOrder.ascending(SortField.NAME), query.sortOrder());
    }

    @ParameterizedTest
    @ValueSource(strings = {"list", "list phones", "list all mice", "list all brand=Dell", "list all sort=x"})
    void listRejectsBadInput(String text) {
        assertThrows(IllegalArgumentException.class, () -> list(text));
    }

    @Test
    void errorMessageNamesTheParameter() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> find("find all minprice=cheap"));

        assertTrue(error.getMessage().contains("minprice"));
        assertTrue(error.getMessage().contains("cheap"));
    }
}
