package com.warehouse.service;

import com.warehouse.dao.DaoException;
import com.warehouse.dao.DaoFactory;
import com.warehouse.dao.criteria.AllMatchCriteria;
import com.warehouse.dao.criteria.BrandParameter;
import com.warehouse.dao.criteria.ConnectionTypeParameter;
import com.warehouse.dao.criteria.IdParameter;
import com.warehouse.dao.criteria.NameParameter;
import com.warehouse.dao.criteria.PriceRangeParameter;
import com.warehouse.dao.criteria.QuantityRangeParameter;
import com.warehouse.dao.criteria.ScreenSizeParameter;
import com.warehouse.dao.criteria.SearchCriteria;
import com.warehouse.entity.Connectable;
import com.warehouse.entity.Equipment;
import com.warehouse.entity.HasScreen;
import com.warehouse.entity.Keyboard;
import com.warehouse.entity.Laptop;
import com.warehouse.entity.Monitor;
import com.warehouse.entity.Mouse;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EquipmentServiceImpl implements EquipmentService {

    @FunctionalInterface
    private interface Finder {

        List<? extends Equipment> find(SearchQuery query) throws DaoException;
    }

    private final Map<Category, Finder> finders = new EnumMap<>(Category.class);

    public EquipmentServiceImpl() {
        finders.put(Category.LAPTOPS, this::findLaptops);
        finders.put(Category.MONITORS, this::findMonitors);
        finders.put(Category.MICE, this::findMice);
        finders.put(Category.KEYBOARDS, this::findKeyboards);
    }

    @Override
    public List<Equipment> search(SearchQuery query) throws ServiceException {
        if (query == null) {
            throw new IllegalArgumentException("Query must not be null");
        }
        checkFilters(query);
        List<Equipment> result = new ArrayList<>();
        try {
            for (Category type : query.category().concreteTypes()) {
                if (appliesTo(type, query)) {
                    result.addAll(finders.get(type).find(query));
                }
            }
        } catch (DaoException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        result.sort(query.sortOrder().comparator());
        return result;
    }

    private void checkFilters(SearchQuery query) {
        Category category = query.category();
        if (query.screenSize() != null && !category.supportsScreenFilter()) {
            throw new IllegalArgumentException(
                    "Screen size filter is not supported for " + label(category));
        }
        if (query.connectionType() != null && !category.supportsConnectionFilter()) {
            throw new IllegalArgumentException(
                    "Connection filter is not supported for " + label(category));
        }
    }

    private boolean appliesTo(Category type, SearchQuery query) {
        boolean screenOk = query.screenSize() == null || type.supportsScreenFilter();
        boolean connectionOk = query.connectionType() == null || type.supportsConnectionFilter();
        return screenOk && connectionOk;
    }

    private String label(Category category) {
        return category.name().toLowerCase(Locale.ROOT);
    }

    private List<Laptop> findLaptops(SearchQuery query) throws DaoException {
        SearchCriteria<Laptop> criteria = commonCriteria(query);
        addScreenFilter(criteria, query);
        return DaoFactory.getDao(Laptop.class).find(criteria);
    }

    private List<Monitor> findMonitors(SearchQuery query) throws DaoException {
        SearchCriteria<Monitor> criteria = commonCriteria(query);
        addScreenFilter(criteria, query);
        return DaoFactory.getDao(Monitor.class).find(criteria);
    }

    private List<Mouse> findMice(SearchQuery query) throws DaoException {
        SearchCriteria<Mouse> criteria = commonCriteria(query);
        addConnectionFilter(criteria, query);
        return DaoFactory.getDao(Mouse.class).find(criteria);
    }

    private List<Keyboard> findKeyboards(SearchQuery query) throws DaoException {
        SearchCriteria<Keyboard> criteria = commonCriteria(query);
        addConnectionFilter(criteria, query);
        return DaoFactory.getDao(Keyboard.class).find(criteria);
    }

    private <E extends Equipment> SearchCriteria<E> commonCriteria(SearchQuery query) {
        SearchCriteria<E> criteria = new AllMatchCriteria<>();
        if (query.id() != null) {
            criteria.add(new IdParameter(query.id()));
        }
        if (query.name() != null) {
            criteria.add(new NameParameter(query.name()));
        }
        if (query.brand() != null) {
            criteria.add(new BrandParameter(query.brand()));
        }
        if (query.price() != null) {
            criteria.add(new PriceRangeParameter(query.price()));
        }
        if (query.quantity() != null) {
            criteria.add(new QuantityRangeParameter(query.quantity()));
        }
        return criteria;
    }

    private <E extends Equipment & HasScreen> void addScreenFilter(SearchCriteria<E> criteria, SearchQuery query) {
        if (query.screenSize() != null) {
            criteria.add(new ScreenSizeParameter<E>(query.screenSize()));
        }
    }

    private <E extends Equipment & Connectable> void addConnectionFilter(SearchCriteria<E> criteria, SearchQuery query) {
        if (query.connectionType() != null) {
            criteria.add(new ConnectionTypeParameter<E>(query.connectionType()));
        }
    }
}
