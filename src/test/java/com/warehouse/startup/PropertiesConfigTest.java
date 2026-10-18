package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.ControllerFactory;
import com.warehouse.controller.RequestFactory;
import com.warehouse.controller.Response;
import com.warehouse.view.ViewFactory;
import org.junit.jupiter.api.Test;

class PropertiesConfigTest {

    @Test
    void initFillsAllFactoriesFromTestData() {
        new PropertiesConfig("app-test.properties").init();

        Response response = ControllerFactory.getController()
                .handle(RequestFactory.create("find laptops brand=Globex sort=price"));

        assertTrue(response.isOk());
        assertTrue(response.responseString().contains("Beta 13"));
        assertTrue(response.responseString().contains("Beta 16"));
        assertTrue(response.responseString().contains("Found 2 product(s)."));
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
