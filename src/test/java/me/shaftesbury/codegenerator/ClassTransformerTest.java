package me.shaftesbury.codegenerator;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import me.shaftesbury.codegenerator.model.Constructor;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@MockitoSettings
class ClassTransformerTest {

    @Test
    void transformThrowsWhenSuppliedNull() {
        final ClassTransformer classTransformer = new ClassTransformer();
        assertThatNullPointerException()
                .isThrownBy(() -> classTransformer.transform(null))
                .withMessage("tokens must not be null");
    }

    @Test
    void transformSimplestClass() {
        final ILogicalClass expectedClass = LogicalClass.of(ClassName.of("A"));
        // "public class A { }"
        final ILogicalClass logicalClass = new ClassTransformer().transform(List.of(Token.PUBLIC, Token.CLASS, ClassName.of("A"), Token.STARTCLASS, Token.ENDCLASS));
        assertThat(logicalClass).isEqualTo(expectedClass);
    }

    @Test
    void transformClassWithConstructor() {
//        final String docString = "public class A { public A(int a) {} }";
        final List<IToken> tokens = List.of(Token.PUBLIC, Token.CLASS, ClassName.of("A"), Token.STARTCLASS,
                Token.PUBLIC, FunctionName.of("A"), Token.STARTFUNCTIONPARAMETERS, Token.INT, Reference.of("a"), Token.ENDFUNCTIONPARAMETERS,
                Token.STARTFUNCTION, Token.ENDFUNCTION, Token.ENDCLASS);
        final ILogicalClass expectedClass = LogicalClass.builder().withName(ClassName.of("A"))
                .withConstructor(Constructor.builder().withParameter(Type.INT, "a").withBody(List.of(Token.EMPTY)).build())
                .build();
        final ILogicalClass logicalClass = new ClassTransformer().transform(tokens);
        assertThat(logicalClass).isEqualTo(expectedClass);
    }

    @Test
    void transformSimpleClassWhichAssignsParametersToFields() {
        final ILogicalClass expectedClass = LogicalClass.builder().withName(ClassName.of("A"))
                .withFields(HashSet.of(Field.withType(Type.INT).withName("a")))
                .withConstructor(Constructor.builder().withParameter(Type.INT, "a").withBody(List.of(Token.THIS, Reference.of("a"), Token.ASSIGNMENT, Reference.of("a"))).build())
                .build();
        // "public class A { private final int a; public A(int a) {this.a=a;} }"
        final List<IToken> tokens = List.of(Token.PUBLIC, Token.CLASS, ClassName.of("A"), Token.STARTCLASS,
                Token.PRIVATE, Token.FINAL, Token.INT, Reference.of("a"), Token.SEMICOLON,
                Token.PUBLIC, FunctionName.of("A"), Token.STARTFUNCTIONPARAMETERS, Token.INT, Reference.of("a"), Token.ENDFUNCTIONPARAMETERS,
                Token.STARTFUNCTION, Token.THIS, Reference.of("a"), Token.ASSIGNMENT, Reference.of("a"), Token.SEMICOLON,
                Token.ENDFUNCTION, Token.ENDCLASS);
        final ILogicalClass logicalClass = new ClassTransformer().transform(tokens);
        assertThat(logicalClass).isEqualTo(expectedClass);
    }
}