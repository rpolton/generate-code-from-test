package me.shaftesbury.codegenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FunctionParameter implements IFunctionParameter {
    private final Type type;
    private final String name;

    private FunctionParameter(final Type type, final String name) {
        this.type = type;
        this.name = name;
    }

    public static FunctionParameter of(final Type token, final String name) {
        return new FunctionParameter(token, name);
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

        final FunctionParameter that = (FunctionParameter) o;

        return new EqualsBuilder().append(type, that.type).append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(name).toHashCode();
    }
}
