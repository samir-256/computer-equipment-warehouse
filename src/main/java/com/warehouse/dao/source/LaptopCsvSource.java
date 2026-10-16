package com.warehouse.dao.source;

import com.warehouse.entity.Laptop;

public class LaptopCsvSource extends AbstractCsvSource<Laptop> {

    private static final int CPU = FIRST_OWN_COLUMN;
    private static final int RAM_GB = FIRST_OWN_COLUMN + 1;
    private static final int SCREEN_SIZE = FIRST_OWN_COLUMN + 2;
    private static final int COLUMN_COUNT = FIRST_OWN_COLUMN + 3;

    public LaptopCsvSource(String csvName) {
        super(csvName, COLUMN_COUNT);
    }

    @Override
    protected Laptop build(CsvLine fields) {
        return withCommonFields(Laptop.builder(), fields)
                .cpu(fields.text(CPU))
                .ramGb(fields.integer(RAM_GB))
                .screenSize(fields.decimal(SCREEN_SIZE))
                .build();
    }
}
