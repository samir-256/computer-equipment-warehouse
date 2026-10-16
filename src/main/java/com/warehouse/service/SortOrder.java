package com.warehouse.service;

import com.warehouse.entity.Equipment;
import java.util.Comparator;
import java.util.Objects;

public record SortOrder(SortField field, boolean descending) {

    public SortOrder {
        Objects.requireNonNull(field, "Sort field must not be null");
    }

    public static SortOrder ascending(SortField field) {
        return new SortOrder(field, false);
    }

    public static SortOrder descending(SortField field) {
        return new SortOrder(field, true);
    }

    public Comparator<Equipment> comparator() {
        Comparator<Equipment> primary = descending ? field.comparator().reversed() : field.comparator();
        return primary.thenComparingInt(Equipment::getId);
    }
}
