package com.warehouse.entity;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

public abstract class Equipment implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int id;
    private final String name;
    private final String brand;
    private final double price;
    private final int quantity;

    protected Equipment(Builder<?, ?> builder) {
        this.id = Validation.positive(builder.id, "Id");
        this.name = Validation.notBlank(builder.name, "Name");
        this.brand = Validation.notBlank(builder.brand, "Brand");
        this.price = Validation.positive(builder.price, "Price");
        this.quantity = Validation.notNegative(builder.quantity, "Quantity");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public double totalCost() {
        return price * quantity;
    }

    protected abstract String ownFields();

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%s{id=%d, name=%s, brand=%s, price=%.2f, quantity=%d, totalCost=%.2f, %s}",
                getClass().getSimpleName(), id, name, brand, price, quantity, totalCost(), ownFields());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Equipment that = (Equipment) o;
        return id == that.id
                && quantity == that.quantity
                && Double.compare(price, that.price) == 0
                && name.equals(that.name)
                && brand.equals(that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, price, quantity);
    }

    public abstract static class Builder<SELF extends Builder<SELF, T>, T extends Equipment>
            implements ObjBuilder<T> {

        private int id;
        private String name;
        private String brand;
        private double price;
        private int quantity;

        protected abstract SELF self();

        public SELF id(int id) {
            this.id = id;
            return self();
        }

        public SELF name(String name) {
            this.name = name;
            return self();
        }

        public SELF brand(String brand) {
            this.brand = brand;
            return self();
        }

        public SELF price(double price) {
            this.price = price;
            return self();
        }

        public SELF quantity(int quantity) {
            this.quantity = quantity;
            return self();
        }
    }
}
