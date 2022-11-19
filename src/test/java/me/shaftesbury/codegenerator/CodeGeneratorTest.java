package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Supplier;

import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static org.assertj.vavr.api.VavrAssertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CodeGeneratorTest {

    @Test
    void builder() {
        final CodeGenerator.Builder builder = CodeGenerator.builder();

        org.assertj.core.api.Assertions.assertThat(builder).isNotNull();
    }

    @Test
    void generateCodeReturnsNoneWhenNoCodeNeedsToBeGeneratedBecauseTheMethodAlreadyExists(
            @Mock final Supplier<IClassNameFinder> classNameFinderFactory, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserFactory, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderFactory, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext,
            @Mock final ILogicalClass logicalClass, @Mock final IClassName className, @Mock final IFunctionName doTheThing,
            @Mock final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory,
            @Mock final PartialCodeGenerator partialCodeGenerator, @Mock final PartialClass partialClass) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserFactory(tokeniserFactory)
                .withClassNameFinderFactory(classNameFinderFactory)
                .withFunctionNameFinderFactory(functionNameFinderFactory)
                .withPartialCodeGeneratorFactory(partialCodeGeneratorFactory)
                .build();
        final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);

        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
        when(tokeniserFactory.get()).thenReturn(tokeniser);
        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(logicalClass);
        when(executionContext.allFunctionsAreInTheContext(logicalClass)).thenReturn(true);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).isEmpty();
    }

    @Test
    void generateCodeReturnsCodeWhenTheClassAlreadyExistsButTheMethodDoesNot(
            @Mock final Supplier<IClassNameFinder> classNameFinderFactory, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserFactory, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderFactory, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext,
            @Mock final IClassName className, @Mock final IFunctionName doTheThing,
            @Mock final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory,
            @Mock final PartialCodeGenerator partialCodeGenerator, @Mock final PartialClass partialClass) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserFactory(tokeniserFactory)
                .withClassNameFinderFactory(classNameFinderFactory)
                .withFunctionNameFinderFactory(functionNameFinderFactory)
                .withPartialCodeGeneratorFactory(partialCodeGeneratorFactory)
                .build();
        final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(className)
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(doTheThing).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
        when(tokeniserFactory.get()).thenReturn(tokeniser);
        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(expectedClass);
        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }

    @Test
    void generateCodeReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist__AndOnlyTheDefaultConstructorIsUsedInTheTest(
            @Mock final Supplier<IClassNameFinder> classNameFinderFactory, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserFactory, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderFactory, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext,
            @Mock final IClassName className, @Mock final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory,
            @Mock final PartialCodeGenerator partialCodeGenerator, @Mock final PartialClass partialClass) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserFactory(tokeniserFactory)
                .withClassNameFinderFactory(classNameFinderFactory)
                .withFunctionNameFinderFactory(functionNameFinderFactory)
                .withPartialCodeGeneratorFactory(partialCodeGeneratorFactory)
                .build();
        final String testFunction = "@Test void test() {     new A(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(className)
                .withDefaultConstructor()
                .build();

        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
        when(tokeniserFactory.get()).thenReturn(tokeniser);
        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.empty()));
        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.empty()));
        when(partialCodeGenerator.generateCodeForClass(partialClass, List.empty())).thenReturn(expectedClass);
        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }

    @Test
    void generateCodeReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist(
            @Mock final Supplier<IClassNameFinder> classNameFinderFactory, @Mock final IClassNameFinder classNameFinder,
            @Mock final Supplier<ITokeniser> tokeniserFactory, @Mock final ITokeniser tokeniser,
            @Mock final Supplier<IFunctionNameFinder> functionNameFinderFactory, @Mock final IFunctionNameFinder functionNameFinder,
            @Mock final IExecutionContext executionContext,
            @Mock final IClassName className, @Mock final IFunctionName doTheThing,
            @Mock final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory,
            @Mock final PartialCodeGenerator partialCodeGenerator, @Mock final PartialClass partialClass) {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .withTokeniserFactory(tokeniserFactory)
                .withClassNameFinderFactory(classNameFinderFactory)
                .withFunctionNameFinderFactory(functionNameFinderFactory)
                .withPartialCodeGeneratorFactory(partialCodeGeneratorFactory)
                .build();
        final String testFunction = "@Test void test() { new A().doTheThing(); }";
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(className)
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(doTheThing).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
        when(tokeniserFactory.get()).thenReturn(tokeniser);
        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

        when(tokeniser.tokenise(testFunction)).thenReturn(tokens);
        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(expectedClass);
        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }

    @Nested
    class Getters {
        @Test
        void gettersReturnComponents(@Mock final IExecutionContext context,
                                     @Mock final Supplier<ITokeniser> tokeniserFactory,
                                     @Mock final Supplier<IClassNameFinder> classNameFinderFactory,
                                     @Mock final Supplier<IFunctionNameFinder> functionNameFinderFactory,
                                     @Mock final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory) {

            final ICodeGenerator codeGenerator = CodeGenerator.builder()
                    .withExecutionContext(context)
                    .withTokeniserFactory(tokeniserFactory)
                    .withClassNameFinderFactory(classNameFinderFactory)
                    .withFunctionNameFinderFactory(functionNameFinderFactory)
                    .withPartialCodeGeneratorFactory(partialCodeGeneratorFactory)
                    .build();

            org.assertj.core.api.Assertions.assertThat(codeGenerator)
                    .extracting(
                            ICodeGenerator::getExecutionContext,
                            ICodeGenerator::getTokeniserFactory,
                            ICodeGenerator::getClassNameFinderFactory,
                            ICodeGenerator::getFunctionNameFinderFactory,
                            ICodeGenerator::getPartialCodeGeneratorFactory)
                    .containsExactly(context, tokeniserFactory, classNameFinderFactory, functionNameFinderFactory, partialCodeGeneratorFactory);
        }
    }
}