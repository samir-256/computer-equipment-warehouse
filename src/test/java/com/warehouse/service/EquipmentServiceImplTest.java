package com.warehouse.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.common.Range;
import com.warehouse.dao.DaoFactory;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EquipmentServiceImplTest {

    private static final List<Laptop> LAPTOPS = List.of(
            Samples.laptop(1, "Acme", 1000.0, 2, 15.6),
            Samples.laptop(2, "Globex", 800.0, 5, 13.3),
            Samples.laptop(3, "Acme", 1500.0, 0, 16.0));
    private static final List<Monitor> MONITORS = List.of(
            Samples.monitor(101, 27.0),
            Samples.monitor(102, 24.0));
    private static final List<Mouse> MICE = List.of(
            Samples.mouse(201, "Acme", 20.0, 10, ConnectionType.WIRED),
            Samples.mouse(202, "Globex", 45.0, 4, ConnectionType.WIRELESS));
    private static final List<Keyboard> KEYBOARDS = List.of(
            Samples.keyboard(301, ConnectionType.WIRED),
            Samples.keyboard(302, ConnectionType.WIRELESS));

    private final EquipmentService service = new EquipmentServiceImpl();

    @BeforeAll
    static void init() {
        DaoFactory.init(Map.of(
                Laptop.class, new InMemoryDao<>(LAPTOPS),
                Monitor.class, new InMemoryDao<>(MONITORS),
                Mouse.class, new InMemoryDao<>(MICE),
                Keyboard.class, new InMemoryDao<>(KEYBOARDS)));
    }

    private static List<Integer> ids(List<Equipment> items) {
        return items.stream().map(Equipment::getId).toList();
    }

    private static SearchQuery.Builder query(Category category) {
        return SearchQuery.builder().category(category);
    }

    @Test
    void shouldListOneCategorySortedById() throws ServiceException {
        assertEquals(List.of(1, 2, 3), ids(service.listAll(Category.LAPTOPS, SortOrder.ascending(SortField.ID))));
        assertEquals(List.of(201, 202), ids(service.listAll(Category.MICE, SortOrder.ascending(SortField.ID))));
    }

    @Test
    void shouldListAllCategoriesTogether() throws ServiceException {
        List<Equipment> all = service.listAll(Category.ALL, SortOrder.ascending(SortField.ID));

        assertEquals(List.of(1, 2, 3, 101, 102, 201, 202, 301, 302), ids(all));
    }

    @Test
    void shouldListSortedByPriceDescending() throws ServiceException {
        List<Equipment> sorted = service.listAll(Category.LAPTOPS, SortOrder.descending(SortField.PRICE));

        assertEquals(List.of(3, 1, 2), ids(sorted));
    }

    @Test
    void shouldListSortedByTotalCost() throws ServiceException {
        List<Equipment> descending = service.listAll(Category.LAPTOPS, SortOrder.descending(SortField.TOTAL_COST));
        List<Equipment> ascending = service.listAll(Category.MICE, SortOrder.ascending(SortField.TOTAL_COST));

        assertEquals(List.of(2, 1, 3), ids(descending));
        assertEquals(List.of(202, 201), ids(ascending));
    }

    @Test
    void shouldFilterByBrand() throws ServiceException {
        assertEquals(List.of(1, 3), ids(service.search(query(Category.LAPTOPS).brand("acme").build())));
    }

    @Test
    void shouldFilterByIdAndName() throws ServiceException {
        assertEquals(List.of(2), ids(service.search(query(Category.LAPTOPS).id(2).build())));
        assertEquals(List.of(101, 102), ids(service.search(query(Category.MONITORS).name("MONITOR").build())));
    }

    @Test
    void shouldFilterByPriceAndQuantityRanges() throws ServiceException {
        SearchQuery byPrice = query(Category.LAPTOPS).price(new Range<>(900.0, 1600.0)).build();
        SearchQuery byQuantity = query(Category.LAPTOPS).quantity(new Range<>(1, null)).build();

        assertEquals(List.of(1, 3), ids(service.search(byPrice)));
        assertEquals(List.of(1, 2), ids(service.search(byQuantity)));
    }

    @Test
    void shouldCombineFiltersWithAnd() throws ServiceException {
        SearchQuery query = query(Category.LAPTOPS)
                .brand("Acme")
                .quantity(new Range<>(1, null))
                .build();

        assertEquals(List.of(1), ids(service.search(query)));
    }

    @Test
    void shouldFilterByScreenSizeForLaptopsAndMonitors() throws ServiceException {
        SearchQuery laptops = query(Category.LAPTOPS).screenSize(new Range<>(15.0, null)).build();
        SearchQuery monitors = query(Category.MONITORS).screenSize(new Range<>(null, 25.0)).build();

        assertEquals(List.of(1, 3), ids(service.search(laptops)));
        assertEquals(List.of(102), ids(service.search(monitors)));
    }

    @Test
    void shouldFilterByConnectionForMiceAndKeyboards() throws ServiceException {
        assertEquals(List.of(202), ids(service.search(
                query(Category.MICE).connectionType(ConnectionType.WIRELESS).build())));
        assertEquals(List.of(301), ids(service.search(
                query(Category.KEYBOARDS).connectionType(ConnectionType.WIRED).build())));
    }

    @Test
    void shouldApplyCommonFilterToEveryCategoryForAll() throws ServiceException {
        List<Equipment> found = service.search(query(Category.ALL).brand("Acme").build());

        assertEquals(List.of(1, 3, 101, 102, 201, 301, 302), ids(found));
    }

    @Test
    void shouldSearchOnlyTypesWithScreenWhenScreenFilterUsedForAll() throws ServiceException {
        SearchQuery query = query(Category.ALL).screenSize(new Range<>(20.0, null)).build();

        assertEquals(List.of(101, 102), ids(service.search(query)));
    }

    @Test
    void shouldSearchOnlyConnectableTypesWhenConnectionFilterUsedForAll() throws ServiceException {
        SearchQuery query = query(Category.ALL).connectionType(ConnectionType.WIRELESS).build();

        assertEquals(List.of(202, 302), ids(service.search(query)));
    }

    @Test
    void shouldSortSearchResults() throws ServiceException {
        SearchQuery query = query(Category.LAPTOPS)
                .brand("Acme")
                .sortOrder(SortOrder.descending(SortField.PRICE))
                .build();

        assertEquals(List.of(3, 1), ids(service.search(query)));
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() throws ServiceException {
        assertTrue(service.search(query(Category.ALL).brand("Nobody").build()).isEmpty());
    }

    @Test
    void shouldRejectScreenFilterForCategoriesWithoutScreen() {
        SearchQuery mice = query(Category.MICE).screenSize(new Range<>(10.0, null)).build();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.search(mice));

        assertTrue(error.getMessage().contains("mice"));
    }

    @Test
    void shouldRejectConnectionFilterForCategoriesWithoutConnection() {
        SearchQuery laptops = query(Category.LAPTOPS).connectionType(ConnectionType.WIRED).build();

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> service.search(laptops));

        assertTrue(error.getMessage().contains("laptops"));
    }

    @Test
    void shouldRejectNullQuery() {
        assertThrows(IllegalArgumentException.class, () -> service.search(null));
    }
}
