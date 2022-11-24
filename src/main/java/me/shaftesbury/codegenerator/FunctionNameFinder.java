package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;

import static me.shaftesbury.codegenerator.tokeniser.Token.DOT;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;

public class FunctionNameFinder implements IFunctionNameFinder {
    @Override
    public Map<IClassName, ? extends Seq<IFunctionName>> findFunctionsUsed(final Seq<IToken> tokens) {
        return tokens.foldLeft(Tuple.of(List.empty(), HashMap.empty()), FunctionNameFinder::accumulate)._2;
//        return tokens.filter(t -> t instanceof IFunctionName).map(t -> (IFunctionName) t).groupBy(t -> ClassName.of("A"));
    }

    private static Tuple2<List<IToken>, HashMap<IClassName, List<IFunctionName>>> accumulate(final Tuple2<List<IToken>, HashMap<IClassName, List<IFunctionName>>> t, final IToken token) {
        final List<IToken> previous = t._1;
        final HashMap<IClassName, List<IFunctionName>> accumulator = t._2;
        if (previous.isEmpty())
            return Tuple.of(previous.prepend(token), accumulator);
        else {
            final int size = previous.size();
            if (size < 2) {
                return Tuple.of(previous.prepend(token), accumulator);
            } else {
                if (token instanceof IFunctionName &&
                        previous.get().equals(DOT) &&
                        previous.get(1) instanceof IClassName) {
                    final IClassName className = (IClassName) previous.get(1);
                    final IFunctionName functionName = (IFunctionName) token;
                    return Tuple.of(previous.prepend(token), accumulator.put(className, List.of(functionName)));
                } else if (size >= 4 && token instanceof IFunctionName &&
                        previous.get().equals(DOT) &&
                        previous.get(1).equals(ENDFUNCTIONPARAMETERS) &&
                        previous.get(2).equals(STARTFUNCTIONPARAMETERS) &&
                        previous.get(3) instanceof IClassName) {
                    final IClassName className = (IClassName) previous.get(3);
                    final IFunctionName functionName = (IFunctionName) token;
                    return Tuple.of(previous.prepend(token), accumulator.put(className, List.of(functionName)));
                } else {
                    return Tuple.of(previous.prepend(token), accumulator);
                }
            }
        }
//        return Tuple.of(previous.append(token), accumulator.put((IClassName) previous.get(), List.of((IFunctionName) token)));
    }
}
