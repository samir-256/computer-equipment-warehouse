package com.warehouse.dao.source;

import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.KeyboardLayout;

public class KeyboardCsvSource extends AbstractCsvSource<Keyboard> {

    private static final int LAYOUT = FIRST_OWN_COLUMN;
    private static final int CONNECTION_TYPE = FIRST_OWN_COLUMN + 1;
    private static final int COLUMN_COUNT = FIRST_OWN_COLUMN + 2;

    public KeyboardCsvSource(String csvName) {
        super(csvName, COLUMN_COUNT);
    }

    @Override
    protected Keyboard build(CsvLine fields) {
        return withCommonFields(Keyboard.builder(), fields)
                .layout(fields.enumValue(LAYOUT, KeyboardLayout.class))
                .connectionType(fields.enumValue(CONNECTION_TYPE, ConnectionType.class))
                .build();
    }
}
