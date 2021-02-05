package me.shaftesbury.codegenerator;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;

import java.util.function.Supplier;

public interface ICodeGenerator {
    IExecutionContext getExecutionContext();

    Traversable<ILogicalClass> generateCode(final ITestMethod testMethod);

    Supplier<ITokeniser> getTokeniserFactory();

    Supplier<IClassNameFinder> getClassNameFinderFactory();
}
