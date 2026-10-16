package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;
import java.util.Objects;

public record QuantityRangeParameter(Range<Integer> range) implements Parameter<Equipment> {

    public QuantityRangeParameter {
        Objects.requireNonNull(range, "Range must not be null");
    }

    @Override
    public boolean test(Equipment equipment) {
        return range.isIn(equipment.getQuantity());
    }
}
