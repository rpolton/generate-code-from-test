package me.shaftesbury.codegenerator.text;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Class {
    private final String name;
    private final String body;

    public Class(final String name, final String body) {
        this.name = name;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public String getBody() {
        return body;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Class aClass = (Class) o;

        return new EqualsBuilder()
                .append(name, aClass.name)
                .append(body, aClass.body)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(body)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "Class{" +
                "name='" + name + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
