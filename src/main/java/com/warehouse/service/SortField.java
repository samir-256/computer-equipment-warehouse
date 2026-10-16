package com.warehouse.service;

import com.warehouse.entity.Equipment;
import java.util.Comparator;

public enum SortField {
    ID(Comparator.comparingInt(Equipment::getId)),
    NAME(Comparator.comparing(Equipment::getName, String.CASE_INSENSITIVE_ORDER)),
    BRAND(Comparator.comparing(Equipment::getBrand, String.CASE_INSENSITIVE_ORDER)),
    PRICE(Comparator.comparingDouble(Equipment::getPrice)),
    QUANTITY(Comparator.comparingInt(Equipment::getQuantity)),
    TOTAL_COST(Comparator.comparingDouble(Equipment::totalCost));

    private final Comparator<Equipment> comparator;

    SortField(Comparator<Equipment> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Equipment> comparator() {
        return comparator;
    }
}
