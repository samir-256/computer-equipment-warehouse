package com.warehouse.startup;

import java.nio.file.Path;

record StartupArguments(String configResource, Path batchFile) {

    static final String USAGE = "Usage: java -jar computer-equipment-warehouse-1.0.jar"
            + " [--config <properties-file>] [--batch <commands-file>]";

    private static final String CONFIG_OPTION = "--config";
    private static final String BATCH_OPTION = "--batch";

    static StartupArguments parse(String[] args) {
        String config = null;
        String batch = null;
        for (int i = 0; i < args.length; i += 2) {
            if (i + 1 >= args.length) {
                throw new IllegalArgumentException(USAGE);
            }
            switch (args[i]) {
                case CONFIG_OPTION -> config = once(config, args[i + 1]);
                case BATCH_OPTION -> batch = once(batch, args[i + 1]);
                default -> throw new IllegalArgumentException(USAGE);
            }
        }
        return new StartupArguments(
                config == null ? PropertiesConfig.DEFAULT_RESOURCE : config,
                batch == null ? null : Path.of(batch));
    }

    private static String once(String current, String value) {
        if (current != null) {
            throw new IllegalArgumentException(USAGE);
        }
        return value;
    }
}
