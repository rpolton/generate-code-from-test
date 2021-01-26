package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public class ClassNameFinder implements IClassNameFinder {
    @Override
    public Seq<String> findClassNamesPrecededByNew(final Seq<IToken> tokens) {
        return null;
    }

    @Override
    public Traversable<IClassName> findConstructedClasses(final Traversable<IToken> tokens) {
        // tokens.filter(token -> token instanceof ClassName).map(token -> (ClassName) token);
        return null;
    }
}
