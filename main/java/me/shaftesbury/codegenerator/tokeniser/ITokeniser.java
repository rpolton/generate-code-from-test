package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.ITestMethod;

public interface ITokeniser {
    Traversable<IToken> tokenise(String text);

    Traversable<IToken> tokenise(ITestMethod testMethod);
}
