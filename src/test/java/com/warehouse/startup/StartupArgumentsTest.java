package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class StartupArgumentsTest {

    @Test
    void noArgumentsUseDefaults() {
        StartupArguments arguments = StartupArguments.parse(new String[0]);

        assertEquals("app.properties", arguments.configResource());
        assertNull(arguments.batchFile());
    }

    @Test
    void readsConfigAndBatchInAnyOrder() {
        StartupArguments first = StartupArguments.parse(new String[] {"--config", "a.properties", "--batch", "c.txt"});
        StartupArguments second = StartupArguments.parse(new String[] {"--batch", "c.txt", "--config", "a.properties"});

        assertEquals(first, second);
        assertEquals("a.properties", first.configResource());
        assertEquals(Path.of("c.txt"), first.batchFile());
    }

    @Test
    void batchAloneKeepsDefaultConfig() {
        StartupArguments arguments = StartupArguments.parse(new String[] {"--batch", "c.txt"});

        assertEquals("app.properties", arguments.configResource());
    }

    @Test
    void rejectsWrongArguments() {
        assertThrows(IllegalArgumentException.class, () -> StartupArguments.parse(new String[] {"app.properties"}));
        assertThrows(IllegalArgumentException.class, () -> StartupArguments.parse(new String[] {"--batch"}));
        assertThrows(IllegalArgumentException.class, () -> StartupArguments.parse(new String[] {"--color", "red"}));
        assertThrows(IllegalArgumentException.class,
                () -> StartupArguments.parse(new String[] {"--batch", "a.txt", "--batch", "b.txt"}));
        assertThrows(IllegalArgumentException.class,
                () -> StartupArguments.parse(new String[] {"--config", "a", "--config", "b"}));
    }
}
