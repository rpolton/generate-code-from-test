package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Traversable;

public interface ITokeniser {
    Traversable<IToken> tokenise(IFunction text);

    Traversable<IToken> tokenise(String text);
}
