package com.warehouse.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AppInfoTest {

    private static final LocalDate DATE = LocalDate.of(2026, 1, 2);

    @Test
    void trimsTextFields() {
        AppInfo info = new AppInfo(" App ", " 1.0 ", DATE, " Dev ", " a@b.c ");

        assertEquals("App", info.name());
        assertEquals("1.0", info.version());
        assertEquals("Dev", info.developerName());
        assertEquals("a@b.c", info.developerEmail());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    void rejectsBlankFields(String blank) {
        assertThrows(IllegalArgumentException.class, () -> new AppInfo(blank, "1", DATE, "Dev", "a@b.c"));
        assertThrows(IllegalArgumentException.class, () -> new AppInfo("App", blank, DATE, "Dev", "a@b.c"));
        assertThrows(IllegalArgumentException.class, () -> new AppInfo("App", "1", DATE, blank, "a@b.c"));
        assertThrows(IllegalArgumentException.class, () -> new AppInfo("App", "1", DATE, "Dev", blank));
    }

    @Test
    void rejectsNulls() {
        assertThrows(IllegalArgumentException.class, () -> new AppInfo(null, "1", DATE, "Dev", "a@b.c"));
        assertThrows(IllegalArgumentException.class, () -> new AppInfo("App", "1", null, "Dev", "a@b.c"));
    }
}
