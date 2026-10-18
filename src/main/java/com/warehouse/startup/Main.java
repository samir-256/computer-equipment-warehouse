package com.warehouse.startup;

import com.warehouse.view.View;
import com.warehouse.view.ViewFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String USAGE = "Usage: java -jar computer-equipment-warehouse-1.0.jar [properties-file]";

    private Main() {
    }

    public static void main(String[] args) {
        View view = null;
        try {
            if (args.length > 1) {
                throw new IllegalArgumentException(USAGE);
            }
            String resource = args.length == 1 ? args[0] : PropertiesConfig.DEFAULT_RESOURCE;
            Config config = new PropertiesConfig(resource);
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
