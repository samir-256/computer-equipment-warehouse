package com.warehouse.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.common.Range;
import com.warehouse.dao.criteria.RangeParameter;
import com.warehouse.dao.criteria.AllMatchCriteria;
import com.warehouse.dao.criteria.BrandParameter;
import com.warehouse.dao.criteria.ConnectionTypeParameter;
import com.warehouse.dao.criteria.IdParameter;
import com.warehouse.dao.criteria.NameParameter;
import com.warehouse.dao.criteria.Parameter;
import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.dao.source.LaptopCsvSource;
import com.warehouse.dao.source.MouseCsvSource;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Mouse;
import java.util.List;
import org.junit.jupiter.api.Test;

class EquipmentDaoImplTest {

    private static EquipmentDao<Laptop> laptopDao(String csvName) {
        return new EquipmentDaoImpl<>(new LaptopCsvSource(csvName));
    }

    private static SearchCriteria<Laptop> criteria() {
        return new AllMatchCriteria<>();
    }

    private static List<Integer> ids(List<? extends Equipment> items) {
        return items.stream().map(Equipment::getId).toList();
    }

    @Test
    void shouldReturnAllRowsInFileOrder() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv").find(criteria().add(Parameter.any()));

        assertEquals(List.of(1, 2, 3, 4, 5), ids(found));
    }

    @Test
    void shouldFilterByBrand() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv").find(criteria().add(new BrandParameter("acme")));

        assertEquals(List.of(1, 2), ids(found));
    }

    @Test
    void shouldFilterByIdAndName() throws DaoException {
        EquipmentDao<Laptop> dao = laptopDao("laptops-test.csv");

        assertEquals(List.of(3), ids(dao.find(criteria().add(new IdParameter(3)))));
        assertEquals(List.of(3, 4), ids(dao.find(criteria().add(new NameParameter("BETA")))));
    }

    @Test
    void shouldFilterByPriceRange() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv")
                .find(criteria().add(RangeParameter.price(new Range<>(900.0, 1200.0))));

        assertEquals(List.of(1, 2, 5), ids(found));
    }

    @Test
    void shouldFilterByQuantityRange() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv")
                .find(criteria().add(RangeParameter.quantity(new Range<>(1, null))));

        assertEquals(List.of(1, 3, 4, 5), ids(found));
    }

    @Test
    void shouldFilterByScreenSize() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv")
                .find(criteria().add(RangeParameter.screenSize(new Range<>(15.0, null))));

        assertEquals(List.of(2, 4, 5), ids(found));
    }

    @Test
    void shouldCombineParametersWithAnd() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv").find(criteria()
                .add(new BrandParameter("Acme"))
                .add(RangeParameter.quantity(new Range<>(1, null))));

        assertEquals(List.of(1), ids(found));
    }

    @Test
    void shouldReturnEmptyListWhenNothingMatches() throws DaoException {
        List<Laptop> found = laptopDao("laptops-test.csv").find(criteria().add(new BrandParameter("Nobody")));

        assertTrue(found.isEmpty());
    }

    @Test
    void shouldReturnEmptyListForFileWithHeaderOnly() throws DaoException {
        assertTrue(laptopDao("laptops-empty.csv").find(criteria().add(Parameter.any())).isEmpty());
    }

    @Test
    void shouldSkipBlankLines() throws DaoException {
        List<Laptop> found = laptopDao("laptops-blank-lines.csv").find(criteria().add(Parameter.any()));

        assertEquals(List.of(1, 2), ids(found));
    }

    @Test
    void shouldWorkWithAnotherEntityType() throws DaoException {
        EquipmentDao<Mouse> dao = new EquipmentDaoImpl<>(new MouseCsvSource("mice-test.csv"));

        List<Mouse> found = dao.find(new AllMatchCriteria<Mouse>()
                .add(new ConnectionTypeParameter<Mouse>(ConnectionType.WIRELESS)));

        assertEquals(List.of(202, 203), ids(found));
    }

    @Test
    void shouldReportFileAndLineOfBrokenRow() {
        DaoException error = assertThrows(DaoException.class,
                () -> laptopDao("laptops-broken.csv").find(criteria().add(Parameter.any())));

        assertTrue(error.getMessage().contains("laptops-broken.csv"));
        assertTrue(error.getMessage().contains("line 3"));
        assertTrue(error.getMessage().contains("abc"));
    }

    @Test
    void shouldReportWrongColumnCount() {
        DaoException error = assertThrows(DaoException.class,
                () -> laptopDao("laptops-wrong-columns.csv").find(criteria().add(Parameter.any())));

        assertTrue(error.getMessage().contains("line 2"));
        assertTrue(error.getMessage().contains("Expected 8 fields"));
    }

    @Test
    void shouldFailWhenFileDoesNotExist() {
        DaoException error = assertThrows(DaoException.class,
                () -> laptopDao("missing.csv").find(criteria().add(Parameter.any())));

        assertTrue(error.getMessage().contains("missing.csv"));
    }

    @Test
    void shouldRejectNullSourceAndCriteria() {
        assertThrows(NullPointerException.class, () -> new EquipmentDaoImpl<Laptop>(null));
        assertThrows(NullPointerException.class, () -> laptopDao("laptops-test.csv").find(null));
    }
}
