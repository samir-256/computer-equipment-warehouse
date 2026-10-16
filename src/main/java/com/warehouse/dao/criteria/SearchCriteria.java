package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;

public interface SearchCriteria<E extends Equipment> {

    SearchCriteria<E> add(Parameter<? super E> parameter);

    boolean test(E equipment);
}
