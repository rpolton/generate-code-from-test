package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface IClassNameFinder {
    /**
     * find the names of the classes whose constructors are called in the tokens
     *
     * @param tokens
     * @return
     */
    Seq<IClassName> findConstructedClasses(Seq<IToken> tokens);
}
