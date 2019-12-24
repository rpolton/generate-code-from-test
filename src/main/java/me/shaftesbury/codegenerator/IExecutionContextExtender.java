package me.shaftesbury.codegenerator;

import me.shaftesbury.codegenerator.text.ITestMethod;

public interface IExecutionContextExtender {
    IExecutionContext addTestMethod(final IExecutionContext executionContext, final ITestMethod testMethod);
}
