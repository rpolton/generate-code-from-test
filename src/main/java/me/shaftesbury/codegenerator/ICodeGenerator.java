package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;

import java.util.function.Supplier;

public interface ICodeGenerator {
    IExecutionContext getExecutionContext();

    Seq<Class> generateCodeFor(final ITestMethod testMethod);

    Supplier<ITokeniser> getTokeniserBuilder();

    Supplier<IClassNameFinder> getClassNameFinder();
}
