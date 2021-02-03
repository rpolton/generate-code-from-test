package me.shaftesbury.codegenerator;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Fields {
    private final Set<Field> fields;

    public Fields(final Builder builder) {
        fields = builder.fields;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Fields empty() {
        return new Fields(builder());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fields", fields)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Fields fields1 = (Fields) o;

        return new EqualsBuilder().append(fields, fields1.fields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(fields).toHashCode();
    }

    public static class Builder {
        private Set<Field> fields = HashSet.empty();

        public Builder withField(final Field field) {
            fields = fields.add(field);
            return this;
        }

        public Fields build() {
            return new Fields(this);
        }
    }
}
