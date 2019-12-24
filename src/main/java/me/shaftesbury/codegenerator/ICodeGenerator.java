package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;

public interface ICodeGenerator {
    IExecutionContext getExecutionContext();
    Seq<Class> generateCodeFor(final ITestMethod testMethod);
}
