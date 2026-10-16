package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void shouldExpandAllToEveryConcreteType() {
        assertEquals(
                List.of(Category.LAPTOPS, Category.MONITORS, Category.MICE, Category.KEYBOARDS),
                Category.ALL.concreteTypes());
    }

    @Test
    void shouldKeepSingleCategoryAsItIs() {
        assertEquals(List.of(Category.MICE), Category.MICE.concreteTypes());
    }

    @Test
    void shouldKnowWhichTypesHaveScreen() {
        assertTrue(Category.LAPTOPS.supportsScreenFilter());
        assertTrue(Category.MONITORS.supportsScreenFilter());
        assertFalse(Category.MICE.supportsScreenFilter());
        assertFalse(Category.KEYBOARDS.supportsScreenFilter());
    }

    @Test
    void shouldKnowWhichTypesHaveConnection() {
        assertTrue(Category.MICE.supportsConnectionFilter());
        assertTrue(Category.KEYBOARDS.supportsConnectionFilter());
        assertFalse(Category.LAPTOPS.supportsConnectionFilter());
        assertFalse(Category.MONITORS.supportsConnectionFilter());
    }

    @Test
    void shouldAllowEveryFilterForAll() {
        assertTrue(Category.ALL.supportsScreenFilter());
        assertTrue(Category.ALL.supportsConnectionFilter());
    }
}
