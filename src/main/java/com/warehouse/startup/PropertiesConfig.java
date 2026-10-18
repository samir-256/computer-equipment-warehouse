package com.warehouse.startup;

import com.warehouse.controller.CommandProvider;
import com.warehouse.controller.ControllerFactory;
import com.warehouse.controller.ControllerImpl;
import com.warehouse.dao.DaoFactory;
import com.warehouse.dao.EquipmentDao;
import com.warehouse.dao.EquipmentDaoImpl;
import com.warehouse.dao.source.KeyboardCsvSource;
import com.warehouse.dao.source.LaptopCsvSource;
import com.warehouse.dao.source.MonitorCsvSource;
import com.warehouse.dao.source.MouseCsvSource;
import com.warehouse.dao.source.Source;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import com.warehouse.service.EquipmentServiceImpl;
import com.warehouse.service.ServiceFactory;
import com.warehouse.view.AppInfo;
import com.warehouse.view.ConsoleView;
import com.warehouse.view.ViewFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Properties;

public final class PropertiesConfig implements Config {

    public static final String DEFAULT_RESOURCE = "app.properties";

    private final String resourceName;

    public PropertiesConfig(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public void init() {
        Properties properties = load();
        AppInfo info = appInfo(properties);
        DaoFactory.init(daos(properties));
        ServiceFactory.init(new EquipmentServiceImpl());
        ControllerFactory.init(new ControllerImpl(new CommandProvider(ServiceFactory.getService())));
        ViewFactory.init(new ConsoleView(
                new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)),
                System.out,
                ControllerFactory.getController(),
                info));
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

    private Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> daos(Properties properties) {
        return Map.of(
                Laptop.class, dao(new LaptopCsvSource(required(properties, "data.laptops"))),
                Monitor.class, dao(new MonitorCsvSource(required(properties, "data.monitors"))),
                Mouse.class, dao(new MouseCsvSource(required(properties, "data.mice"))),
                Keyboard.class, dao(new KeyboardCsvSource(required(properties, "data.keyboards"))));
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
            throw new IllegalStateException("Property 'app.created' must look like 2026-12-27 in " + resourceName + ", but was '" + text + "'", e);
        }
    }
}
