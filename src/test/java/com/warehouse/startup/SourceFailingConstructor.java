package com.warehouse.startup;

import com.warehouse.dao.source.Source;
import com.warehouse.entity.Laptop;

public class SourceFailingConstructor implements Source<Laptop> {

    public SourceFailingConstructor(String csvName) {
        throw new IllegalArgumentException("broken source " + csvName);
    }

    @Override
    public String csvName() {
        return "none.csv";
    }

    @Override
    public Laptop parse(String line) {
        throw new UnsupportedOperationException();
    }
}
