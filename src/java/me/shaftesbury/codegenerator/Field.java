package me.shaftesbury.codegenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Field implements IField {
    private final Type type;
    private final String name;

    private Field(final Builder builder) {
        type = builder.type;
        name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Access getAccess() {
        return Access.PRIVATE_FINAL;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("type", type)
                .append("name", name)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Field field = (Field) o;

        return new EqualsBuilder().append(type, field.type).append(name, field.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(name).toHashCode();
    }

    public static class Builder {

        private Type type;
        private String name;

        public Builder withAccess(final Access access) {
            return this;
        }

        public Builder withType(final Type type) {
            this.type = type;
            return this;
        }

        public Builder withName(final String name) {
            this.name = name;
            return this;
        }

        public Field build() {
            return new Field(this);
        }
    }
}
