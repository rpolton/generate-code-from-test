package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import static java.util.Objects.requireNonNull;

public class ExecutionContext implements IExecutionContext {
    private final Seq<Class> context;
    private final RuntimeCompiler runtimeCompiler;

    private ExecutionContext(final Builder builder) {
        context = builder.context.appendAll(builder.additionalClasses);
        runtimeCompiler = builder.compiler;
    }

    public Seq<Class> getContext() {
        return context;
    }

    public RuntimeCompiler getRuntimeCompiler() {
        return runtimeCompiler;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ExecutionContext that = (ExecutionContext) o;

        return new EqualsBuilder()
                .append(context, that.context)
                .append(runtimeCompiler, that.runtimeCompiler)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(context)
                .append(runtimeCompiler)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "ExecutionContext{" +
                "context=" + context +
                ", runtimeCompiler=" + runtimeCompiler +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder().withCompiler(runtimeCompiler).withContext(context);
    }

    public static class Builder {
        private Seq<Class> context;
        private Seq<Class> additionalClasses = List.empty();
        private RuntimeCompiler compiler;

        public Builder withContext(final Seq<Class> context) {
            this.context = context;
            return this;
        }

        public Builder withCompiler(final RuntimeCompiler compiler) {
            this.compiler = compiler;
            return this;
        }

        public Builder withAdditionalClasses(final Seq<Class> classes) {
            this.additionalClasses = classes;
            return this;
        }

        public ExecutionContext build() {
            requireNonNull(compiler, "runtimeCompiler must not be null");
            requireNonNull(context, "context must not be null");
            requireNonNull(additionalClasses, "additionalClasses must not be null");

            context.forEach(cl -> compiler.addClass(cl.getName(), String.join(" ", cl.getBody())));
            additionalClasses.forEach(cl -> compiler.addClass(cl.getName(), String.join(" ", cl.getPublicFunctions())));
            if (!context.isEmpty())
                compiler.compile();

            return new ExecutionContext(this);
        }
    }
}