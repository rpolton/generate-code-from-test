package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassNameFinderTest {

    @Test
    void findConstructedClassesReturnsEmptyWhenNoConstructorsCalledInTokens() {
        final ClassNameFinder classNameFinder = new ClassNameFinder();
        final Traversable<IToken> tokens = List.empty();
        final Traversable<IClassName> constructedClasses = classNameFinder.findConstructedClasses(tokens);
        assertThat(constructedClasses).isEmpty();
    }

    @Test
    void findConstructedClassesReturnsNamesOfClassesConstructedInTokens() {
        final ClassNameFinder classNameFinder = new ClassNameFinder();
        final Traversable<IToken> tokens = List.of(Token.NEW, ClassName.of("A"), Token.STARTFUNCTIONPARAMETERS, Token.ENDFUNCTIONPARAMETERS,
                Token.SEMICOLON, Token.NEW, ClassName.of("B"), Token.STARTFUNCTIONPARAMETERS, Token.ENDFUNCTIONPARAMETERS,
                Token.SEMICOLON, Token.NEW, ClassName.of("C"), Token.STARTFUNCTIONPARAMETERS, Token.ENDFUNCTIONPARAMETERS);
        final Traversable<IClassName> constructedClasses = classNameFinder.findConstructedClasses(tokens);
        assertThat(constructedClasses).containsExactly(ClassName.of("A"), ClassName.of("B"), ClassName.of("C"));
    }
}