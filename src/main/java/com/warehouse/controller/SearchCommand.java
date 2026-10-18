package com.warehouse.controller;

import com.warehouse.entity.Equipment;
import com.warehouse.service.EquipmentService;
import com.warehouse.service.SearchQuery;
import com.warehouse.service.ServiceException;
import java.util.List;

abstract class SearchCommand implements Command {

    static final String NOTHING_FOUND = "No products found.";

    private final EquipmentService service;

    protected SearchCommand(EquipmentService service) {
        this.service = service;
    }

    protected abstract SearchQuery toQuery(CommandLine line);

    @Override
    public Response execute(CommandLine line) throws ServiceException {
        SearchQuery query = toQuery(line);
        List<Equipment> items = service.search(query);
        if (items.isEmpty()) {
            return ResponseImpl.ok(NOTHING_FOUND);
        }
        return ResponseImpl.ok("Found " + items.size() + " product(s).",
                ProductTableBuilder.build(query.category(), items));
    }
}
