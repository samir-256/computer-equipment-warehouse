package com.warehouse.entity;

import java.util.Objects;

public class Keyboard extends Equipment implements Connectable {

    private static final long serialVersionUID = 1L;

    private final KeyboardLayout layout;
    private final ConnectionType connectionType;

    private Keyboard(Builder builder) {
        super(builder);
        this.layout = Validation.notNull(builder.layout, "Layout");
        this.connectionType = Validation.notNull(builder.connectionType, "Connection type");
    }

    public static Builder builder() {
        return new Builder();
    }

    public KeyboardLayout getLayout() {
        return layout;
    }

    @Override
    public ConnectionType getConnectionType() {
        return connectionType;
    }

    @Override
    protected String ownFields() {
        return "layout=" + layout + ", connectionType=" + connectionType;
    }

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) {
            return false;
        }
        Keyboard that = (Keyboard) o;
        return layout == that.layout && connectionType == that.connectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), layout, connectionType);
    }

    public static class Builder extends Equipment.Builder<Builder, Keyboard> {

        private KeyboardLayout layout;
        private ConnectionType connectionType;

        public Builder layout(KeyboardLayout layout) {
            this.layout = layout;
            return this;
        }

        public Builder connectionType(ConnectionType connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Keyboard build() {
            return new Keyboard(this);
        }
    }
}
