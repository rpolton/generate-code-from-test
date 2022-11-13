package me.shaftesbury.codegenerator;

import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface IFunctionNameFinder {
    Map<IClassName, ? extends Traversable<IFunctionName>> findFunctionsUsed(Traversable<IToken> tokens);
}
