package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

import java.util.function.Function;

public class FunctionNameFinder implements IFunctionNameFinder {
    @Override
    public Map<IClassName, Traversable<IFunctionName>> findFunctionsUsedInTest(final Traversable<IClassName> classNamesUsedInTest, final Traversable<IToken> tokens) {
//        final Multimap<IClassName, IFunctionName> functionsInEachClass = classNamesUsedInTest
//                .zip(classNamesUsedInTest.map(findFunctionsUsedInTest(tokens)))
//                .toMap(t1->t1._1,t2->t2._2);
//        classNamesUsedInTest.map(cls -> functionsInEachClass.map(m -> m.get(cls))
//                generateCodeFor()
        return null;
    }

    private Function<IClassName, List<IFunctionName>> findFunctionsUsedInTest(final Traversable<IToken> tokens) {
        return null;
    }
}
