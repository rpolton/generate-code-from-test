package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Objects.isNull;

public class ExecutionContext implements IExecutionContext {
    private final Traversable<ILogicalClass> classes;
    private final RuntimeCompiler runtimeCompiler;

    private ExecutionContext(final Builder builder) {
        classes = builder.context/*.appendAll(builder.additionalClasses)*/;
        runtimeCompiler = builder.runtimeCompiler;
    }

    public Traversable<ILogicalClass> getClasses() {
        return classes;
    }

    public RuntimeCompiler getRuntimeCompiler() {
        return runtimeCompiler;
    }

    @Override
    public boolean allFunctionsAreInTheContext(final IClassName iClassName, final Traversable<IFunctionName> iFunctionNames) {
        return classIsInTheContext(iClassName) && functionsAreInTheClass(classes.filter(cls -> cls.getName().equals(iClassName)), iFunctionNames);
    }

    @Override
    public boolean allFunctionsAreInTheContext(final ILogicalClass cls) {
        return false;
    }

    private boolean classIsInTheContext(final IClassName iClassName) {
        return classes.map(ILogicalClass::getName).contains(iClassName);
    }

    private boolean functionsAreInTheClass(final Traversable<ILogicalClass> classes, final Traversable<IFunctionName> functionNames) {
        return classes.filter(cls -> cls.getMethods().map(ILogicalFunction::getName).containsAll(functionNames)).containsAll(classes);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ExecutionContext that = (ExecutionContext) o;

        return new EqualsBuilder()
                .append(classes, that.classes)
                .append(runtimeCompiler, that.runtimeCompiler)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(classes)
                .append(runtimeCompiler)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "context=" + classes +
                ", runtimeCompiler=" + runtimeCompiler +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Traversable<ILogicalClass> context;
        private Traversable<ILogicalClass> additionalClasses = List.empty();
        private RuntimeCompiler runtimeCompiler;

        public static Builder from(final IExecutionContext executionContext) {
            return builder().withCompiler(executionContext.getRuntimeCompiler()).withContext(executionContext.getClasses());
        }

        public Builder withContext(final Traversable<ILogicalClass> context) {
            this.context = context;
            return this;
        }

        public Builder withCompiler(final RuntimeCompiler runtimeCompiler) {
            this.runtimeCompiler = runtimeCompiler;
            return this;
        }

        public Builder withAdditionalClasses(final Traversable<ILogicalClass> classes) {
            this.additionalClasses = classes;
            return this;
        }

        public ExecutionContext build() {
//            requireNonNull(runtimeCompiler, "runtimeCompiler must not be null");
//            requireNonNull(additionalClasses, "additionalClasses must not be null");

            context = isNull(context) ? List.empty() : context;
//            context.forEach(cl -> runtimeCompiler.addClass(cl.getName(), String.join(" ", cl.getBody())));
//            additionalClasses.forEach(cl -> runtimeCompiler.addClass(cl.getName(), String.join(" ", cl.getPublicFunctions())));
//            if (!context.isEmpty())
//                runtimeCompiler.compile();

            return new ExecutionContext(this);
        }
    }

    public static class Factory {
        public IExecutionContext create(final ILogicalClass theClass) {
            return getBuilder().withContext(List.of(theClass)).build();
        }

        public IExecutionContext create() {
            return getBuilder().build();
        }

        private Builder getBuilder() {
            return ExecutionContext.builder().withCompiler(new RuntimeCompiler());
        }
    }
}