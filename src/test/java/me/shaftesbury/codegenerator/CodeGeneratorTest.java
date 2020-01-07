package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class CodeGeneratorTest {

    @Test
    void builder() {
        final CodeGenerator.Builder builder = CodeGenerator.builder();

        assertThat(builder).isNotNull();
    }

    @Nested
    class Getters {
        @Test
        void executionContext() {
            final ExecutionContext executionContext = mock(ExecutionContext.class);
            final Supplier<ITokeniser> tokeniserBuilder = mock(Supplier.class);
            final Supplier<IClassNameFinder> classNameFinder = mock(Supplier.class);
            final CodeGenerator codeGenerator = CodeGenerator.builder().withExecutionContext(executionContext).withTokeniserBuilder(tokeniserBuilder).withClassNameFinder(classNameFinder).build();

            assertAll(
                    () -> assertThat(codeGenerator.getClassNameFinder()).isSameAs(classNameFinder),
                    () -> assertThat(codeGenerator.getExecutionContext()).isSameAs(executionContext),
                    () -> assertThat(codeGenerator.getTokeniserBuilder()).isSameAs(tokeniserBuilder)
            );
        }
    }

    @Test
    void generateCodeForReturnsNoneWhenNoCodeNeedsToBeGeneratedBecauseTheMethodAlreadyExists() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final Supplier<ITokeniser> tokeniserBuilder = mock(Supplier.class);
        final Supplier<IClassNameFinder> classNameFinderBuilder = mock(Supplier.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinder(classNameFinderBuilder)
                .build();
        final Class sourceCodeClass = mock(Class.class);
        final ITokeniser tokeniser = mock(ITokeniser.class);
        final IClassNameFinder classNameFinder = mock(IClassNameFinder.class);
        final String testFunction = "@Test void test() { new A(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.STARTFUNCTION, Token.NEW, ClassName.of("A"), Token.NOPARAMS,
                Token.SEMICOLON, Token.ENDFUNCTION);

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(classNameFinder.findClassNamesPrecededByNew(tokens)).thenReturn(List.of("A()"));
        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getMethod()).thenReturn(testFunction);
        when(sourceCodeClass.getName()).thenReturn("A");
        when(sourceCodeClass.getPublicFunctions()).thenReturn(List.of("A()"));
        when(sourceCodeClass.getBody()).thenReturn("A() {}");

        final Traversable<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass, atLeastOnce()).getName();
        verify(sourceCodeClass).getPublicFunctions();
        verify(sourceCodeClass).getBody();
        verify(testMethod).getMethod();
        verify(tokeniserBuilder).get();
        verify(classNameFinderBuilder).get();
        verify(tokeniser).tokenise(testFunction);
        verify(classNameFinder).findClassNamesPrecededByNew(tokens);

        verifyNoMoreInteractions(executionContext, testMethod, tokeniserBuilder, classNameFinderBuilder, sourceCodeClass, tokeniser, classNameFinder);

        assertThat(code.isEmpty()).isTrue();
    }

    @Test
    void generateCodeForReturnsCodeWhenTheClassAlreadyExistsButTheMethodDoesNot() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final Supplier<ITokeniser> tokeniserBuilder = mock(Supplier.class);
        final Supplier<IClassNameFinder> classNameFinderBuilder = mock(Supplier.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinder(classNameFinderBuilder)
                .build();
        final Class sourceCodeClass = mock(Class.class);
        final ITokeniser tokeniser = mock(ITokeniser.class);
        final IClassNameFinder classNameFinder = mock(IClassNameFinder.class);
        final String testFunction = "@Test void test() { new A(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.STARTFUNCTION, Token.NEW, ClassName.of("A"), Token.NOPARAMS,
                Token.SEMICOLON, Token.ENDFUNCTION);
        final Class expectedClass = new Class("A", List.of("A() {}"), "");

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(classNameFinder.findClassNamesPrecededByNew(tokens)).thenReturn(List.of("A"));
        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getMethod()).thenReturn(testFunction);
        when(sourceCodeClass.getName()).thenReturn("A");
//        when(sourceCodeClass.getBody()).thenReturn("");

        final Traversable<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass, atLeastOnce()).getName();
        verify(sourceCodeClass).getPublicFunctions();
        verify(testMethod).getMethod();
        verify(tokeniserBuilder).get();
        verify(classNameFinderBuilder).get();
        verify(tokeniser).tokenise(testFunction);
        verify(classNameFinder).findClassNamesPrecededByNew(tokens);

        verifyNoMoreInteractions(executionContext, testMethod, tokeniserBuilder, classNameFinderBuilder, sourceCodeClass, tokeniser, classNameFinder);

        assertThat(code.isEmpty()).isFalse();
        assertThat(code.toJavaList()).containsExactlyInAnyOrder(expectedClass);
    }

    @Test
    void generateCodeForReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final Supplier<ITokeniser> tokeniserBuilder = mock(Supplier.class);
        final Supplier<IClassNameFinder> classNameFinderBuilder = mock(Supplier.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinder(classNameFinderBuilder)
                .build();
        final Class sourceCodeClass = mock(Class.class);
        final ITokeniser tokeniser = mock(ITokeniser.class);
        final IClassNameFinder classNameFinder = mock(IClassNameFinder.class);
        final String testFunction = "@Test void test() { new A(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.STARTFUNCTION, Token.NEW, ClassName.of("A"), Token.NOPARAMS,
                Token.SEMICOLON, Token.ENDFUNCTION);

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(classNameFinder.findClassNamesPrecededByNew(tokens)).thenReturn(List.of("A"));
        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getMethod()).thenReturn(testFunction);
        when(sourceCodeClass.getName()).thenReturn("");
        when(sourceCodeClass.getPublicFunctions()).thenReturn(List.empty());

        final Traversable<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass, atLeastOnce()).getName();
        verify(sourceCodeClass).getPublicFunctions();
        verify(testMethod).getMethod();
        verify(tokeniserBuilder).get();
        verify(classNameFinderBuilder).get();
        verify(tokeniser).tokenise(testFunction);
        verify(classNameFinder).findClassNamesPrecededByNew(tokens);

        verifyNoMoreInteractions(executionContext, testMethod, tokeniserBuilder, classNameFinderBuilder, sourceCodeClass, tokeniser, classNameFinder);

        assertThat(code.isEmpty()).isFalse();
    }

    @Nested
    class Builder {
        @Test
        void buildWithStatementBuilder() {
            final IExecutionContext context = mock(IExecutionContext.class);
            final Supplier<ITokeniser> tokeniserBuilder = mock(Supplier.class);
            final Supplier<IClassNameFinder> classNameFinder = mock(Supplier.class);

            final ICodeGenerator codeGenerator = CodeGenerator.builder()
                    .withExecutionContext(context)
                    .withTokeniserBuilder(tokeniserBuilder)
                    .withClassNameFinder(classNameFinder)
                    .build();

            assertThat(codeGenerator.getExecutionContext()).isEqualTo(context);
            assertThat(codeGenerator.getTokeniserBuilder()).isSameAs(tokeniserBuilder);
            assertThat(codeGenerator.getClassNameFinder()).isSameAs(classNameFinder);
        }
    }
}