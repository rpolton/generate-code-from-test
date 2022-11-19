package me.shaftesbury.codegenerator.tokeniser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;


@ExtendWith(MockitoExtension.class)
class TokeniserTest {
    @Test
    void tokenise(@Mock final CompilationUnit cu, @Mock final TokeniserImpl tokeniserImpl) {
        final String text = "public class Test {}";
        // TODO this needs to assert that the 'text' is being passed to the javaParser object
        doNothing().when(tokeniserImpl).visit(any(com.github.javaparser.ast.CompilationUnit.class), any(java.util.List.class));
        final Tokeniser tokeniser = new Tokeniser(x -> cu, () -> tokeniserImpl);

        tokeniser.tokenise(text);
    }

    @Test
    void testTokenise() {
    }
}