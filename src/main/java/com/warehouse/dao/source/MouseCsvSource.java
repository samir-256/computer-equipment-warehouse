package com.warehouse.dao.source;

import com.warehouse.entity.ConnectionType;
import com.warehouse.entity.Mouse;

public class MouseCsvSource extends AbstractCsvSource<Mouse> {

    private static final int CONNECTION_TYPE = FIRST_OWN_COLUMN;
    private static final int DPI = FIRST_OWN_COLUMN + 1;
    private static final int COLUMN_COUNT = FIRST_OWN_COLUMN + 2;

    public MouseCsvSource(String csvName) {
        super(csvName, COLUMN_COUNT);
    }

    @Override
    protected Mouse build(CsvLine fields) {
        return withCommonFields(Mouse.builder(), fields)
                .connectionType(fields.enumValue(CONNECTION_TYPE, ConnectionType.class))
                .dpi(fields.integer(DPI))
                .build();
    }
}
