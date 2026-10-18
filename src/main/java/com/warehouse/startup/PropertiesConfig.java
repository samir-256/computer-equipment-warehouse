package com.warehouse.startup;

import com.warehouse.controller.CommandProvider;
import com.warehouse.controller.ControllerFactory;
import com.warehouse.controller.ControllerImpl;
import com.warehouse.dao.DaoFactory;
import com.warehouse.dao.EquipmentDao;
import com.warehouse.dao.EquipmentDaoImpl;
import com.warehouse.dao.source.Source;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import com.warehouse.service.EquipmentService;
import com.warehouse.service.ServiceFactory;
import com.warehouse.view.AppInfo;
import com.warehouse.view.BorderStyle;
import com.warehouse.view.ConsoleView;
import com.warehouse.view.ViewFactory;
import com.warehouse.view.ViewSettings;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class PropertiesConfig implements Config {

    public static final String DEFAULT_RESOURCE = "app.properties";

    private final String resourceName;
    private final Path batchFile;

    public PropertiesConfig(String resourceName) {
        this(resourceName, null);
    }

    public PropertiesConfig(String resourceName, Path batchFile) {
        this.resourceName = resourceName;
        this.batchFile = batchFile;
    }

    @Override
    public void init() {
        Properties properties = load();
        AppInfo info = appInfo(properties);
        ViewSettings settings = viewSettings(properties);
        BufferedReader input = input();
        Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> daos = daos(properties);
        EquipmentService service = Instances.create(
                required(properties, "service.class"), EquipmentService.class);
        DaoFactory.init(daos);
        ServiceFactory.init(service);
        ControllerFactory.init(new ControllerImpl(new CommandProvider(ServiceFactory.getService())));
        ViewFactory.init(new ConsoleView(
                input,
                System.out,
                ControllerFactory.getController(),
                info,
                settings));
    }

    private BufferedReader input() {
        if (batchFile == null) {
            return new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        }
        try {
            return new BufferedReader(new StringReader(Files.readString(batchFile, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read batch file " + batchFile, e);
        }
    }

    private Properties load() {
        InputStream stream = PropertiesConfig.class.getClassLoader().getResourceAsStream(resourceName);
        if (stream == null) {
            throw new IllegalStateException("Properties file not found: " + resourceName);
        }
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Properties properties = new Properties();
            properties.load(reader);
            return properties;
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read properties file " + resourceName, e);
        }
    }

    private AppInfo appInfo(Properties properties) {
        return new AppInfo(
                required(properties, "app.name"),
                required(properties, "app.version"),
                date(required(properties, "app.created")),
                required(properties, "developer.name"),
                required(properties, "developer.email"));
    }

    private ViewSettings viewSettings(Properties properties) {
        return new ViewSettings(color(properties), borders(properties), batchFile != null);
    }

    private boolean color(Properties properties) {
        String value = required(properties, "view.color").toLowerCase(Locale.ROOT);
        if (!value.equals("true") && !value.equals("false")) {
            throw new IllegalStateException("Property 'view.color' must be true or false in "
                    + resourceName + ", but was '" + value + "'");
        }
        return Boolean.parseBoolean(value);
    }

    private BorderStyle borders(Properties properties) {
        String value = required(properties, "view.borders");
        return Arrays.stream(BorderStyle.values())
                .filter(style -> style.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Property 'view.borders' must be one of "
                        + Arrays.toString(BorderStyle.values()) + " in " + resourceName + ", but was '"
                        + value + "'"));
    }

    private Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> daos(Properties properties) {
        return Map.of(
                Laptop.class, dao(source(properties, "laptops")),
                Monitor.class, dao(source(properties, "monitors")),
                Mouse.class, dao(source(properties, "mice")),
                Keyboard.class, dao(source(properties, "keyboards")));
    }

    private Source<? extends Equipment> source(Properties properties, String name) {
        Source<?> source = Instances.create(required(properties, "source." + name), Source.class,
                required(properties, "data." + name));
        return source;
    }

    private static <E extends Equipment> EquipmentDao<E> dao(Source<E> source) {
        return new EquipmentDaoImpl<>(source);
    }

    private String required(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Property '" + key + "' is missing in " + resourceName);
        }
        return value.trim();
    }

    private LocalDate date(String text) {
        try {
            return LocalDate.parse(text);
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("Property 'app.created' must look like 2026-12-27 in "
                    + resourceName + ", but was '" + text + "'", e);
        }
    }
}
