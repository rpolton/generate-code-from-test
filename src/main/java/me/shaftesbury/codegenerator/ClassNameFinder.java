package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;

public class ClassNameFinder implements IClassNameFinder {
    @Override
    public Seq<IClassName> findConstructedClasses(final Seq<IToken> tokens) {
        return tokens.foldLeft(
                        Tuple.of(List.<IToken>empty(), List.<IToken>empty()),
                        (prevTokenAndAcc, token) -> prevTokenAndAcc._1.nonEmpty() && Token.NEW.equals(prevTokenAndAcc._1.head()) && token instanceof ClassName
                                ? Tuple.of(List.of(token), prevTokenAndAcc._2.prepend(token))
                                : Tuple.of(List.of(token), prevTokenAndAcc._2))
                ._2
                .map(token -> (IClassName) token)
                .reverse();
//        return tokens.filter(token -> token instanceof ClassName).map(token -> (ClassName) token);
    }
}
