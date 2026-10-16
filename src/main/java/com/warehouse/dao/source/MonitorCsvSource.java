package com.warehouse.dao.source;

import com.warehouse.entity.Monitor;

public class MonitorCsvSource extends AbstractCsvSource<Monitor> {

    private static final int SCREEN_SIZE = FIRST_OWN_COLUMN;
    private static final int RESOLUTION = FIRST_OWN_COLUMN + 1;
    private static final int REFRESH_RATE = FIRST_OWN_COLUMN + 2;
    private static final int COLUMN_COUNT = FIRST_OWN_COLUMN + 3;

    public MonitorCsvSource(String csvName) {
        super(csvName, COLUMN_COUNT);
    }

    @Override
    protected Monitor build(CsvLine fields) {
        return withCommonFields(Monitor.builder(), fields)
                .screenSize(fields.decimal(SCREEN_SIZE))
                .resolution(fields.text(RESOLUTION))
                .refreshRate(fields.integer(REFRESH_RATE))
                .build();
    }
}
