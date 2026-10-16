package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;
import java.util.Locale;

public record NameParameter(String text) implements Parameter<Equipment> {

    public NameParameter {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Name text must not be blank");
        }
        text = text.trim();
    }

    @Override
    public boolean test(Equipment equipment) {
        return equipment.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT));
    }
}
