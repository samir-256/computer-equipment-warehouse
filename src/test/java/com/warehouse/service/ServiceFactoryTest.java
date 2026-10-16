package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ServiceFactoryTest {

    @Test
    void shouldKeepFirstServiceAfterSecondInit() {
        EquipmentService first = new EquipmentServiceImpl();
        EquipmentService second = new EquipmentServiceImpl();

        ServiceFactory.init(first);
        ServiceFactory.init(second);

        assertSame(first, ServiceFactory.getService());
    }

    @Test
    void shouldRejectNullService() {
        assertThrows(IllegalArgumentException.class, () -> ServiceFactory.init(null));
    }
}
