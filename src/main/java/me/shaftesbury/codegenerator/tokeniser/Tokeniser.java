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
        if (body.startsWith("@Test")) {
            return tokenise(body.replaceFirst("@Test[" + System.lineSeparator() + " ]+", ""), tokens.prepend(Token.TESTANNOTATION));
        }

        if (body.startsWith("void ")) {
            return tokenise(body.replaceFirst("void +", ""), tokens.prepend(Token.VOIDRETURNTYPE));
        }

        if (!tokens.isEmpty() && tokens.head().equals(Token.VOIDRETURNTYPE)) {
            final String functionName = body.substring(0, body.indexOf("("));
            return tokenise(body.replaceFirst("^[a-zA-Z0-9_]+ *", ""), tokens.prepend(FunctionName.of(functionName)));
        }

        if (!tokens.isEmpty() && tokens.head() instanceof FunctionName && body.startsWith("()")) {
            return tokenise(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.NOPARAMS));
        }

        if (body.startsWith("{")) {
            return tokenise(body.replaceFirst("^\\{ *", ""), tokens.prepend(Token.STARTFUNCTION));
        }

        if (body.startsWith("new ")) {
            return tokenise(body.replaceFirst("^new +", ""), tokens.prepend(Token.NEW));
        }

        if (!tokens.isEmpty() && tokens.head().equals(Token.NEW)) {
            final String className = body.substring(0, body.indexOf("("));
            return tokenise(body.replaceFirst("^[a-zA-Z0-9_]+ *", ""), tokens.prepend(ClassName.of(className)));
        }

        if (!tokens.isEmpty() && tokens.head() instanceof ClassName && body.startsWith("()")) {
            return tokenise(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.NOPARAMS));
        }
        if (body.startsWith(";")) {
            return tokenise(body.replaceFirst("^; *", ""), tokens.prepend(Token.SEMICOLON));
        }
        if (body.startsWith("}")) {
            return tokenise(body.replaceFirst("^\\} *", ""), tokens.prepend(Token.ENDFUNCTION));
        }

        return tokens.reverse();
    }
}
