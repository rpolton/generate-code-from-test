package me.shaftesbury.codegenerator.tokeniser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class FunctionName implements IToken {
    private static final FunctionName FUNCTIONNAME_AS_TOKEN = new FunctionName("");
    private final String name;

    private FunctionName(final String name) {
        this.name = name;
    }

    public static FunctionName of(final String name) {
        return new FunctionName(name);
    }

    public static FunctionName token() {
        return FUNCTIONNAME_AS_TOKEN;
    }

    @Override
    public String toString() {
        return "FunctionName{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final FunctionName that = (FunctionName) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
