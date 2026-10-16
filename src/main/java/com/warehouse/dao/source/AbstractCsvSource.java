package com.warehouse.dao.source;

import com.warehouse.entity.Equipment;

public abstract class AbstractCsvSource<E extends Equipment> implements Source<E> {

    private static final int ID = 0;
    private static final int NAME = 1;
    private static final int BRAND = 2;
    private static final int PRICE = 3;
    private static final int QUANTITY = 4;

    protected static final int FIRST_OWN_COLUMN = 5;

    private final String csvName;
    private final int columnCount;

    protected AbstractCsvSource(String csvName, int columnCount) {
        if (csvName == null || csvName.isBlank()) {
            throw new IllegalArgumentException("Csv name must not be blank");
        }
        if (columnCount < FIRST_OWN_COLUMN) {
            throw new IllegalArgumentException("Column count must be at least " + FIRST_OWN_COLUMN);
        }
        this.csvName = csvName;
        this.columnCount = columnCount;
    }

    @Override
    public String csvName() {
        return csvName;
    }

    @Override
    public E parse(String line) {
        CsvLine fields = CsvLine.of(line);
        if (fields.size() != columnCount) {
            throw new IllegalArgumentException(
                    "Expected " + columnCount + " fields, but found " + fields.size());
        }
        return build(fields);
    }

    protected abstract E build(CsvLine fields);

    protected <B extends Equipment.Builder<B, E>> B withCommonFields(B builder, CsvLine fields) {
        builder.id(fields.integer(ID))
                .name(fields.text(NAME))
                .brand(fields.text(BRAND))
                .price(fields.decimal(PRICE))
                .quantity(fields.integer(QUANTITY));
        return builder;
    }
}
