package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.ControllerFactory;
import com.warehouse.view.ViewFactory;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class PropertiesConfigFailureTest {

    private static String failureOf(String resource) {
        return assertThrows(IllegalStateException.class, () -> new PropertiesConfig(resource).init())
                .getMessage();
    }

    @Test
    void missingFileIsReported() {
        assertTrue(failureOf("missing.properties").contains("missing.properties"));
    }

    @Test
    void missingApplicationNameIsReported() {
        assertTrue(failureOf("app-no-name.properties").contains("app.name"));
    }

    @Test
    void missingDataFileNameIsReported() {
        assertTrue(failureOf("app-no-data.properties").contains("data.mice"));
    }

    @Test
    void badDateIsReported() {
        String message = failureOf("app-bad-date.properties");

        assertTrue(message.contains("app.created"));
        assertTrue(message.contains("yesterday"));
    }

    @Test
    void missingBatchFileIsReported() {
        IllegalStateException error = assertThrows(IllegalStateException.class,
                () -> new PropertiesConfig("app-test.properties", Path.of("no-such-commands.txt")).init());

        assertTrue(error.getMessage().contains("no-such-commands.txt"));
    }

    @Test
    void failedConfigurationLeavesFactoriesUntouched() {
        failureOf("app-no-name.properties");

        assertThrows(IllegalStateException.class, ViewFactory::getView);
        assertThrows(IllegalStateException.class, ControllerFactory::getController);
    }
}
