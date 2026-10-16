package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;

public record BrandParameter(String brand) implements Parameter<Equipment> {

    public BrandParameter {
        if (brand == null || brand.isBlank()) {
            throw new IllegalArgumentException("Brand must not be blank");
        }
        brand = brand.trim();
    }

    @Override
    public boolean test(Equipment equipment) {
        return equipment.getBrand().equalsIgnoreCase(brand);
    }
}
