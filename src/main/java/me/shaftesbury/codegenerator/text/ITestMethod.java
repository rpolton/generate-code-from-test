package me.shaftesbury.codegenerator.text;

import io.vavr.control.Option;

public interface ITestMethod {
    String getClassName();
    String getMethodName();
    String getMethod();
    Option<Exception> execute();
}
