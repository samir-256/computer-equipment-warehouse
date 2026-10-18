package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class MainBatchTest {

    @Test
    void commandsAreReadFromFileInsteadOfKeyboard() throws IOException {
        Path commands = Files.createTempFile("commands", ".txt");
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            Files.writeString(commands, "find laptops brand=Globex\n\ndance\nexit\nlist mice\n");
            System.setIn(new ByteArrayInputStream("list keyboards\n".getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));

            Main.main(new String[] {"--config", "app-test.properties", "--batch", commands.toString()});
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
            Files.deleteIfExists(commands);
        }
        String output = out.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("> find laptops brand=Globex"));
        assertTrue(output.contains("Beta 13"));
        assertTrue(output.contains("Error: Unknown command 'dance'"));
        assertTrue(output.contains("Goodbye!"));
        assertFalse(output.contains("Click One"));
        assertFalse(output.contains("Type One"));
    }
}
