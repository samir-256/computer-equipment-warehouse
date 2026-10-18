package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MainFailureTest {

    private static String runAndCaptureErrors(String... args) {
        PrintStream oldErr = System.err;
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
            Main.main(args);
        } finally {
            System.setErr(oldErr);
        }
        return err.toString(StandardCharsets.UTF_8);
    }

    @Test
    void missingPropertiesFileGivesClearMessage() {
        String errors = runAndCaptureErrors("missing.properties");

        assertTrue(errors.contains("The application cannot start: Properties file not found: missing.properties"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"app-no-name.properties", "app-bad-date.properties", "app-no-data.properties"})
    void brokenPropertiesGiveClearMessage(String resource) {
        String errors = runAndCaptureErrors(resource);

        assertTrue(errors.contains("The application cannot start:"));
        assertTrue(errors.contains(resource));
    }

    @Test
    void tooManyArgumentsShowUsage() {
        String errors = runAndCaptureErrors("one", "two");

        assertTrue(errors.contains("Usage:"));
    }
}
