package com.warehouse.controller;

import com.warehouse.service.EquipmentService;
import com.warehouse.service.SearchQuery;
import com.warehouse.service.ServiceException;

abstract class SearchCommand implements Command {

    private final EquipmentService service;

    protected SearchCommand(EquipmentService service) {
        this.service = service;
    }

    protected abstract SearchQuery toQuery(CommandLine line);

    @Override
    public Response execute(CommandLine line) throws ServiceException {
        SearchQuery query = toQuery(line);
        return ResponseImpl.ok(ResponseFormatter.format(service.search(query)));
    }
}
