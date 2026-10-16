package com.warehouse;

import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.KeyboardLayout;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;

public final class Samples {

    private Samples() {
    }

    public static Laptop laptop(int id, String brand, double price, int quantity, double screenSize) {
        return Laptop.builder()
                .id(id)
                .name("Laptop " + id)
                .brand(brand)
                .price(price)
                .quantity(quantity)
                .cpu("Intel Core i5")
                .ramGb(8)
                .screenSize(screenSize)
                .build();
    }

    public static Monitor monitor(int id, double screenSize) {
        return Monitor.builder()
                .id(id)
                .name("Monitor " + id)
                .brand("Acme")
                .price(300.0)
                .quantity(1)
                .screenSize(screenSize)
                .resolution("1920x1080")
                .refreshRate(60)
                .build();
    }

    public static Mouse mouse(int id, String brand, double price, int quantity, ConnectionType type) {
        return Mouse.builder()
                .id(id)
                .name("Mouse " + id)
                .brand(brand)
                .price(price)
                .quantity(quantity)
                .connectionType(type)
                .dpi(1600)
                .build();
    }

    public static Keyboard keyboard(int id, ConnectionType type) {
        return Keyboard.builder()
                .id(id)
                .name("Keyboard " + id)
                .brand("Acme")
                .price(50.0)
                .quantity(1)
                .layout(KeyboardLayout.ANSI)
                .connectionType(type)
                .build();
    }
}
