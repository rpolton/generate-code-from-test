package me.shaftesbury.codegenerator.tokeniser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ClassName implements IToken {
    private static final ClassName CLASSNAME_AS_TOKEN = new ClassName("");
    private final String name;

    private ClassName(final String name) {
        this.name = name;
    }

    public static ClassName of(final String name) {
        return new ClassName(name);
    }

    public static ClassName token() {
        return CLASSNAME_AS_TOKEN;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "ClassName{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ClassName className = (ClassName) o;

        if (this == CLASSNAME_AS_TOKEN || className == CLASSNAME_AS_TOKEN)
            return true;
        return new EqualsBuilder()
                .append(name, className.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
}
