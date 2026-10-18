package com.warehouse.view;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

final class StubInput {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final PrintStream out = new PrintStream(buffer, true, StandardCharsets.UTF_8);
    private final BufferedReader in;

    StubInput(String text) {
        this.in = new BufferedReader(new StringReader(text));
    }

    BufferedReader in() {
        return in;
    }

    PrintStream out() {
        return out;
    }

    String output() {
        return buffer.toString(StandardCharsets.UTF_8);
    }
}
