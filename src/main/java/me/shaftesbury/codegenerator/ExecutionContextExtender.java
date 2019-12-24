package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;

public class ExecutionContextExtender implements IExecutionContextExtender {
    public IExecutionContext addTestMethod(final IExecutionContext originalExecutionContext, final ITestMethod testMethod) {
        final String className = testMethod.getClassName();
        final String methodName = testMethod.getMethodName();
        final ExecutionContext executionContext = ((ExecutionContext) originalExecutionContext).toBuilder()
                .withAdditionalClasses(List.of(new Class(className, methodName)))
                .build();
        final RuntimeCompiler runtimeCompiler = executionContext.getRuntimeCompiler();
        runtimeCompiler.addClass(className,testMethod.getMethod());
        final boolean compiled = runtimeCompiler.compile();
        return executionContext;
    }
}
