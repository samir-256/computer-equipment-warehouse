package com.warehouse.startup;

import com.warehouse.dao.source.Source;
import com.warehouse.entity.Laptop;

public class SourceWithoutStringConstructor implements Source<Laptop> {

    @Override
    public String csvName() {
        return "none.csv";
    }

    @Override
    public Laptop parse(String line) {
        throw new UnsupportedOperationException();
    }
}
