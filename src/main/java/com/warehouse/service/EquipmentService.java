package com.warehouse.service;

import com.warehouse.entity.Equipment;
import java.util.List;

public interface EquipmentService {

    List<Equipment> search(SearchQuery query) throws ServiceException;

    default List<Equipment> listAll(Category category, SortOrder sortOrder) throws ServiceException {
        return search(SearchQuery.builder().category(category).sortOrder(sortOrder).build());
    }
}
