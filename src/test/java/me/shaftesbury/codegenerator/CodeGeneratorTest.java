package me.shaftesbury.codegenerator;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MockitoSettings
class CodeGeneratorTest {

    @Test
    void builder() {
        final CodeGenerator.Builder builder = CodeGenerator.builder();

        assertThat(builder).isNotNull();
    }

    @Test
    void generateCodeReturnsNoneWhenNoCodeNeedsToBeGeneratedBecauseTheMethodAlreadyExists(
            @Mock final Supplier<IClassNameFinder> classNameFinderBuilder, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserBuilder, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderBuilder, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext, @Mock final ITestMethod testMethod,
            @Mock final ILogicalClass sourceCodeClass, @Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinderBuilder(classNameFinderBuilder)
                .withFunctionNameFinderBuilder(functionNameFinderBuilder)
                .build();
        // final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.NOPARAMS, Token.STARTFUNCTION, Token.NEW, className, Token.NOPARAMS,
                Token.DOT, doTheThing, Token.NOPARAMS, Token.SEMICOLON, Token.ENDFUNCTION);

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(functionNameFinderBuilder.get()).thenReturn(functionNameFinder);
        when(tokeniser.tokenise(testMethod)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(executionContext.getClasses()).thenReturn(List.of(sourceCodeClass));
        when(executionContext.allFunctionsAreInTheContext(className, List.of(doTheThing))).thenReturn(true);
        when(functionNameFinder.findFunctionsUsedInTest(any(Traversable.class), any(Traversable.class))).thenReturn(HashMap.of(className, List.of(doTheThing)));

        final Traversable<ILogicalClass> code = codeGenerator.generateCode(testMethod);

        assertThat(code.isEmpty()).isTrue();
    }

    @Test
    void generateCodeReturnsCodeWhenTheClassAlreadyExistsButTheMethodDoesNot(
            @Mock final Supplier<IClassNameFinder> classNameFinderBuilder, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserBuilder, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderBuilder, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext, @Mock final ITestMethod testMethod,
            @Mock final ILogicalClass sourceCodeClass, @Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinderBuilder(classNameFinderBuilder)
                .withFunctionNameFinderBuilder(functionNameFinderBuilder)
                .build();
        // final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.NOPARAMS, Token.STARTFUNCTION, Token.NEW, className, Token.NOPARAMS,
                Token.DOT, doTheThing, Token.NOPARAMS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(className)
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(doTheThing).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(functionNameFinderBuilder.get()).thenReturn(functionNameFinder);
        when(tokeniser.tokenise(testMethod)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(executionContext.getClasses()).thenReturn(List.of(sourceCodeClass));
        when(executionContext.allFunctionsAreInTheContext(className, List.of(doTheThing))).thenReturn(false);
        when(functionNameFinder.findFunctionsUsedInTest(any(Traversable.class), any(Traversable.class))).thenReturn(HashMap.of(className, List.of(doTheThing)));

        final Traversable<ILogicalClass> code = codeGenerator.generateCode(testMethod);

        assertThat(code.isEmpty()).isFalse();
        assertThat(code).contains(expectedClass);
    }

    @Test
    void generateCodeReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist(
            @Mock final Supplier<IClassNameFinder> classNameFinderBuilder, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserBuilder, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderBuilder, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext, @Mock final ITestMethod testMethod,
            @Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserBuilder(tokeniserBuilder)
                .withClassNameFinderBuilder(classNameFinderBuilder)
                .withFunctionNameFinderBuilder(functionNameFinderBuilder)
                .build();
        // final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE,
                FunctionName.of("test"), Token.NOPARAMS, Token.STARTFUNCTION, Token.NEW, className, Token.NOPARAMS,
                Token.DOT, doTheThing, Token.NOPARAMS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(className)
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(doTheThing).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

        when(tokeniserBuilder.get()).thenReturn(tokeniser);
        when(classNameFinderBuilder.get()).thenReturn(classNameFinder);
        when(functionNameFinderBuilder.get()).thenReturn(functionNameFinder);
        when(tokeniser.tokenise(testMethod)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(functionNameFinder.findFunctionsUsedInTest(any(Traversable.class), any(Traversable.class))).thenReturn(HashMap.of(className, List.of(doTheThing)));
        when(executionContext.getClasses()).thenReturn(List.empty());

        final Traversable<ILogicalClass> code = codeGenerator.generateCode(testMethod);

        assertThat(code.isEmpty()).isFalse();
        assertThat(code).contains(expectedClass);
    }

    @Nested
    class Getters {
        @Test
        void executionContext(@Mock final ExecutionContext executionContext, @Mock final Supplier<ITokeniser> tokeniserBuilder, @Mock final Supplier<IClassNameFinder> classNameFinder) {
            final CodeGenerator codeGenerator = CodeGenerator.builder().withExecutionContext(executionContext).withTokeniserBuilder(tokeniserBuilder).withClassNameFinderBuilder(classNameFinder).build();

            assertThat(codeGenerator)
                    .extracting(CodeGenerator::getClassNameFinderBuilder, CodeGenerator::getExecutionContext, CodeGenerator::getTokeniserBuilder)
                    .containsExactly(classNameFinder, executionContext, tokeniserBuilder);
        }
    }

    @Nested
    class Builder {
        @Test
        void buildWithStatementBuilder(@Mock final IExecutionContext context, @Mock final Supplier<ITokeniser> tokeniserBuilder, @Mock final Supplier<IClassNameFinder> classNameFinder) {

            final ICodeGenerator codeGenerator = CodeGenerator.builder()
                    .withExecutionContext(context)
                    .withTokeniserBuilder(tokeniserBuilder)
                    .withClassNameFinderBuilder(classNameFinder)
                    .build();

            assertThat(codeGenerator.getExecutionContext()).isEqualTo(context);
            assertThat(codeGenerator.getTokeniserBuilder()).isSameAs(tokeniserBuilder);
            assertThat(codeGenerator.getClassNameFinderBuilder()).isSameAs(classNameFinder);
        }
    }
}