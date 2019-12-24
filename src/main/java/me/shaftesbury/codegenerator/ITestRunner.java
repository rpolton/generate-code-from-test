package me.shaftesbury.codegenerator;

import io.vavr.control.Option;
import me.shaftesbury.codegenerator.text.ITestMethod;

public interface ITestRunner {
    Option<Throwable> execute(final ITestMethod testMethod);
}
