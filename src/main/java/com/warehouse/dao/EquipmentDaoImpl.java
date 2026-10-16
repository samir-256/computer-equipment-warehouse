package com.warehouse.dao;

import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.dao.source.Source;
import com.warehouse.entity.Equipment;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EquipmentDaoImpl<E extends Equipment> implements EquipmentDao<E> {

    private final Source<E> source;

    public EquipmentDaoImpl(Source<E> source) {
        this.source = Objects.requireNonNull(source, "Source must not be null");
    }

    @Override
    public List<E> find(SearchCriteria<E> criteria) throws DaoException {
        Objects.requireNonNull(criteria, "Criteria must not be null");
        try (BufferedReader reader = openReader()) {
            return readMatching(reader, criteria);
        } catch (IOException e) {
            throw new DaoException("Cannot read file " + source.csvName(), e);
        }
    }

    private BufferedReader openReader() throws DaoException {
        InputStream stream = EquipmentDaoImpl.class.getClassLoader().getResourceAsStream(source.csvName());
        if (stream == null) {
            throw new DaoException("File not found: " + source.csvName());
        }
        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private List<E> readMatching(BufferedReader reader, SearchCriteria<E> criteria)
            throws IOException, DaoException {
        List<E> found = new ArrayList<>();
        reader.readLine();
        int number = 1;
        String line;
        while ((line = reader.readLine()) != null) {
            number++;
            if (line.isBlank()) {
                continue;
            }
            E item = parse(line, number);
            if (criteria.test(item)) {
                found.add(item);
            }
        }
        return found;
    }

    private E parse(String line, int number) throws DaoException {
        try {
            return source.parse(line);
        } catch (IllegalArgumentException e) {
            throw new DaoException(source.csvName() + ", line " + number + ": " + e.getMessage(), e);
        }
    }
}
