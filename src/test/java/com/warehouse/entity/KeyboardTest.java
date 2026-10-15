package com.warehouse.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KeyboardTest {

    private static Keyboard.Builder validBuilder() {
        return Keyboard.builder()
                .id(301)
                .name("K2")
                .brand("Keychron")
                .price(90.0)
                .quantity(5)
                .layout(KeyboardLayout.ANSI)
                .connectionType(ConnectionType.WIRELESS);
    }

    @Test
    void shouldBuildKeyboardWithAllFields() {
        Keyboard keyboard = validBuilder().build();

        assertEquals(301, keyboard.getId());
        assertEquals("K2", keyboard.getName());
        assertEquals("Keychron", keyboard.getBrand());
        assertEquals(KeyboardLayout.ANSI, keyboard.getLayout());
        assertEquals(ConnectionType.WIRELESS, keyboard.getConnectionType());
    }

    @Test
    void shouldCalculateTotalCost() {
        assertEquals(450.0, validBuilder().build().totalCost(), 0.001);
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        assertEquals(validBuilder().build(), validBuilder().build());
        assertEquals(validBuilder().build().hashCode(), validBuilder().build().hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOwnFieldDiffers() {
        assertNotEquals(validBuilder().build(), validBuilder().layout(KeyboardLayout.ISO).build());
        assertNotEquals(validBuilder().build(), validBuilder().connectionType(ConnectionType.WIRED).build());
    }

    @Test
    void shouldNotBeEqualToOtherEquipmentTypes() {
        Mouse mouse = Mouse.builder()
                .id(301)
                .name("K2")
                .brand("Keychron")
                .price(90.0)
                .quantity(5)
                .connectionType(ConnectionType.WIRELESS)
                .dpi(1000)
                .build();

        assertNotEquals(validBuilder().build(), mouse);
    }

    @Test
    void shouldShowFieldsInToString() {
        String text = validBuilder().build().toString();

        assertTrue(text.startsWith("Keyboard{"));
        assertTrue(text.contains("layout=ANSI"));
        assertTrue(text.contains("totalCost=450.00"));
    }

    @Test
    void shouldRejectWrongOwnFields() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().layout(null).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().connectionType(null).build());
    }

    @Test
    void shouldRejectWrongCommonFields() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().id(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().price(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().quantity(-1).build());
    }
}
