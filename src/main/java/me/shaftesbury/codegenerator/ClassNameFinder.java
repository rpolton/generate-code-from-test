package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public class ClassNameFinder implements IClassNameFinder {
    @Override
    public Seq<String> findClassNamesPrecededByNew(final Seq<IToken> tokens) {
        return null;
    }
}
