package com.warehouse.startup;

import com.warehouse.view.View;
import com.warehouse.view.ViewFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private Main() {
    }

    public static void main(String[] args) {
        View view = null;
        try {
            StartupArguments arguments = StartupArguments.parse(args);
            Config config = new PropertiesConfig(arguments.configResource(), arguments.batchFile());
            config.init();
            view = ViewFactory.getView();
            view.start();
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Application failed", e);
            crash(view, e);
        }
    }

    private static void crash(View view, RuntimeException cause) {
        if (view == null) {
            System.err.println("The application cannot start: " + cause.getMessage());
        } else {
            view.crash();
        }
    }
}
