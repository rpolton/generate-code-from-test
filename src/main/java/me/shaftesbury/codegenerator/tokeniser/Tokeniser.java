package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.Reference;
import me.shaftesbury.codegenerator.model.ITestMethod;

import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;

public class Tokeniser implements ITokeniser {
    @Override
    public Traversable<IToken> tokenise(final String text) {
        return tokenise(text, List.empty());
    }

    @Override
    public Traversable<IToken> tokenise(final ITestMethod testMethod) {
        return tokenise(testMethod.getMethod().getBody(), List.empty());
    }

    private Traversable<IToken> tokenise(final String body, final List<IToken> tokens) {
        if (body.isEmpty())
            return tokens.reverse();

        if (body.startsWith("public ")) {
            return tokenise(body.replaceFirst("public +", ""), tokens);
        }
        if (body.startsWith("class ")) {
            final Tuple2<String, List<IToken>> tuple2 = tokeniseClass(body.replaceFirst("class +", ""), tokens.prepend(CLASS));
            return tokenise(tuple2._1, tokens.prependAll(tuple2._2));
        }
        if (body.startsWith("@Test")) {
            return tokenise(body.replaceFirst("@Test[" + System.lineSeparator() + " ]+", ""), tokens.prepend(Token.TESTANNOTATION));
        }

        if (body.startsWith("void ")) {
            return tokenise(body.replaceFirst("void +", ""), tokens.prepend(Token.VOIDRETURNTYPE));
        }

        if (!tokens.isEmpty() && tokens.head().equals(Token.VOIDRETURNTYPE)) {
            final String functionName = body.substring(0, body.indexOf("("));
            return tokenise(body.replaceFirst("^[^(]+ *", ""), tokens.prepend(FunctionName.of(functionName)));
        }

        if (!tokens.isEmpty() && tokens.head() instanceof FunctionName && body.startsWith("()")) {
            return tokenise(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.STARTFUNCTIONPARAMETERS).prepend(Token.ENDFUNCTIONPARAMETERS));
        }

        if (body.startsWith("{")) {
            final Tuple2<String, List<IToken>> tuple2 = tokeniseFunction(body, List.empty());
            return tokenise(tuple2._1, tokens.prependAll(tuple2._2));
        }

        if (body.startsWith("new ")) {
            return tokenise(body.replaceFirst("^new +", ""), tokens.prepend(Token.NEW));
        }

        if (!tokens.isEmpty() && tokens.head().equals(Token.NEW)) {
            final String className = body.substring(0, body.indexOf("("));
            return tokenise(body.replaceFirst("^[^(]+ *", ""), tokens.prepend(ClassName.of(className)));
        }

        if (!tokens.isEmpty() && tokens.head() instanceof ClassName && body.startsWith("()")) {
            return tokenise(body.replaceFirst("\\( *\\) +", ""), tokens.prepend(Token.STARTFUNCTIONPARAMETERS).prepend(Token.ENDFUNCTIONPARAMETERS));
        }
        if (body.startsWith(";")) {
            return tokenise(body.replaceFirst("^; *", ""), tokens.prepend(Token.SEMICOLON));
        }
        if (body.startsWith("final ")) {
            return tokenise(body.replaceFirst("^final +", ""), tokens.prepend(Token.FINAL));
        }
        if (body.startsWith("int ")) {
            return tokenise(body.replaceFirst("^int +", ""), tokens.prepend(Token.INT));
        }
        if (body.startsWith("= ")) {
            return tokenise(body.replaceFirst("^= +", ""), tokens.prepend(Token.ASSIGNMENT));
        }
        if (body.matches("^[0-9]+(;| .*)")) {
            final int endIndex = body.indexOf(" ");
            final String value = body.substring(0, endIndex < 0 ? body.indexOf(";") : endIndex);
            return tokenise(body.replaceFirst("^[0-9]+ *", ""), tokens.prepend(Value.of(Integer.parseInt(value))));
        }
        if (body.matches("^[a-zA-Z0-9_]+ .*")) {
            final String refName = body.substring(0, body.indexOf(" "));
            return tokenise(body.replaceFirst("^[a-zA-Z0-9_]+ ", ""), tokens.prepend(Reference.of(refName)));
        }

        return tokens.reverse();
    }

    private Tuple2<String, List<IToken>> tokeniseClass(final String body, final List<IToken> tokens) {
        if (body.matches("^[a-zA-Z0-9_]+ .*")) {
            final String refName = body.substring(0, body.indexOf(" "));
            return tokeniseClass(body.replaceFirst("^[a-zA-Z0-9_]+ ", ""), tokens.prepend(ClassName.of(refName)));
        }
        if (body.startsWith("{")) {
            return tokeniseClass(body.replaceFirst("^\\{ *", ""), tokens.prepend(Token.STARTCLASS));
        }
        if (body.startsWith("}")) {
            return tokeniseClass(body.replaceFirst("^\\} *", ""), tokens.prepend(Token.ENDCLASS));
        }

        return new Tuple2<>(body, tokens);
    }

    private Tuple2<String, List<IToken>> tokeniseFunction(final String body, final List<IToken> tokens) {
        if (body.startsWith("{")) {
            return tokeniseFunction(body.replaceFirst("^\\{ *", ""), tokens.prepend(Token.STARTFUNCTION));
        }
        if (body.startsWith("}")) {
            return tokeniseFunction(body.replaceFirst("^\\} *", ""), tokens.prepend(Token.ENDFUNCTION));
        }

        if (body.startsWith("new ")) {
            return tokeniseFunction(body.replaceFirst("^new +", ""), tokens.prepend(Token.NEW));
        }

        if (!tokens.isEmpty() && tokens.head().equals(Token.NEW)) {
            final String className = body.substring(0, body.indexOf("("));
            return tokeniseFunction(body.replaceFirst("^[^(]+ *", ""), tokens.prepend(ClassName.of(className)));
        }

        if (!tokens.isEmpty() && tokens.head() instanceof ClassName && body.startsWith("()")) {
            return tokeniseFunction(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.STARTFUNCTIONPARAMETERS).prepend(Token.ENDFUNCTIONPARAMETERS));
        }
        if (body.startsWith(";")) {
            return tokeniseFunction(body.replaceFirst("^; *", ""), tokens.prepend(Token.SEMICOLON));
        }

        return new Tuple2<>(body, tokens);
    }
}
