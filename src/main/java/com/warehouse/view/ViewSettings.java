package com.warehouse.view;

import java.util.Objects;

public record ViewSettings(boolean color, BorderStyle borders, boolean echoInput) {

    public ViewSettings {
        Objects.requireNonNull(borders, "Border style must not be null");
    }

    public static ViewSettings plain() {
        return new ViewSettings(false, BorderStyle.ASCII, false);
    }
}
