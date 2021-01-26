package me.shaftesbury.codegenerator.model;

import me.shaftesbury.codegenerator.IClassName;
import me.shaftesbury.codegenerator.IExecutionContext;
import me.shaftesbury.codegenerator.Result;
import me.shaftesbury.codegenerator.tokeniser.IFunction;

public interface ITestMethod extends IFunction {
    IClassName getClassName();

    IMethodName getMethodName();

    IMethod getMethod();

    Result execute(final IExecutionContext newContext);
}
