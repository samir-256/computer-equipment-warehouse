package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.Samples;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import com.warehouse.service.Category;
import com.warehouse.service.EquipmentService;
import com.warehouse.service.ServiceException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ControllerImplTest {

    private static final List<Equipment> ITEMS = List.of(
            Samples.laptop(1, "Acme", 1000.0, 2, 15.6),
            Samples.mouse(201, "Globex", 45.0, 4, ConnectionType.WIRED));

    private static Response handle(Controller controller, String text) {
        return controller.handle(RequestFactory.create(text));
    }

    private static Controller controller(StubService service) {
        return new ControllerImpl(new CommandProvider(service));
    }

    @Test
    void findPrintsMatchingProducts() {
        StubService service = new StubService(ITEMS);

        Response response = handle(controller(service), "find all brand=Acme");

        assertTrue(response.isOk());
        assertFalse(response.isOut());
        assertTrue(response.responseString().contains("Laptop 1"));
        assertTrue(response.responseString().contains("Found 2 product(s)."));
        assertEquals("Acme", service.lastQuery().brand());
        assertEquals(Category.ALL, service.lastQuery().category());
    }

    @Test
    void findWithoutMatchesSaysNothingFound() {
        Response response = handle(controller(new StubService(List.of())), "find laptops brand=Nobody");

        assertTrue(response.isOk());
        assertFalse(response.isOut());
        assertEquals("No products found.", response.responseString());
    }

    @Test
    void listPassesCategoryAndSortToService() {
        StubService service = new StubService(ITEMS);

        Response response = handle(controller(service), "list mice sort=price:desc");

        assertTrue(response.isOk());
        assertEquals(Category.MICE, service.lastQuery().category());
        assertTrue(service.lastQuery().sortOrder().descending());
    }

    @Test
    void helpShowsMenu() {
        Response response = handle(controller(new StubService(List.of())), "help");

        assertTrue(response.isOk());
        assertFalse(response.isOut());
        assertEquals(HelpCommand.TEXT, response.responseString());
    }

    @Test
    void helpMentionsEveryCommandCategoryAndFilter() {
        for (String word : List.of("find", "list", "help", "exit", "laptops", "monitors", "mice", "keyboards",
                "all", "id=", "name=", "brand=", "minprice", "maxprice", "minqty", "maxqty", "minscreen",
                "maxscreen", "connection=", "sort")) {
            assertTrue(HelpCommand.TEXT.contains(word), word);
        }
    }

    @Test
    void exitStopsTheApplication() {
        Response response = handle(controller(new StubService(List.of())), "exit");

        assertTrue(response.isOk());
        assertTrue(response.isOut());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "dance", "fnd all", "find", "find phones", "find all minprice=abc",
            "find all minprice=9 maxprice=1", "find all colour=red", "find all name=\"unclosed",
            "list", "list all brand=Dell", "list all sort=weight", "help me", "exit now", "=", "\"", "find all ="})
    void invalidCommandsGiveClearErrorAndKeepApplicationRunning(String text) {
        StubService service = new StubService(ITEMS);

        Response response = handle(controller(service), text);

        assertFalse(response.isOk());
        assertFalse(response.isOut());
        assertFalse(response.responseString().isBlank());
        assertNull(service.lastQuery());
    }

    @Test
    void unknownCommandMentionsItsNameAndHelp() {
        Response response = handle(controller(new StubService(List.of())), "Dance now");

        assertTrue(response.responseString().contains("dance"));
        assertTrue(response.responseString().contains("help"));
    }

    @Test
    void unsupportedFilterRejectedByServiceIsAnOrdinaryError() {
        EquipmentService rejecting = query -> {
            throw new IllegalArgumentException("Category MICE does not support screen filter");
        };
        Controller controller = new ControllerImpl(new CommandProvider(rejecting));

        Response response = handle(controller, "find mice minscreen=13");

        assertFalse(response.isOk());
        assertFalse(response.isOut());
        assertEquals("Category MICE does not support screen filter", response.responseString());
    }

    @Test
    void serviceFailureIsFatal() {
        StubService service = new StubService(new ServiceException("laptops.csv, line 3: bad price"));

        Response response = handle(controller(service), "list laptops");

        assertFalse(response.isOk());
        assertTrue(response.isOut());
        assertTrue(response.responseString().contains("laptops.csv, line 3: bad price"));
    }

    @Test
    void nullRequestIsRejected() {
        Controller controller = controller(new StubService(List.of()));

        assertThrows(NullPointerException.class, () -> controller.handle(null));
    }

    @Test
    void nullTextCannotBecomeRequest() {
        assertThrows(IllegalArgumentException.class, () -> RequestFactory.create(null));
    }

    @Test
    void controllerNeedsProvider() {
        assertThrows(NullPointerException.class, () -> new ControllerImpl(null));
        assertThrows(NullPointerException.class, () -> new CommandProvider(null));
    }
}
