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
    void unknownSourceClassIsReported() {
        assertTrue(failureOf("app-unknown-source.properties").contains("com.warehouse.NoSuchSource"));
    }

    @Test
    void sourceClassOfWrongTypeIsReported() {
        String message = failureOf("app-wrong-source-type.properties");

        assertTrue(message.contains("java.lang.String"));
        assertTrue(message.contains("Source"));
    }

    @Test
    void sourceWithoutSuitableConstructorIsReported() {
        assertTrue(failureOf("app-no-constructor.properties").contains("no public constructor"));
    }

    @Test
    void failingSourceConstructorIsReported() {
        assertTrue(failureOf("app-failing-constructor.properties").contains("broken source laptops-test.csv"));
    }

    @Test
    void serviceClassOfWrongTypeIsReported() {
        assertTrue(failureOf("app-wrong-service-type.properties").contains("EquipmentService"));
    }

    @Test
    void missingServiceClassPropertyIsReported() {
        assertTrue(failureOf("app-no-service.properties").contains("service.class"));
    }

    @Test
    void missingSourceClassPropertyIsReported() {
        assertTrue(failureOf("app-no-source.properties").contains("source.mice"));
    }

    @Test
    void badColorValueIsReported() {
        String message = failureOf("app-bad-color.properties");

        assertTrue(message.contains("view.color"));
        assertTrue(message.contains("maybe"));
    }

    @Test
    void badBorderStyleIsReported() {
        String message = failureOf("app-bad-borders.properties");

        assertTrue(message.contains("view.borders"));
        assertTrue(message.contains("fancy"));
        assertTrue(message.contains("UNICODE"));
    }

    @Test
    void missingBorderStyleIsReported() {
        assertTrue(failureOf("app-no-borders.properties").contains("view.borders"));
    }

    @Test
    void failedConfigurationLeavesFactoriesUntouched() {
        failureOf("app-no-name.properties");

        assertThrows(IllegalStateException.class, ViewFactory::getView);
        assertThrows(IllegalStateException.class, ControllerFactory::getController);
    }
}
