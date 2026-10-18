package com.warehouse.view;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ViewFactoryTest {

    private static View view() {
        return new View() {
            @Override
            public void start() {
            }

            @Override
            public void crash() {
            }
        };
    }

    @Test
    void firstInitWinsAndNullIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> ViewFactory.init(null));

        View first = view();
        ViewFactory.init(first);
        ViewFactory.init(view());

        assertSame(first, ViewFactory.getView());
    }
}
