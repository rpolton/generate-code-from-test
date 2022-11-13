package me.shaftesbury.codegenerator;

import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public class FunctionNameFinder implements IFunctionNameFinder {
    @Override
    public Map<IClassName, ? extends Traversable<IFunctionName>> findFunctionsUsed(final Traversable<IToken> tokens) {
        return tokens.filter(t -> t instanceof IFunctionName).map(t -> (IFunctionName) t).groupBy(t -> ClassName.of("A"));
    }
}
