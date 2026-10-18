package com.warehouse.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LaptopTest {

    private static Laptop.Builder validBuilder() {
        return Laptop.builder()
                .id(1)
                .name("XPS 13")
                .brand("Dell")
                .price(1000.0)
                .quantity(3)
                .cpu("Intel Core i7")
                .ramGb(16)
                .screenSize(13.4);
    }

    @Test
    void shouldBuildLaptopWithAllFields() {
        Laptop laptop = validBuilder().build();

        assertEquals(1, laptop.getId());
        assertEquals("XPS 13", laptop.getName());
        assertEquals("Dell", laptop.getBrand());
        assertEquals(1000.0, laptop.getPrice(), 0.001);
        assertEquals(3, laptop.getQuantity());
        assertEquals("Intel Core i7", laptop.getCpu());
        assertEquals(16, laptop.getRamGb());
        assertEquals(13.4, laptop.getScreenSize(), 0.001);
    }

    @Test
    void shouldTrimTextFields() {
        Laptop laptop = validBuilder().name("  XPS 13 ").brand(" Dell ").build();

        assertEquals("XPS 13", laptop.getName());
        assertEquals("Dell", laptop.getBrand());
    }

    @Test
    void shouldShowOwnFieldsInDetails() {
        String details = validBuilder().build().details();

        assertTrue(details.contains("cpu="));
        assertTrue(details.contains("ramGb="));
        assertTrue(details.contains("screenSize="));
    }

    @Test
    void shouldCalculateTotalCost() {
        assertEquals(3000.0, validBuilder().build().totalCost(), 0.001);
        assertEquals(0.0, validBuilder().quantity(0).build().totalCost(), 0.001);
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        Laptop first = validBuilder().build();
        Laptop second = validBuilder().build();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenOwnFieldDiffers() {
        assertNotEquals(validBuilder().build(), validBuilder().cpu("AMD Ryzen 5").build());
        assertNotEquals(validBuilder().build(), validBuilder().ramGb(8).build());
        assertNotEquals(validBuilder().build(), validBuilder().price(999.0).build());
    }

    @Test
    void shouldShowFieldsAndTotalCostInToString() {
        String text = validBuilder().build().toString();

        assertTrue(text.startsWith("Laptop{"));
        assertTrue(text.contains("name=XPS 13"));
        assertTrue(text.contains("totalCost=3000.00"));
        assertTrue(text.contains("cpu=Intel Core i7"));
    }

    @Test
    void shouldRejectWrongCommonFields() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().id(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().id(-1).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().name(null).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().name("  ").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().brand("").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().price(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().price(-5.5).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().price(Double.NaN).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().quantity(-1).build());
    }

    @Test
    void shouldRejectWrongOwnFields() {
        assertThrows(IllegalArgumentException.class, () -> validBuilder().cpu(" ").build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().ramGb(0).build());
        assertThrows(IllegalArgumentException.class, () -> validBuilder().screenSize(0).build());
    }

    @Test
    void shouldBeAScreenEquipment() {
        HasScreen screen = validBuilder().build();

        assertEquals(13.4, screen.getScreenSize(), 0.001);
    }
}
