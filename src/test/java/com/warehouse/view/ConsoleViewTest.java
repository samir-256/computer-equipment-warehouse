package com.warehouse.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.warehouse.controller.Controller;
import com.warehouse.controller.Request;
import com.warehouse.controller.Response;
import com.warehouse.controller.Table;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ConsoleViewTest {

    private static final AppInfo INFO = new AppInfo("Test Warehouse", "9.9", LocalDate.of(2026, 1, 2),
            "Test Developer", "dev@example.com");

    private static final class FakeController implements Controller {

        private final List<String> received = new ArrayList<>();

        @Override
        public Response handle(Request request) {
            String text = request.requestString();
            received.add(text);
            return switch (text) {
                case "help" -> response("MENU", true, false);
                case "exit" -> response("Goodbye!", true, true);
                case "bad" -> response("bad command", false, false);
                case "fatal" -> response("Fatal error: broken", false, true);
                case "table" -> tableResponse();
                default -> response("result of " + text, true, false);
            };
        }
    }

    private static Response tableResponse() {
        Table table = new Table("Things",
                List.of(new Table.Column("ID", Table.Alignment.RIGHT), new Table.Column("Name", Table.Alignment.LEFT)),
                List.of(List.of(Table.Cell.of("1"), Table.Cell.of("Alpha"))),
                List.of());
        return new Response() {
            @Override
            public String responseString() {
                return "Found 1 product(s).";
            }

            @Override
            public boolean isOk() {
                return true;
            }

            @Override
            public boolean isOut() {
                return false;
            }

            @Override
            public Optional<Table> table() {
                return Optional.of(table);
            }
        };
    }

    private static Response response(String text, boolean ok, boolean out) {
        return new Response() {
            @Override
            public String responseString() {
                return text;
            }

            @Override
            public boolean isOk() {
                return ok;
            }

            @Override
            public boolean isOut() {
                return out;
            }
        };
    }

    private static String run(String input, FakeController controller) {
        StubInput io = new StubInput(input);
        new ConsoleView(io.in(), io.out(), controller, INFO).start();
        return io.output();
    }

    @Test
    void startupScreenShowsApplicationInfoAndMenu() {
        FakeController controller = new FakeController();

        String output = run("exit\n", controller);

        assertTrue(output.contains("Test Warehouse"));
        assertTrue(output.contains("Version: 9.9"));
        assertTrue(output.contains("Created: 2026-01-02"));
        assertTrue(output.contains("Test Developer"));
        assertTrue(output.contains("dev@example.com"));
        assertTrue(output.contains("MENU"));
        assertEquals("help", controller.received.get(0));
    }

    @Test
    void commandsAreSentToControllerAndResultsAreShown() {
        FakeController controller = new FakeController();

        String output = run("find laptops\nlist mice\nexit\n", controller);

        assertEquals(List.of("help", "find laptops", "list mice", "exit"), controller.received);
        assertTrue(output.contains("result of find laptops"));
        assertTrue(output.contains("result of list mice"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void blankLinesAreNotSentToController() {
        FakeController controller = new FakeController();

        run("\n   \n\t\nexit\n", controller);

        assertEquals(List.of("help", "exit"), controller.received);
    }

    @Test
    void loopStopsWhenControllerSaysOut() {
        FakeController controller = new FakeController();

        String output = run("exit\nfind laptops\n", controller);

        assertEquals(List.of("help", "exit"), controller.received);
        assertFalse(output.contains("result of find laptops"));
    }

    @Test
    void loopStopsWhenInputEnds() {
        FakeController controller = new FakeController();

        run("find laptops\n", controller);

        assertEquals(List.of("help", "find laptops"), controller.received);
    }

    @Test
    void ordinaryErrorIsMarkedAndLoopContinues() {
        FakeController controller = new FakeController();

        String output = run("bad\nfind mice\nexit\n", controller);

        assertTrue(output.contains("Error: bad command"));
        assertTrue(output.contains("result of find mice"));
    }

    @Test
    void fatalResponseIsShownAsIsAndStopsTheLoop() {
        FakeController controller = new FakeController();

        String output = run("fatal\nfind mice\n", controller);

        assertTrue(output.contains("Fatal error: broken"));
        assertFalse(output.contains("Error: Fatal"));
        assertEquals(List.of("help", "fatal"), controller.received);
    }

    @Test
    void echoedInputShowsTheCommandsOfABatchRun() {
        FakeController controller = new FakeController();
        StubInput io = new StubInput("find laptops\nexit\n");

        new ConsoleView(io.in(), io.out(), controller, INFO, new ViewSettings(false, BorderStyle.ASCII, true)).start();

        assertTrue(io.output().contains("> find laptops"));
        assertTrue(io.output().contains("> exit"));
    }

    @Test
    void inputIsNotEchoedByDefault() {
        String output = run("find laptops\nexit\n", new FakeController());

        assertFalse(output.contains("> find laptops"));
    }

    @Test
    void tableIsDrawnBeforeTheMessage() {
        String output = run("table\nexit\n", new FakeController());

        assertTrue(output.contains("| ID | Name  |"));
        assertTrue(output.contains("|  1 | Alpha |"));
        assertTrue(output.indexOf("Alpha") < output.indexOf("Found 1 product(s)."));
    }

    @Test
    void startupInfoIsFramed() {
        String output = run("exit\n", new FakeController());

        assertTrue(output.contains("| Test Warehouse"));
        assertTrue(output.contains("+---"));
    }

    @Test
    void colorSettingPaintsPromptErrorsAndFoundLine() {
        StubInput io = new StubInput("bad\ntable\nexit\n");

        new ConsoleView(io.in(), io.out(), new FakeController(), INFO,
                new ViewSettings(true, BorderStyle.UNICODE, false)).start();

        String output = io.output();
        assertTrue(output.contains("\u001B[32m> \u001B[0m"));
        assertTrue(output.contains("\u001B[31mError: bad command\u001B[0m"));
        assertTrue(output.contains("\u001B[32mFound 1 product(s).\u001B[0m"));
        assertTrue(output.contains("\u250C"));
    }

    @Test
    void plainSettingsNeverPrintColorCodes() {
        String output = run("bad\ntable\nexit\n", new FakeController());

        assertFalse(output.contains("\u001B"));
    }

    @Test
    void settingsMustNotBeNull() {
        StubInput io = new StubInput("");

        assertThrows(NullPointerException.class,
                () -> new ConsoleView(io.in(), io.out(), new FakeController(), INFO, null));
        assertThrows(NullPointerException.class, () -> new ViewSettings(true, null, false));
    }

    @Test
    void crashShowsShortMessage() {
        StubInput io = new StubInput("");

        new ConsoleView(io.in(), io.out(), new FakeController(), INFO).crash();

        assertTrue(io.output().contains("unexpected error"));
    }

    @Test
    void inputFailureBecomesUncheckedException() {
        Reader broken = new Reader() {
            @Override
            public int read(char[] buffer, int offset, int length) throws IOException {
                throw new IOException("keyboard is gone");
            }

            @Override
            public void close() {
            }
        };
        StubInput io = new StubInput("");
        ConsoleView view = new ConsoleView(new BufferedReader(broken), io.out(), new FakeController(), INFO);

        assertThrows(UncheckedIOException.class, view::start);
    }

    @Test
    void constructorRejectsNulls() {
        StubInput io = new StubInput("");
        FakeController controller = new FakeController();

        assertThrows(NullPointerException.class, () -> new ConsoleView(null, io.out(), controller, INFO));
        assertThrows(NullPointerException.class, () -> new ConsoleView(io.in(), null, controller, INFO));
        assertThrows(NullPointerException.class, () -> new ConsoleView(io.in(), io.out(), null, INFO));
        assertThrows(NullPointerException.class, () -> new ConsoleView(io.in(), io.out(), controller, null));
    }
}
