package com.warehouse.controller;

import com.warehouse.common.Holder;

public final class ControllerFactory {

    private static final Holder<Controller> CONTROLLER = new Holder<>("Controller");

    private ControllerFactory() {
    }

    public static void init(Controller controller) {
        CONTROLLER.init(controller);
    }

    public static Controller getController() {
        return CONTROLLER.get();
    }
}
