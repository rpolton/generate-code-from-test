package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;

public class CodeGenerator implements ICodeGenerator {
    private final IExecutionContext executionContext;

    public CodeGenerator(final Builder builder) {
        executionContext = builder.executionContext;
    }

    public static Builder builder() {
        return new Builder();
    }

    public IExecutionContext getExecutionContext() {
        return executionContext;
    }

    public Seq<Class> generateCodeFor(final ITestMethod testMethod) {
        return List.empty();
    }

    public static class Builder {
        private IExecutionContext executionContext;

        public Builder withExecutionContext(final IExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        public CodeGenerator build() {
            return new CodeGenerator(this);
        }
    }
}
