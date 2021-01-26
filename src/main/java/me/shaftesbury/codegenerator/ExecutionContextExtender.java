package me.shaftesbury.codegenerator;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;

public class ExecutionContextExtender implements IExecutionContextExtender {
    public ExecutionContextExtender(final IExecutionContext executionContext) {

    }

    public IExecutionContext addTestMethod(final IExecutionContext originalExecutionContext, final ITestMethod testMethod) {
//        final String className = testMethod.getClassName();
//        final String methodName = testMethod.getMethodName();
//        final IExecutionContext executionContext = /*((ExecutionContext) originalExecutionContext).toBuilder()
//                .withAdditionalClasses(List.of(new Class(className, List.of(methodName), "")))
//                .build()*/originalExecutionContext;
//        final RuntimeCompiler runtimeCompiler = executionContext.getRuntimeCompiler();
//        runtimeCompiler.addClass(className,testMethod.getMethod());
//        final boolean compiled = runtimeCompiler.compile();
//        return executionContext;
        return null;
    }

    public IExecutionContext with(final Traversable<ILogicalClass> classes) {
        return null;
    }
}
