package me.shaftesbury.codegenerator;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public class ClassNameFinder implements IClassNameFinder {
    @Override
    public Traversable<IClassName> findConstructedClasses(final Traversable<IToken> tokens) {
        return tokens.filter(token -> token instanceof ClassName).map(token -> (ClassName) token);
    }
}
