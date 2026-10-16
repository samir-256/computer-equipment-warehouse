package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AllMatchCriteria<E extends Equipment> implements SearchCriteria<E> {

    private final List<Parameter<? super E>> parameters = new ArrayList<>();

    @Override
    public SearchCriteria<E> add(Parameter<? super E> parameter) {
        parameters.add(Objects.requireNonNull(parameter, "Parameter must not be null"));
        return this;
    }

    @Override
    public boolean test(E equipment) {
        return parameters.stream().allMatch(parameter -> parameter.test(equipment));
    }
}
