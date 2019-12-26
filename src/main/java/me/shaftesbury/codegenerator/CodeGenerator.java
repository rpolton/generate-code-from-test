package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
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
        final Seq<Class> sourceCodeClasses = executionContext.getContext();
        final Option<Class> sourceCodeClass = sourceCodeClasses.find(cl -> testMethod.getMethod().contains(cl.getName()));
        final Option<Class> classWithMethod = sourceCodeClass.filter(cl -> testMethod.getMethod().contains(cl.getBody()));
        return classWithMethod.isDefined() ? List.empty() : generateCodeFor(testMethod.getClassName(), testMethod.getMethodName());
    }

    private Seq<Class> generateCodeFor(final String className, final String methodName) {
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
