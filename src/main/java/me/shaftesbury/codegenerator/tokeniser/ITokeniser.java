package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.ITestMethod;

public interface ITokeniser {
    Seq<IToken> tokenise(String text);

    Seq<IToken> tokenise(ITestMethod testMethod);
}
