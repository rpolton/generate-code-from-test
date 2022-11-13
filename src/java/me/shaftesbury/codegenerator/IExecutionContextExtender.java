package me.shaftesbury.codegenerator;

import me.shaftesbury.codegenerator.model.ITestMethod;

public interface IExecutionContextExtender {
    IExecutionContext addTestMethod(final IExecutionContext executionContext, final ITestMethod testMethod);
}
