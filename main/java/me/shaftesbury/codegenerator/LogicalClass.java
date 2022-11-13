package me.shaftesbury.codegenerator;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IConstructor;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static me.shaftesbury.codegenerator.model.Constructor.DEFAULT_CONSTRUCTOR;

public class LogicalClass implements ILogicalClass {
    private final IClassName name;
    private final Set<IConstructor> constructors;
    private final Set<ILogicalFunction> functions;
    private final Fields fields;

    private LogicalClass(final Builder builder) {
        name = builder.name;
        constructors = builder.constructors;
        functions = builder.functions;
        fields = builder.fields;
    }

    public static ILogicalClass of(final IClassName a) {
        return builder().withName(a).build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public IClassName getName() {
        return name;
    }

    @Override
    public Traversable<ILogicalFunction> getMethods() {
        return functions;
    }

    @Override
    public Traversable<IConstructor> getConstructors() {
        return constructors;
    }

    @Override
    public Fields getFields() {
        return fields;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final LogicalClass that = (LogicalClass) o;

        return new EqualsBuilder().append(name, that.name).append(constructors, that.constructors).append(functions, that.functions).append(fields, that.fields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(constructors).append(functions).append(fields).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("constructors", constructors)
                .append("functions", functions)
                .append("fields", fields)
                .toString();
    }

    public static class Builder {
        private IClassName name;
        private Set<IConstructor> constructors = HashSet.empty();
        private Set<ILogicalFunction> functions = HashSet.empty();
        private Fields fields = Fields.empty();

        public Builder withName(final IClassName className) {
            this.name = className;
            return this;
        }

        public Builder withDefaultConstructor() {
            constructors = constructors.add(DEFAULT_CONSTRUCTOR);
            return this;
        }

        public Builder withConstructor(final IConstructor constructor) {
            constructors = constructors.add(constructor);
            return this;
        }

        public Builder withFields(final Fields fields) {
            this.fields = fields;
            return this;
        }

        public Builder withMethod(final ILogicalFunction logicalFunction) {
            functions = functions.add(logicalFunction);
            return this;
        }

        public LogicalClass build() {
            return new LogicalClass(this);
        }

        public IClassName getName() {
            return name;
        }
    }
}
