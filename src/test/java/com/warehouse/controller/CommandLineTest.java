package com.warehouse.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CommandLineTest {

    @Test
    void parsesCommandWithoutSpace() {
        CommandLine line = CommandLine.parse("exit");

        assertEquals("exit", line.name());
        assertTrue(line.positionals().isEmpty());
        assertTrue(line.options().isEmpty());
    }

    @Test
    void parsesPositionalsAndOptions() {
        CommandLine line = CommandLine.parse("find laptops brand=Dell maxprice=1000");

        assertEquals("find", line.name());
        assertEquals(List.of("laptops"), line.positionals());
        assertEquals(Map.of("brand", "Dell", "maxprice", "1000"), line.options());
    }

    @Test
    void commandAndKeysAreCaseInsensitiveButValuesAreKept() {
        CommandLine line = CommandLine.parse("FIND Laptops BRAND=Dell");

        assertEquals("find", line.name());
        assertEquals(Map.of("brand", "Dell"), line.options());
    }

    @Test
    void quotedValueKeepsSpaces() {
        CommandLine line = CommandLine.parse("find all name=\"XPS 13\"   brand=Dell");

        assertEquals("XPS 13", line.options().get("name"));
        assertEquals("Dell", line.options().get("brand"));
    }

    @Test
    void valueMayContainEqualsSign() {
        assertEquals("a=b", CommandLine.parse("find all name=a=b").options().get("name"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "\t", "\"\"", "find all name=\"XPS 13", "find all =Dell",
            "find all name=", "find all name=\"\"", "find all brand=A brand=B"})
    void rejectsBrokenText(String text) {
        assertThrows(IllegalArgumentException.class, () -> CommandLine.parse(text));
    }

    @Test
    void noArgumentsCheckPassesForBareCommand() {
        CommandLine.parse("help").requireNoArguments();
    }

    @ParameterizedTest
    @ValueSource(strings = {"help me", "help a=b", "exit now"})
    void noArgumentsCheckFailsWhenArgumentsGiven(String text) {
        CommandLine line = CommandLine.parse(text);

        assertThrows(IllegalArgumentException.class, line::requireNoArguments);
    }

    @Test
    void unknownOptionIsReportedWithAllowedList() {
        CommandLine line = CommandLine.parse("find all color=red");

        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> line.requireOptionsIn(Set.of("name", "brand")));

        assertTrue(error.getMessage().contains("color"));
        assertTrue(error.getMessage().contains("brand, name"));
    }

    @Test
    void singlePositionalRequiresExactlyOne() {
        assertEquals("all", CommandLine.parse("find all").singlePositional("usage"));
        assertThrows(IllegalArgumentException.class, () -> CommandLine.parse("find").singlePositional("usage"));
        assertThrows(IllegalArgumentException.class,
                () -> CommandLine.parse("find all mice").singlePositional("usage"));
    }
}
