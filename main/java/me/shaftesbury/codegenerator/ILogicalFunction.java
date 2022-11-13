package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

public interface ILogicalFunction {
    IFunctionName getName();

    Traversable<IFunctionParameter> getParameters();

    ReturnType getReturnType();

    List<IToken> getBody();
}
