package com.warehouse.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MouseTest {

    private static Mouse.Builder validBuilder() {
        return Mouse.builder()
                .id(201)
                .name("MX Master 3S")
                .brand("Logitech")
                .price(100.0)
                .quantity(4)
                .connectionType(ConnectionType.WIRELESS)
                .dpi(8000);
    }

    @Test
    void shouldBuildMouseWithAllFields() {
        Mouse mouse = validBuilder().build();

        assertEquals(201, mouse.getId());
        assertEquals("MX Master 3S", mouse.getName());
        assertEquals("Logitech", mouse.getBrand());
        assertEquals(ConnectionType.WIRELESS, mouse.getConnectionType());
        assertEquals(8000, mouse.getDpi());
    }

    @Test
    void shouldCalculateTotalCost() {
        assertEquals(400.0, validBuilder().build().totalCost(), 0.001);
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        assertEquals(validBuilder().build(), validBuilder().build());
        assertEquals(validBuilder().build().hashCode(), validBuilder().build().hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOwnFieldDiffers() {
        assertNotEquals(validBuilder().build(), validBuilder().dpi(1600).build());
        assertNotEquals(validBuilder().build(), validBuilder().connectionType(ConnectionType.WIRED).build());
    }

    @Test
    void shouldShowFieldsInToString() {
        String text = validBuilder().build().toString();

        assertTrue(text.startsWith("Mouse{"));
        assertTrue(text.contains("connectionType=WIRELESS"));
        assertTrue(text.contains("dpi=8000"));
    }

    @Test
    void shouldRejectWrongOwnFields() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().connectionType(null).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().dpi(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().dpi(-800).build());
    }

    @Test
    void shouldBeConnectable() {
        Connectable connectable = validBuilder().connectionType(ConnectionType.WIRED).build();

        assertEquals(ConnectionType.WIRED, connectable.getConnectionType());
    }
}
