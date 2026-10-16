package com.warehouse.dao.criteria;

import com.warehouse.common.Range;
import com.warehouse.entity.Equipment;
import java.util.Objects;

public record PriceRangeParameter(Range<Double> range) implements Parameter<Equipment> {

    public PriceRangeParameter {
        Objects.requireNonNull(range, "Range must not be null");
    }

    @Override
    public boolean test(Equipment equipment) {
        return range.isIn(equipment.getPrice());
    }
}
