package com.warehouse.view;

import com.warehouse.controller.Controller;
import com.warehouse.controller.RequestFactory;
import com.warehouse.controller.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public final class ConsoleView implements View {

    private static final String PROMPT = "> ";
    private static final String HELP_COMMAND = "help";

    private final BufferedReader in;
    private final PrintStream out;
    private final Controller controller;
    private final AppInfo info;

    public ConsoleView(BufferedReader in, PrintStream out, Controller controller, AppInfo info) {
        this.in = Objects.requireNonNull(in, "Input must not be null");
        this.out = Objects.requireNonNull(out, "Output must not be null");
        this.controller = Objects.requireNonNull(controller, "Controller must not be null");
        this.info = Objects.requireNonNull(info, "Application info must not be null");
    }

    @Override
    public void start() {
        printStartupScreen();
        boolean running = true;
        while (running) {
            String line = readLine();
            if (line == null) {
                out.println();
                running = false;
            } else if (!line.isBlank()) {
                running = show(controller.handle(RequestFactory.create(line)));
            }
        }
    }

    @Override
    public void crash() {
        out.println("The application stopped because of an unexpected error. Details are in the log.");
    }

    private void printStartupScreen() {
        out.println(info.name());
        out.println("Version: " + info.version());
        out.println("Created: " + info.created());
        out.println("Developer: " + info.developerName() + " <" + info.developerEmail() + ">");
        out.println();
        show(controller.handle(RequestFactory.create(HELP_COMMAND)));
        out.println();
    }

    private String readLine() {
        out.print(PROMPT);
        out.flush();
        try {
            return in.readLine();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read user input", e);
        }
    }

    private boolean show(Response response) {
        boolean ordinaryError = !response.isOk() && !response.isOut();
        out.println(ordinaryError ? "Error: " + response.responseString() : response.responseString());
        return !response.isOut();
    }
}
