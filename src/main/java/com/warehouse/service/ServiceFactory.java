package com.warehouse.service;

import com.warehouse.common.Holder;

public final class ServiceFactory {

    private static final Holder<EquipmentService> SERVICE = new Holder<>("Equipment service");

    private ServiceFactory() {
    }

    public static void init(EquipmentService service) {
        SERVICE.init(service);
    }

    public static EquipmentService getService() {
        return SERVICE.get();
    }
}
