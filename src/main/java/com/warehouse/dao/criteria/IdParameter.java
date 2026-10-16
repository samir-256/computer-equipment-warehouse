package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;

public record IdParameter(int id) implements Parameter<Equipment> {

    public IdParameter {
        if (id <= 0) {
            throw new IllegalArgumentException("Id must be positive, but was " + id);
        }
    }

    @Override
    public boolean test(Equipment equipment) {
        return equipment.getId() == id;
    }
}
