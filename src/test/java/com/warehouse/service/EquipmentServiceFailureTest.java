package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.dao.DaoException;
import com.warehouse.dao.DaoFactory;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EquipmentServiceFailureTest {

    private final EquipmentService service = new EquipmentServiceImpl();

    @BeforeAll
    static void init() {
        DaoFactory.init(Map.of(
                Laptop.class, new FailingDao<Laptop>(),
                Monitor.class, new FailingDao<Monitor>(),
                Mouse.class, new FailingDao<Mouse>(),
                Keyboard.class, new FailingDao<Keyboard>()));
    }

    @Test
    void shouldWrapDaoFailureForOneCategory() {
        ServiceException error = assertThrows(ServiceException.class,
                () -> service.listAll(Category.LAPTOPS, SortOrder.ascending(SortField.ID)));

        assertTrue(error.getMessage().contains(FailingDao.MESSAGE));
        assertTrue(error.getCause() instanceof DaoException);
    }

    @Test
    void shouldWrapDaoFailureForAllCategories() {
        ServiceException error = assertThrows(ServiceException.class,
                () -> service.listAll(Category.ALL, SortOrder.ascending(SortField.ID)));

        assertTrue(error.getCause() instanceof DaoException);
    }

    @Test
    void shouldReportBadRequestBeforeTouchingDao() {
        SearchQuery query = SearchQuery.builder()
                .category(Category.MICE)
                .screenSize(new com.warehouse.common.Range<>(1.0, null))
                .build();

        assertThrows(IllegalArgumentException.class, () -> service.search(query));
    }
}
