package com.warehouse.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RangeTest {

    @Test
    void shouldIncludeBothBounds() {
        Range<Integer> range = new Range<>(10, 20);

        assertTrue(range.isIn(10));
        assertTrue(range.isIn(15));
        assertTrue(range.isIn(20));
    }

    @Test
    void shouldExcludeValuesOutsideBounds() {
        Range<Integer> range = new Range<>(10, 20);

        assertFalse(range.isIn(9));
        assertFalse(range.isIn(21));
    }

    @Test
    void shouldAllowOpenEnd() {
        Range<Double> atLeast = new Range<>(5.0, null);

        assertTrue(atLeast.isIn(5.0));
        assertTrue(atLeast.isIn(1000.0));
        assertFalse(atLeast.isIn(4.99));
    }

    @Test
    void shouldAllowOpenStart() {
        Range<Double> atMost = new Range<>(null, 5.0);

        assertTrue(atMost.isIn(5.0));
        assertTrue(atMost.isIn(-1.0));
        assertFalse(atMost.isIn(5.01));
    }

    @Test
    void shouldAllowSingleValueRange() {
        Range<Integer> range = new Range<>(7, 7);

        assertTrue(range.isIn(7));
        assertFalse(range.isIn(8));
    }

    @Test
    void shouldRejectRangeWithoutBounds() {
        assertThrows(IllegalArgumentException.class, () -> new Range<Integer>(null, null));
    }

    @Test
    void shouldRejectStartGreaterThanEnd() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new Range<>(20, 10));

        assertTrue(error.getMessage().contains("20"));
        assertTrue(error.getMessage().contains("10"));
    }

    @Test
    void shouldRejectNullValue() {
        Range<Integer> range = new Range<>(1, 2);

        assertThrows(NullPointerException.class, () -> range.isIn(null));
    }

    @Test
    void shouldExposeBounds() {
        Range<Integer> range = new Range<>(1, 2);

        assertEquals(1, range.from());
        assertEquals(2, range.to());
    }
}
