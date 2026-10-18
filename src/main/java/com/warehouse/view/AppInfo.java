package com.warehouse.view;

import java.time.LocalDate;

public record AppInfo(String name, String version, LocalDate created, String developerName,
                      String developerEmail) {

    public AppInfo {
        name = notBlank(name, "Application name");
        version = notBlank(version, "Version");
        if (created == null) {
            throw new IllegalArgumentException("Creation date must not be null");
        }
        developerName = notBlank(developerName, "Developer name");
        developerEmail = notBlank(developerEmail, "Developer email");
    }

    private static String notBlank(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }
}
