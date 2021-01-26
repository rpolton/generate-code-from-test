package me.shaftesbury.codegenerator;

import io.vavr.control.Option;
import me.shaftesbury.codegenerator.model.ITestMethod;

public interface ITestRunner {
    Option<Exception> execute(final ITestMethod testMethod);
}
