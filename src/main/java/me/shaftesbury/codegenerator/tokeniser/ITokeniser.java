package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Seq;

public interface ITokeniser {
    Seq<IToken> tokenise(String text);
}
