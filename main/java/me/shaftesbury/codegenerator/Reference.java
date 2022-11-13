package me.shaftesbury.codegenerator;

import me.shaftesbury.codegenerator.tokeniser.IToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Reference implements IToken {
    private final String name;

    private Reference(final String name) {
        this.name = name;
    }

    public static Reference of(final String name) {
        return new Reference(name);
    }

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

        final Reference reference = (Reference) o;

        return new EqualsBuilder().append(name, reference.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).toHashCode();
    }
}
