package com.warehouse.view;

import com.warehouse.controller.Controller;
import com.warehouse.controller.RequestFactory;
import com.warehouse.controller.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

public final class ConsoleView implements View {

    private static final String PROMPT = "> ";
    private static final String HELP_COMMAND = "help";

    private final BufferedReader in;
    private final PrintStream out;
    private final Controller controller;
    private final AppInfo info;
    private final boolean echoInput;
    private final Style style;
    private final ConsoleRenderer renderer;

    public ConsoleView(BufferedReader in, PrintStream out, Controller controller, AppInfo info) {
        this(in, out, controller, info, ViewSettings.plain());
    }

    public ConsoleView(BufferedReader in, PrintStream out, Controller controller, AppInfo info,
                       ViewSettings settings) {
        this.in = Objects.requireNonNull(in, "Input must not be null");
        this.out = Objects.requireNonNull(out, "Output must not be null");
        this.controller = Objects.requireNonNull(controller, "Controller must not be null");
        this.info = Objects.requireNonNull(info, "Application info must not be null");
        Objects.requireNonNull(settings, "View settings must not be null");
        this.echoInput = settings.echoInput();
        this.style = new Style(settings.color());
        this.renderer = new ConsoleRenderer(settings.borders(), style);
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
        out.println(renderer.renderBox(List.of(
                info.name(),
                "Version: " + info.version(),
                "Created: " + info.created(),
                "Developer: " + info.developerName() + " <" + info.developerEmail() + ">")));
        out.println();
        show(controller.handle(RequestFactory.create(HELP_COMMAND)));
        out.println();
    }

    private String readLine() {
        out.print(style.prompt(PROMPT));
        out.flush();
        try {
            String line = in.readLine();
            if (echoInput && line != null) {
                out.println(line);
            }
            return line;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read user input", e);
        }
    }

    private boolean show(Response response) {
        response.table().ifPresent(table -> out.println(renderer.renderTable(table)));
        out.println(message(response));
        return !response.isOut();
    }

    private String message(Response response) {
        String text = response.responseString();
        if (!response.isOk()) {
            return style.danger(response.isOut() ? text : "Error: " + text);
        }
        return response.table().isPresent() ? style.success(text) : text;
    }
}
