package me.shaftesbury.codegenerator;

import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public class FunctionNameFinder implements IFunctionNameFinder {
    @Override
    public Map<IClassName, ? extends Traversable<IFunctionName>> findFunctionsUsedInTest(final Traversable<IClassName> classNamesUsedInTest, final Traversable<IToken> tokens) {
        final IClassName c = classNamesUsedInTest.get();
        final Map<IClassName, ? extends Traversable<IFunctionName>> m = tokens.filter(t -> t instanceof IFunctionName).map(t -> (IFunctionName) t).groupBy(t -> c);
        return m;
    }
}
