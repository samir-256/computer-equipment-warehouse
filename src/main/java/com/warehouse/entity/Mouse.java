package com.warehouse.entity;

import java.util.Objects;

public class Mouse extends Equipment implements Connectable {

    private static final long serialVersionUID = 1L;

    private final ConnectionType connectionType;
    private final int dpi;

    private Mouse(Builder builder) {
        super(builder);
        this.connectionType = Validation.notNull(builder.connectionType, "Connection type");
        this.dpi = Validation.positive(builder.dpi, "Dpi");
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public int getDpi() {
        return dpi;
    }

    @Override
    protected String ownFields() {
        return "connectionType=" + connectionType + ", dpi=" + dpi;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Mouse that = (Mouse) o;
        return dpi == that.dpi && connectionType == that.connectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), connectionType, dpi);
    }

    public static class Builder extends Equipment.Builder<Builder, Mouse> {

        private ConnectionType connectionType;
        private int dpi;

        public Builder connectionType(ConnectionType connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public Builder dpi(int dpi) {
            this.dpi = dpi;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Mouse build() {
            return new Mouse(this);
        }
    }
}
