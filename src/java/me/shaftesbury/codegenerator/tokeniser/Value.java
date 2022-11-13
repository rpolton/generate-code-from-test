package me.shaftesbury.codegenerator.tokeniser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Value implements IToken {
    private final int value;

    public Value(final int i) {
        this.value = i;
    }

    public static IToken of(final int i) {
        return new Value(i);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Value value1 = (Value) o;

        return new EqualsBuilder().append(value, value1.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    public int getValue() {
        return value;
    }
}
