package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Seq;
import org.junit.jupiter.api.Test;

import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.NOPARAMS;
import static me.shaftesbury.codegenerator.tokeniser.Token.SEMICOLON;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.TESTANNOTATION;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;
import static org.assertj.core.api.Assertions.assertThat;

public class TokeniserTest {
    @Test
    void newTokenAndClassName() {
        final String testMethodBody = "new A()";

        final Tokeniser tokeniser = new Tokeniser();

        final Seq<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(NEW, ClassName.of("A"), NOPARAMS);
    }

    @Test
    void tokens() {
        final String testMethodBody = "@Test void test() { new A(); }";

        final Tokeniser tokeniser = new Tokeniser();

        final Seq<IToken> tokens = tokeniser.tokenise(testMethodBody);

        assertThat(tokens.toJavaList()).containsExactly(TESTANNOTATION, VOIDRETURNTYPE, FunctionName.of("test"), STARTFUNCTION, NEW, ClassName.of("A"), NOPARAMS, SEMICOLON, ENDFUNCTION);
    }
}
