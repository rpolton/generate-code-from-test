package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface IClassNameFinder {
    Seq<String> findClassNamesPrecededByNew(Seq<IToken> tokens);

    Traversable<IClassName> findConstructedClasses(Traversable<IToken> tokens);
}
