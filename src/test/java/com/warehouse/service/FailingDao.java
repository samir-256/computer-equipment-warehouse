package com.warehouse.service;

import com.warehouse.dao.DaoException;
import com.warehouse.dao.EquipmentDao;
import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.entity.Equipment;
import java.util.List;

final class FailingDao<E extends Equipment> implements EquipmentDao<E> {

    static final String MESSAGE = "Data file is broken";

    @Override
    public List<E> find(SearchCriteria<E> criteria) throws DaoException {
        throw new DaoException(MESSAGE);
    }
}
