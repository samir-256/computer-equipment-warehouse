package com.warehouse.dao.source;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.KeyboardLayout;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CsvSourceTest {

    private static final LaptopCsvSource LAPTOPS = new LaptopCsvSource("laptops-test.csv");
    private static final MonitorCsvSource MONITORS = new MonitorCsvSource("monitors-test.csv");
    private static final MouseCsvSource MICE = new MouseCsvSource("mice-test.csv");
    private static final KeyboardCsvSource KEYBOARDS = new KeyboardCsvSource("keyboards-test.csv");

    @Test
    void shouldParseLaptop() {
        Laptop laptop = LAPTOPS.parse("1;XPS 13;Dell;1199.99;5;Intel Core i7;16;13.4");

        assertEquals(1, laptop.getId());
        assertEquals("XPS 13", laptop.getName());
        assertEquals("Dell", laptop.getBrand());
        assertEquals(1199.99, laptop.getPrice(), 0.001);
        assertEquals(5, laptop.getQuantity());
        assertEquals("Intel Core i7", laptop.getCpu());
        assertEquals(16, laptop.getRamGb());
        assertEquals(13.4, laptop.getScreenSize(), 0.001);
    }

    @Test
    void shouldParseMonitor() {
        Monitor monitor = MONITORS.parse("101;S2721DGF;Dell;429.00;6;27.0;2560x1440;165");

        assertEquals(101, monitor.getId());
        assertEquals(27.0, monitor.getScreenSize(), 0.001);
        assertEquals("2560x1440", monitor.getResolution());
        assertEquals(165, monitor.getRefreshRate());
    }

    @Test
    void shouldParseMouse() {
        Mouse mouse = MICE.parse("201;MX Master 3S;Logitech;99.99;20;WIRELESS;8000");

        assertEquals("Logitech", mouse.getBrand());
        assertEquals(ConnectionType.WIRELESS, mouse.getConnectionType());
        assertEquals(8000, mouse.getDpi());
    }

    @Test
    void shouldParseKeyboard() {
        Keyboard keyboard = KEYBOARDS.parse("301;K2;Keychron;89.99;15;ANSI;WIRELESS");

        assertEquals(KeyboardLayout.ANSI, keyboard.getLayout());
        assertEquals(ConnectionType.WIRELESS, keyboard.getConnectionType());
    }

    @Test
    void shouldAcceptLowerCaseEnumValues() {
        assertEquals(ConnectionType.WIRED, MICE.parse("201;M;B;10;1;wired;800").getConnectionType());
    }

    @Test
    void shouldExposeCsvName() {
        assertEquals("laptops-test.csv", LAPTOPS.csvName());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "x;XPS;Dell;1000;2;Intel Core i7;16;13.4",
            "1;XPS;Dell;abc;2;Intel Core i7;16;13.4",
            "1;XPS;Dell;-5;2;Intel Core i7;16;13.4",
            "1;XPS;Dell;1000;-2;Intel Core i7;16;13.4",
            "1;;Dell;1000;2;Intel Core i7;16;13.4",
            "1;XPS;;1000;2;Intel Core i7;16;13.4",
            "1;XPS;Dell;1000;2;;16;13.4",
            "1;XPS;Dell;1000;2;Intel Core i7;0;13.4",
            "1;XPS;Dell;1000;2;Intel Core i7;16;0",
            "1;XPS;Dell;1000;2;Intel Core i7;16",
            "1;XPS;Dell;1000;2;Intel Core i7;16;13.4;extra",
            ""
    })
    void shouldRejectWrongLaptopLine(String line) {
        assertThrows(IllegalArgumentException.class, () -> LAPTOPS.parse(line));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "101;S;Dell;429;6;27.0;FullHD;165",
            "101;S;Dell;429;6;27.0;2560x1440;0",
            "101;S;Dell;429;6;abc;2560x1440;165"
    })
    void shouldRejectWrongMonitorLine(String line) {
        assertThrows(IllegalArgumentException.class, () -> MONITORS.parse(line));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "201;M;B;10;1;WIRELESSLY;800",
            "201;M;B;10;1;WIRED;0",
            "201;M;B;10;1;WIRED"
    })
    void shouldRejectWrongMouseLine(String line) {
        assertThrows(IllegalArgumentException.class, () -> MICE.parse(line));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "301;K;B;10;1;QWERTY;WIRED",
            "301;K;B;10;1;ANSI;USB"
    })
    void shouldRejectWrongKeyboardLine(String line) {
        assertThrows(IllegalArgumentException.class, () -> KEYBOARDS.parse(line));
    }

    @Test
    void shouldTellExpectedAndFoundFieldCount() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> LAPTOPS.parse("1;a;b"));

        assertTrue(error.getMessage().contains("8"));
        assertTrue(error.getMessage().contains("3"));
    }

    @Test
    void shouldRejectBlankCsvName() {
        assertThrows(IllegalArgumentException.class, () -> new LaptopCsvSource(" "));
        assertThrows(IllegalArgumentException.class, () -> new LaptopCsvSource(null));
    }
}
