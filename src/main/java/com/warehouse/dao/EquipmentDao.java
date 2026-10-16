package com.warehouse.dao;

import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.entity.Equipment;
import java.util.List;

public interface EquipmentDao<E extends Equipment> {

    List<E> find(SearchCriteria<E> criteria) throws DaoException;
}
