package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.ControllerFactory;
import com.warehouse.controller.RequestFactory;
import com.warehouse.controller.Response;
import com.warehouse.view.ViewFactory;
import java.util.List;
import org.junit.jupiter.api.Test;

class PropertiesConfigTest {

    @Test
    void initFillsAllFactoriesFromTestData() {
        new PropertiesConfig("app-test.properties").init();

        Response response = ControllerFactory.getController()
                .handle(RequestFactory.create("find laptops brand=Globex sort=price"));

        assertTrue(response.isOk());
        List<String> names = response.table().orElseThrow().rows().stream()
                .map(row -> row.get(2).text())
                .toList();
        assertEquals(List.of("Beta 13", "Beta 16"), names);
        assertEquals("Found 2 product(s).", response.responseString());
        assertTrue(ViewFactory.getView() != null);
    }

    @Test
    void secondInitKeepsFirstConfiguration() {
        new PropertiesConfig("app-test.properties").init();
        Object first = ControllerFactory.getController();

        new PropertiesConfig("app-test.properties").init();

        assertTrue(first == ControllerFactory.getController());
    }
}
