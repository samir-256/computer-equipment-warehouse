package com.warehouse.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class StyleTest {

    @Test
    void disabledStyleReturnsTextUnchanged() {
        Style style = new Style(false);

        assertEquals("text", style.header("text"));
        assertEquals("text", style.title("text"));
        assertEquals("text", style.strong("text"));
        assertEquals("text", style.border("text"));
        assertEquals("text", style.danger("text"));
        assertEquals("text", style.success("text"));
        assertEquals("text", style.prompt("text"));
    }

    @Test
    void enabledStyleWrapsTextAndResets() {
        Style style = new Style(true);

        assertEquals("\u001B[31mtext\u001B[0m", style.danger("text"));
        assertEquals("\u001B[32mtext\u001B[0m", style.success("text"));
        assertTrue(style.header("text").startsWith("\u001B["));
        assertTrue(style.border("text").endsWith("\u001B[0m"));
    }

    @Test
    void differentRolesUseDifferentColors() {
        Style style = new Style(true);

        assertNotEquals(style.danger("x"), style.success("x"));
        assertNotEquals(style.header("x"), style.title("x"));
        assertNotEquals(style.border("x"), style.danger("x"));
    }
}
