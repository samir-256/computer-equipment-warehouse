package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;
import com.warehouse.entity.HasScreen;
import java.util.Objects;

public record ScreenSizeParameter<E extends Equipment & HasScreen>(Range<Double> range)
        implements Parameter<E> {

    public ScreenSizeParameter {
        Objects.requireNonNull(range, "Range must not be null");
    }

    @Override
    public boolean test(E equipment) {
        return range.isIn(equipment.getScreenSize());
    }
}
