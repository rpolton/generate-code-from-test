package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.CompilationUnit;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.Token;
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;
import me.shaftesbury.codegenerator.tokeniser.TokeniserImpl;
import org.junit.jupiter.api.Test;

import static org.assertj.vavr.api.VavrAssertions.assertThat;

class CodeGeneratorIntegrationTest {

    private final Tokeniser tokeniser = new Tokeniser(CompilationUnit::new, TokeniserImpl::new);

    @Test
    void generateCodeReturnsNoneWhenNoCodeNeedsToBeGeneratedBecauseTheMethodAlreadyExists() {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(new ExecutionContext.Factory().create(/*this should contain the logicalClass*/))
                .withTokeniserFactory(() -> tokeniser)
                .withClassNameFinderFactory(ClassNameFinder::new)
                .withFunctionNameFinderFactory(FunctionNameFinder::new)
                .withPartialCodeGeneratorFactory(() -> new PartialCodeGenerator(new PartialClassFactory()))
                .build();
        final String testFunction = "public class TestClass { @Test void test() { new A().doTheThing(); } }";
//        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, ClassName.of("A"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, FunctionName.of("doTheThing"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);


//        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
//        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
//        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
//        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
//        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(logicalClass);
//        when(executionContext.allFunctionsAreInTheContext(logicalClass)).thenReturn(true);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).isEmpty();
    }

    @Test
    void generateCodeReturnsCodeWhenTheClassAlreadyExistsButTheMethodDoesNot() {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(new ExecutionContext.Factory().create())
                .withTokeniserFactory(() -> tokeniser)
                .withClassNameFinderFactory(ClassNameFinder::new)
                .withFunctionNameFinderFactory(FunctionNameFinder::new)
                .withPartialCodeGeneratorFactory(() -> new PartialCodeGenerator(new PartialClassFactory()))
                .build();
        final String testFunction = "public class TestClass { @Test void test() { new A().doTheThing(); } }";
//        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(ClassName.of("A"))
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(FunctionName.of("doTheThing")).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

//        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
//        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
//        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

//        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
//        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
//        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
//        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
//        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(expectedClass);
//        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }

    @Test
    void generateCodeReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist__AndOnlyTheDefaultConstructorIsUsedInTheTest() {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(new ExecutionContext.Factory().create())
                .withTokeniserFactory(() -> tokeniser)
                .withClassNameFinderFactory(ClassNameFinder::new)
                .withFunctionNameFinderFactory(FunctionNameFinder::new)
                .withPartialCodeGeneratorFactory(() -> new PartialCodeGenerator(new PartialClassFactory()))
                .build();
        final String testFunction = "public class TestClass { @Test void test() { new A().doTheThing(); } }";
//        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(ClassName.of("A"))
                .withDefaultConstructor()
                .build();

//        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
//        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
//        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

//        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
//        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
//        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.empty()));
//        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.empty()));
//        when(partialCodeGenerator.generateCodeForClass(partialClass, List.empty())).thenReturn(expectedClass);
//        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }

    @Test
    void generateCodeReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist() {
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(new ExecutionContext.Factory().create())
                .withTokeniserFactory(() -> tokeniser)
                .withClassNameFinderFactory(ClassNameFinder::new)
                .withFunctionNameFinderFactory(FunctionNameFinder::new)
                .withPartialCodeGeneratorFactory(() -> new PartialCodeGenerator(new PartialClassFactory()))
                .build();
        final String testFunction = "public class TestClass { @Test void test() { new A().doTheThing(); } }";
//        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
//                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);
        // public class A { public A() {} public void doTheThing() {} }
        final ILogicalClass expectedClass = LogicalClass.builder().withName(ClassName.of("A"))
                .withDefaultConstructor()
                .withMethod(LogicalFunction.builder().withName(FunctionName.of("doTheThing")).havingNoParameters().returning(ReturnType.VOID).withBody(List.of(Token.EMPTY)).build())
                .build();

//        when(partialCodeGeneratorFactory.get()).thenReturn(partialCodeGenerator);
//        when(classNameFinderFactory.get()).thenReturn(classNameFinder);
//        when(functionNameFinderFactory.get()).thenReturn(functionNameFinder);

//        when(classNameFinder.findConstructedClasses(tokens)).thenReturn(List.of(className));
//        when(partialCodeGenerator.generateConstructorCodeForClasses(List.of(className))).thenReturn(List.of(partialClass));
//        Mockito.<Map<IClassName, ? extends Seq<IFunctionName>>>when(functionNameFinder.findFunctionsUsed(tokens)).thenReturn(HashMap.of(className, List.of(doTheThing)));
//        when(partialCodeGenerator.initialisePartialClass(List.of(partialClass))).thenReturn(t -> Tuple.of(partialClass, List.of(doTheThing)));
//        when(partialCodeGenerator.generateCodeForClass(partialClass, List.of(doTheThing))).thenReturn(expectedClass);
//        when(executionContext.allFunctionsAreInTheContext(expectedClass)).thenReturn(false);

        final Seq<ILogicalClass> code = codeGenerator.generateCodeSatisfying(testFunction);

        assertThat(code).contains(expectedClass);
    }
}