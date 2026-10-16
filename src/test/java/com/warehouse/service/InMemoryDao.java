package com.warehouse.service;

import com.warehouse.dao.EquipmentDao;
import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.entity.Equipment;
import java.util.List;

final class InMemoryDao<E extends Equipment> implements EquipmentDao<E> {

    private final List<E> items;

    InMemoryDao(List<E> items) {
        this.items = items;
    }

    @Override
    public List<E> find(SearchCriteria<E> criteria) {
        return items.stream().filter(criteria::test).toList();
    }
}
