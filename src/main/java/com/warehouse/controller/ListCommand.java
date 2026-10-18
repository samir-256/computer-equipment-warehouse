package com.warehouse.controller;

import com.warehouse.service.EquipmentService;
import com.warehouse.service.SearchQuery;

final class ListCommand extends SearchCommand {

    ListCommand(EquipmentService service) {
        super(service);
    }

    @Override
    protected SearchQuery toQuery(CommandLine line) {
        return SearchQueryFactory.forList(line);
    }
}
