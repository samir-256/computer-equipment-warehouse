package com.warehouse.dao.source;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.entity.ConnectionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CsvLineTest {

    @Test
    void shouldSplitFieldsByDelimiter() {
        CsvLine line = CsvLine.of("1;XPS 13;Dell");

        assertEquals(3, line.size());
        assertEquals("XPS 13", line.text(1));
    }

    @Test
    void shouldTrimFields() {
        assertEquals("Dell", CsvLine.of("1; Dell ;3").text(1));
    }

    @Test
    void shouldKeepEmptyTrailingField() {
        CsvLine line = CsvLine.of("a;b;");

        assertEquals(3, line.size());
        assertEquals("", line.text(2));
    }

    @Test
    void shouldReadNumbers() {
        CsvLine line = CsvLine.of("42;2.5");

        assertEquals(42, line.integer(0));
        assertEquals(2.5, line.decimal(1), 0.001);
    }

    @Test
    void shouldReadEnumIgnoringCase() {
        assertEquals(ConnectionType.WIRELESS, CsvLine.of("wireless").enumValue(0, ConnectionType.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "", "1.5", "99999999999"})
    void shouldRejectWrongInteger(String value) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> CsvLine.of("x;" + value).integer(1));

        assertTrue(error.getMessage().contains("Column 2"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "", "1,5"})
    void shouldRejectWrongDecimal(String value) {
        assertThrows(IllegalArgumentException.class, () -> CsvLine.of("x;" + value).decimal(1));
    }

    @Test
    void shouldListAllowedEnumValues() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> CsvLine.of("USB").enumValue(0, ConnectionType.class));

        assertTrue(error.getMessage().contains("WIRED"));
        assertTrue(error.getMessage().contains("WIRELESS"));
        assertTrue(error.getMessage().contains("USB"));
    }

    @Test
    void shouldRejectMissingColumn() {
        CsvLine line = CsvLine.of("a;b");

        assertThrows(IllegalArgumentException.class, () -> line.text(2));
        assertThrows(IllegalArgumentException.class, () -> line.text(-1));
    }

    @Test
    void shouldRejectNullLine() {
        assertThrows(IllegalArgumentException.class, () -> CsvLine.of(null));
    }
}
