package com.warehouse.dao.criteria;

import com.warehouse.entity.Equipment;

@FunctionalInterface
public interface Parameter<E extends Equipment> {

    boolean test(E equipment);

    static <E extends Equipment> Parameter<E> any() {
        return equipment -> true;
    }

    static <E extends Equipment> Parameter<E> none() {
        return equipment -> false;
    }
}
