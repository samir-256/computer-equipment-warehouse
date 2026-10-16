package com.warehouse.dao.source;

import com.warehouse.entity.Equipment;

public interface Source<E extends Equipment> {

    String csvName();

    E parse(String line);
}
