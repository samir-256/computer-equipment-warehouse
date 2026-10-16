package com.warehouse.dao;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.warehouse.entity.Equipment;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Mouse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class DaoFactoryTest {

    private static final EquipmentDao<Laptop> LAPTOP_DAO = criteria -> List.of();
    private static final EquipmentDao<Mouse> MOUSE_DAO = criteria -> List.of();

    @BeforeAll
    static void init() {
        DaoFactory.init(Map.of(Laptop.class, LAPTOP_DAO));
    }

    @Test
    void shouldReturnRegisteredDao() {
        assertSame(LAPTOP_DAO, DaoFactory.getDao(Laptop.class));
    }

    @Test
    void shouldRejectTypeWithoutDao() {
        assertThrows(IllegalArgumentException.class, () -> DaoFactory.getDao(Mouse.class));
    }

    @Test
    void shouldIgnoreSecondInitialization() {
        Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> other = Map.of(Mouse.class, MOUSE_DAO);

        DaoFactory.init(other);

        assertSame(LAPTOP_DAO, DaoFactory.getDao(Laptop.class));
        assertThrows(IllegalArgumentException.class, () -> DaoFactory.getDao(Mouse.class));
    }

    @Test
    void shouldRejectNullAndEmptyMaps() {
        assertThrows(IllegalArgumentException.class, () -> DaoFactory.init(null));
        assertThrows(IllegalArgumentException.class, () -> DaoFactory.init(Map.of()));
    }
}
