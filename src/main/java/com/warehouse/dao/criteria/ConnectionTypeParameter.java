package com.warehouse.dao.criteria;

import com.warehouse.entity.Connectable;
import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Equipment;
import java.util.Objects;

public record ConnectionTypeParameter<E extends Equipment & Connectable>(ConnectionType type)
        implements Parameter<E> {

    public ConnectionTypeParameter {
        Objects.requireNonNull(type, "Connection type must not be null");
    }

    @Override
    public boolean test(E equipment) {
        return equipment.getConnectionType() == type;
    }
}
