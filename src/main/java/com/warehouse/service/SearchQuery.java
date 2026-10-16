package com.warehouse.service;

import com.warehouse.common.Range;
import com.warehouse.entity.ConnectionType;

public record SearchQuery(
        Category category,
        Integer id,
        String name,
        String brand,
        Range<Double> price,
        Range<Integer> quantity,
        Range<Double> screenSize,
        ConnectionType connectionType,
        SortOrder sortOrder) {

    public SearchQuery {
        if (category == null) {
            throw new IllegalArgumentException("Category must not be null");
        }
        if (id != null && id <= 0) {
            throw new IllegalArgumentException("Id must be positive, but was " + id);
        }
        if (name != null) {
            name = notBlank(name, "Name");
        }
        if (brand != null) {
            brand = notBlank(brand, "Brand");
        }
        if (sortOrder == null) {
            sortOrder = SortOrder.ascending(SortField.ID);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private static String notBlank(String value, String field) {
        if (value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.trim();
    }

    public static final class Builder {

        private Category category;
        private Integer id;
        private String name;
        private String brand;
        private Range<Double> price;
        private Range<Integer> quantity;
        private Range<Double> screenSize;
        private ConnectionType connectionType;
        private SortOrder sortOrder;

        private Builder() {
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder price(Range<Double> price) {
            this.price = price;
            return this;
        }

        public Builder quantity(Range<Integer> quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder screenSize(Range<Double> screenSize) {
            this.screenSize = screenSize;
            return this;
        }

        public Builder connectionType(ConnectionType connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public Builder sortOrder(SortOrder sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public SearchQuery build() {
            return new SearchQuery(category, id, name, brand, price, quantity, screenSize,
                    connectionType, sortOrder);
        }
    }
}
