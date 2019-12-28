package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.List;
import io.vavr.collection.Seq;

public class Tokeniser implements ITokeniser {
    public Seq<IToken> tokenise(final String body) {
        return tokenise(body, List.empty());
    }

    private Seq<IToken> tokenise(final String body, final Seq<IToken> tokens) {
        if (body.isEmpty())
            return tokens.reverse();

        if (body.startsWith("new ")) {
            return tokenise(body.replaceFirst("^new +", ""), tokens.prepend(Token.NEW));
        }

        return tokens.reverse();
    }
}
