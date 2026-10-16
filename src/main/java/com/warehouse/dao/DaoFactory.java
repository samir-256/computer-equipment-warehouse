package com.warehouse.dao;

import com.warehouse.entity.Equipment;
import java.util.Map;

public final class DaoFactory {

    private static Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> daos;

    private DaoFactory() {
    }

    public static synchronized void init(
            Map<Class<? extends Equipment>, EquipmentDao<? extends Equipment>> daoMap) {
        if (daoMap == null || daoMap.isEmpty()) {
            throw new IllegalArgumentException("Dao map must not be empty");
        }
        if (daos == null) {
            daos = Map.copyOf(daoMap);
        }
    }

    @SuppressWarnings("unchecked")
    public static synchronized <E extends Equipment> EquipmentDao<E> getDao(Class<E> type) {
        if (daos == null) {
            throw new IllegalStateException("Dao factory is not initialized");
        }
        EquipmentDao<? extends Equipment> dao = daos.get(type);
        if (dao == null) {
            throw new IllegalArgumentException("No dao registered for " + type.getSimpleName());
        }
        return (EquipmentDao<E>) dao;
    }
}
