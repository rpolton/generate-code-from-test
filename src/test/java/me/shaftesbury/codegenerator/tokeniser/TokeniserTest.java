package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.Reference;
import org.junit.jupiter.api.Test;

import static me.shaftesbury.codegenerator.tokeniser.Token.ASSIGNMENT;
import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
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

public class TokeniserTest {
    @Test
    void newTokenAndClassName() {
        final String testMethodBody = "new A()";

        final ITokeniser tokeniser = new Tokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(NEW, ClassName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);
    }

    @Test
    void simplestClass() {
        final String testMethodBody = "public class A { }";

        final ITokeniser tokeniser = new Tokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(CLASS, ClassName.of("A"), STARTCLASS, ENDCLASS);
    }

    @Test
    void assignmentFunction() {
        final String testMethodBody = "final int a = 10;";

        final ITokeniser tokeniser = new Tokeniser();

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(FINAL, INT, Reference.of("a"), ASSIGNMENT, Value.of(10), SEMICOLON);
    }

    @Test
    void testFunction() {
        final String testMethod = "@Test void test() {     new A(); }";
        final Tokeniser tokeniser = new Tokeniser();
        final Traversable<IToken> tokens = tokeniser.tokenise(testMethod);
        assertThat(tokens.toJavaList()).containsExactly(TESTANNOTATION, VOIDRETURNTYPE, FunctionName.of("test"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, NEW, ClassName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON, ENDFUNCTION);
    }
}
