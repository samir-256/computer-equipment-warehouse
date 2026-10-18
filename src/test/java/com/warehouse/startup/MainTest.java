package com.warehouse.startup;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class MainTest {

    private static String run(String input, String... args) {
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;
        PrintStream oldErr = System.err;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
            System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
            Main.main(args);
        } finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
            System.setErr(oldErr);
        }
        return out.toString(StandardCharsets.UTF_8) + err.toString(StandardCharsets.UTF_8);
    }

    @Test
    void fullSessionWorksFromStartToExit() {
        String output = run("list laptops sort=price:desc\nfind laptops brand=Nobody\nfind mice connection=wired\n"
                + "dance\nexit\nlist mice\n", "--config", "app-test.properties");

        assertTrue(output.contains("Test Warehouse"));
        assertTrue(output.contains("Version: 9.9"));
        assertTrue(output.contains("Test Developer"));
        assertTrue(output.contains("Commands:"));
        assertTrue(output.contains("Found 5 product(s)."));
        assertTrue(output.contains("No products found."));
        assertTrue(output.contains("Click One"));
        assertTrue(output.contains("Error: Unknown command 'dance'"));
        assertTrue(output.contains("Goodbye!"));
        assertFalse(output.contains("Click Three"));
    }
}
