package com.warehouse.view;

import com.warehouse.common.Holder;

public final class ViewFactory {

    private static final Holder<View> VIEW = new Holder<>("View");

    private ViewFactory() {
    }

    public static void init(View view) {
        VIEW.init(view);
    }

    public static View getView() {
        return VIEW.get();
    }
}
