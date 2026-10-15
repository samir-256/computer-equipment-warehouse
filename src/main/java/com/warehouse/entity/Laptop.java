package com.warehouse.entity;

import java.util.Locale;
import java.util.Objects;

public class Laptop extends Equipment implements HasScreen {

    private static final long serialVersionUID = 1L;

    private final String cpu;
    private final int ramGb;
    private final double screenSize;

    private Laptop(Builder builder) {
        super(builder);
        this.cpu = Validation.notBlank(builder.cpu, "Cpu");
        this.ramGb = Validation.positive(builder.ramGb, "Ram");
        this.screenSize = Validation.positive(builder.screenSize, "Screen size");
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getCpu() {
        return cpu;
    }

    public int getRamGb() {
        return ramGb;
    }

    @Override
    public double getScreenSize() {
        return screenSize;
    }

    @Override
    protected String ownFields() {
        return String.format(Locale.US, "cpu=%s, ramGb=%d, screenSize=%.1f", cpu, ramGb, screenSize);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Laptop that = (Laptop) o;
        return ramGb == that.ramGb
                && Double.compare(screenSize, that.screenSize) == 0
                && cpu.equals(that.cpu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cpu, ramGb, screenSize);
    }

    public static class Builder extends Equipment.Builder<Builder, Laptop> {

        private String cpu;
        private int ramGb;
        private double screenSize;

        public Builder cpu(String cpu) {
            this.cpu = cpu;
            return this;
        }

        public Builder ramGb(int ramGb) {
            this.ramGb = ramGb;
            return this;
        }

        public Builder screenSize(double screenSize) {
            this.screenSize = screenSize;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Laptop build() {
            return new Laptop(this);
        }
    }
}
