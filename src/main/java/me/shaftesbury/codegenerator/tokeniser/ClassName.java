package me.shaftesbury.codegenerator.tokeniser;

import me.shaftesbury.codegenerator.IClassName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClassName implements IClassName {
    //    private static final ClassName CLASSNAME_AS_TOKEN = new ClassName("");
    private final String name;

    private ClassName(final String name) {
        this.name = name;
    }

    public static ClassName of(final String name) {
        return new ClassName(name);
    }

//    public static ClassName token() {
//        return CLASSNAME_AS_TOKEN;
//    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ClassName className = (ClassName) o;

        return new EqualsBuilder().append(name, className.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }

    @Override
    public String asCode() {
        return name;
    }
}
