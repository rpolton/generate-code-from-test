package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IMethod;
import me.shaftesbury.codegenerator.model.ITestMethod;

public class Tokeniser implements ITokeniser {
    @Override
    public Traversable<IToken> tokenise(final String text) {
        return tokenise(text, List.empty());
    }

    @Override
    public Traversable<IToken> tokenise(final ITestMethod testMethod) {
        return tokenise(testMethod.getMethod());
    }

    private Traversable<IToken> tokenise(final IMethod method) {
        return null;
    }

    private Traversable<IToken> tokenise(final String body, final List<IToken> tokens) {
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
            return tokenise(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.STARTFUNCTIONPARAMETERS).prepend(Token.ENDFUNCTIONPARAMETERS));
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
            return tokenise(body.replaceFirst("\\( *\\) *", ""), tokens.prepend(Token.STARTFUNCTIONPARAMETERS).prepend(Token.ENDFUNCTIONPARAMETERS));
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
