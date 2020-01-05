package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface IClassNameFinder {
    Seq<String> findClassNamesPrecededByNew(Seq<IToken> tokens);
}
