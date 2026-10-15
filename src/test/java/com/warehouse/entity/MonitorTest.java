package com.warehouse.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MonitorTest {

    private static Monitor.Builder validBuilder() {
        return Monitor.builder()
                .id(101)
                .name("S2721DGF")
                .brand("Dell")
                .price(400.0)
                .quantity(2)
                .screenSize(27.0)
                .resolution("2560x1440")
                .refreshRate(165);
    }

    @Test
    void shouldBuildMonitorWithAllFields() {
        Monitor monitor = validBuilder().build();

        assertEquals(101, monitor.getId());
        assertEquals("S2721DGF", monitor.getName());
        assertEquals("Dell", monitor.getBrand());
        assertEquals(27.0, monitor.getScreenSize(), 0.001);
        assertEquals("2560x1440", monitor.getResolution());
        assertEquals(165, monitor.getRefreshRate());
    }

    @Test
    void shouldCalculateTotalCost() {
        assertEquals(800.0, validBuilder().build().totalCost(), 0.001);
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        assertEquals(validBuilder().build(), validBuilder().build());
        assertEquals(validBuilder().build().hashCode(), validBuilder().build().hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOwnFieldDiffers() {
        assertNotEquals(validBuilder().build(), validBuilder().refreshRate(60).build());
        assertNotEquals(validBuilder().build(), validBuilder().resolution("1920x1080").build());
        assertNotEquals(validBuilder().build(), validBuilder().screenSize(24.0).build());
    }

    @Test
    void shouldShowFieldsInToString() {
        String text = validBuilder().build().toString();

        assertTrue(text.startsWith("Monitor{"));
        assertTrue(text.contains("resolution=2560x1440"));
        assertTrue(text.contains("totalCost=800.00"));
    }

    @Test
    void shouldRejectWrongResolution() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().resolution(null).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().resolution("").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().resolution("1920*1080").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().resolution("FullHD").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().resolution("1920x").build());
    }

    @Test
    void shouldRejectWrongOwnNumbers() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().screenSize(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().refreshRate(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().refreshRate(-60).build());
    }

    @Test
    void shouldBeAScreenEquipment() {
        HasScreen screen = validBuilder().build();

        assertEquals(27.0, screen.getScreenSize(), 0.001);
    }
}
