package com.warehouse.controller;

import com.warehouse.entity.Equipment;
import com.warehouse.service.EquipmentService;
import com.warehouse.service.SearchQuery;
import com.warehouse.service.ServiceException;
import java.util.List;

final class StubService implements EquipmentService {

    private final List<Equipment> result;
    private final ServiceException failure;
    private SearchQuery lastQuery;

    StubService(List<Equipment> result) {
        this.result = result;
        this.failure = null;
    }

    StubService(ServiceException failure) {
        this.result = List.of();
        this.failure = failure;
    }

    SearchQuery lastQuery() {
        return lastQuery;
    }

    @Override
    public List<Equipment> search(SearchQuery query) throws ServiceException {
        lastQuery = query;
        if (failure != null) {
            throw failure;
        }
        return result;
    }
}
