package com.warehouse.dao.criteria;

import com.warehouse.common.Range;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.HasScreen;
import java.util.Objects;
import java.util.function.Function;

public record RangeParameter<E extends Equipment, T extends Comparable<T>>(
        Range<T> range, Function<? super E, T> extractor) implements Parameter<E> {

    public RangeParameter {
        Objects.requireNonNull(range, "Range must not be null");
        Objects.requireNonNull(extractor, "Extractor must not be null");
    }

    public static RangeParameter<Equipment, Double> price(Range<Double> range) {
        return new RangeParameter<>(range, Equipment::getPrice);
    }

    public static RangeParameter<Equipment, Integer> quantity(Range<Integer> range) {
        return new RangeParameter<>(range, Equipment::getQuantity);
    }

    public static <E extends Equipment & HasScreen> RangeParameter<E, Double> screenSize(Range<Double> range) {
        return new RangeParameter<>(range, HasScreen::getScreenSize);
    }

    @Override
    public boolean test(E equipment) {
        return range.isIn(extractor.apply(equipment));
    }
}
