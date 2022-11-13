package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.Reference;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static me.shaftesbury.codegenerator.tokeniser.Token.ASSIGNMENT;
import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.DOT;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.FINAL;
import static me.shaftesbury.codegenerator.tokeniser.Token.INT;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.SEMICOLON;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.TESTANNOTATION;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled("Use JavaParser instead of my hand-rolled parser")
class ManualTokeniserTest {
    @Test
    void newTokenAndClassName() {
        final String testMethodBody = "new A()";

        final ITokeniser tokeniser = new ManualTokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(NEW, ClassName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);
    }

    @Test
    void simplestClass() {
        final String testMethodBody = "public class A { }";

        final ITokeniser tokeniser = new ManualTokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(CLASS, ClassName.of("A"), STARTCLASS, ENDCLASS);
    }

    @Test
    void assignmentFunction() {
        final String testMethodBody = "final int a = 10;";

        final ITokeniser tokeniser = new ManualTokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(FINAL, INT, Reference.of("a"), ASSIGNMENT, Value.of(10), SEMICOLON);
    }

    @Test
    void testFunctionContainingDefaultConstructor() {
        final String testFunction = "@Test void testFunction() { new A(); }";
        final java.util.List<IToken> expectedTokens = List.of(TESTANNOTATION, VOIDRETURNTYPE, FunctionName.of("testFunction"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, NEW, ClassName.of("A"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON, ENDFUNCTION)
                .toJavaList();
        final ManualTokeniser tokeniser = new ManualTokeniser();

        final Traversable<IToken> actualTokens = tokeniser.tokenise(testFunction);

        assertThat(actualTokens.toJavaList()).containsExactlyElementsOf(expectedTokens);
    }

    @Test
    void testFunctionContainingFunctionCall() {
        final String testFunction = "@Test void testFunction() { new A().doTheThing(); }";
        final java.util.List<IToken> expectedTokens = List.of(TESTANNOTATION, VOIDRETURNTYPE, FunctionName.of("testFunction"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, NEW, ClassName.of("A"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, DOT, FunctionName.of("doTheThing"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON, ENDFUNCTION)
                .toJavaList();
        final ManualTokeniser tokeniser = new ManualTokeniser();

        final Traversable<IToken> actualTokens = tokeniser.tokenise(testFunction);

        assertThat(actualTokens.toJavaList()).containsExactlyElementsOf(expectedTokens);
    }
}
