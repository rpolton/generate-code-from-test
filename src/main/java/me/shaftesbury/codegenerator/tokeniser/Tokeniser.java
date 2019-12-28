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

        if (tokens.startsWith(List.of(Token.NEW))) {
            final String className = body.substring(0, body.indexOf("("));
            return tokenise(body.replaceFirst("^[a-zA-Z0-9_]+ *", ""), tokens.prepend(ClassName.of(className)));
        }

        if (tokens.startsWith(List.of(ClassName.token())) && body.startsWith("()")) {
            return tokenise(body.replaceFirst("()", ""), tokens.prepend(Token.NOPARAMS));
        }

        return tokens.reverse();
    }
}
