package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;

import java.util.function.Supplier;

public interface ICodeGenerator {
    IExecutionContext getExecutionContext();

    Seq<ILogicalClass> generateCodeSatisfying(final String testClass);

    Supplier<ITokeniser> getTokeniserFactory();

    Supplier<IClassNameFinder> getClassNameFinderFactory();

    Supplier<IFunctionNameFinder> getFunctionNameFinderFactory();

    Supplier<PartialCodeGenerator> getPartialCodeGeneratorFactory();
}
