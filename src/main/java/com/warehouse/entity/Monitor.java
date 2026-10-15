package com.warehouse.entity;

import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class Monitor extends Equipment implements HasScreen {

    private static final long serialVersionUID = 1L;
    private static final Pattern RESOLUTION_FORMAT = Pattern.compile("\\d{3,5}x\\d{3,5}");

    private final double screenSize;
    private final String resolution;
    private final int refreshRate;

    private Monitor(Builder builder) {
        super(builder);
        this.screenSize = Validation.positive(builder.screenSize, "Screen size");
        this.resolution = Validation.matches(builder.resolution, RESOLUTION_FORMAT, "Resolution");
        this.refreshRate = Validation.positive(builder.refreshRate, "Refresh rate");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public double getScreenSize() {
        return screenSize;
    }

    public String getResolution() {
        return resolution;
    }

    public int getRefreshRate() {
        return refreshRate;
    }

    @Override
    protected String ownFields() {
        return String.format(Locale.US, "screenSize=%.1f, resolution=%s, refreshRate=%d",
                screenSize, resolution, refreshRate);
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Monitor that = (Monitor) o;
        return refreshRate == that.refreshRate
                && Double.compare(screenSize, that.screenSize) == 0
                && resolution.equals(that.resolution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), screenSize, resolution, refreshRate);
    }

    public static class Builder extends Equipment.Builder<Builder, Monitor> {

        private double screenSize;
        private String resolution;
        private int refreshRate;

        public Builder screenSize(double screenSize) {
            this.screenSize = screenSize;
            return this;
        }

        public Builder resolution(String resolution) {
            this.resolution = resolution;
            return this;
        }

        public Builder refreshRate(int refreshRate) {
            this.refreshRate = refreshRate;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Monitor build() {
            return new Monitor(this);
        }
    }
}
