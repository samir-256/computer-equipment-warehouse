package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class ControllerFactoryTest {

    @Test
    void firstInitWinsAndNullIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> ControllerFactory.init(null));

        Controller first = new ControllerImpl(new CommandProvider(new StubService(List.of())));
        Controller second = new ControllerImpl(new CommandProvider(new StubService(List.of())));
        ControllerFactory.init(first);
        ControllerFactory.init(second);

        assertSame(first, ControllerFactory.getController());
    }
}
