package com.warehouse.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HolderTest {

    @Test
    void shouldReturnInitializedValue() {
        Holder<String> holder = new Holder<>("Text");

        holder.init("first");

        assertEquals("first", holder.get());
    }

    @Test
    void shouldKeepFirstValueAfterSecondInit() {
        Holder<Object> holder = new Holder<>("Object");
        Object first = new Object();

        holder.init(first);
        holder.init(new Object());

        assertSame(first, holder.get());
    }

    @Test
    void shouldFailBeforeInitWithNameInMessage() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> new Holder<String>("Text holder").get());

        assertTrue(error.getMessage().contains("Text holder"));
    }

    @Test
    void shouldRejectNullValue() {
        Holder<String> holder = new Holder<>("Text");

        assertThrows(IllegalArgumentException.class, () -> holder.init(null));
    }

    @Test
    void shouldStayEmptyAfterRejectedValue() {
        Holder<String> holder = new Holder<>("Text");

        assertThrows(IllegalArgumentException.class, () -> holder.init(null));

        assertThrows(IllegalStateException.class, holder::get);
    }
}
