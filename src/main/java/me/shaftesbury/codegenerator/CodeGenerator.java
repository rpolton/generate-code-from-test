package me.shaftesbury.codegenerator;

import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;

import java.util.function.Supplier;

public class CodeGenerator implements ICodeGenerator {
    private final IExecutionContext executionContext;
    private final Supplier<ITokeniser> tokeniserBuilder;
    private final Supplier<IClassNameFinder> classNameFinderBuilder;
    private final Supplier<IFunctionNameFinder> functionNameFinderBuilder;

    public CodeGenerator(final Builder builder) {
        executionContext = builder.executionContext;
        tokeniserBuilder = builder.tokeniserBuilder;
        classNameFinderBuilder = builder.classNameFinder;
        functionNameFinderBuilder = builder.functionNameFinder;
    }

//    private Option<Exception> tryAgain(final Traversable<ILogicalClass> classes) {
//        final IExecutionContext newExecutionContext = ExecutionContext.Builder.from(executionContext)
//                .withAdditionalClasses(classes)
//                .build();
//        final ITestRunner testRunner = TestRunner.builder()
//                .withExecutionContext(newExecutionContext)
//                .withExecutionContextExtender(new ExecutionContextExtender())
//                .withMethodInvocationUtils(new MethodInvocationUtilsProxy())
//                .build();
//        results = testRunner.execute(testMethod);
//        if (results.isDefined()) {
//            return tryAgain(codeGenerator.generateCode(testMethod));
//        }
//        return results;
//    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public IExecutionContext getExecutionContext() {
        return executionContext;
    }

    @Override
    public Supplier<ITokeniser> getTokeniserBuilder() {
        return tokeniserBuilder;
    }

    @Override
    public Supplier<IClassNameFinder> getClassNameFinderBuilder() {
        return classNameFinderBuilder;
    }

    @Override
    public Traversable<ILogicalClass> generateCode(final ITestMethod testMethod) {
        final ITokeniser tokeniser = tokeniserBuilder.get();
        final IClassNameFinder classNameFinder = classNameFinderBuilder.get();
        final IFunctionNameFinder functionNameFinder = functionNameFinderBuilder.get();

        final Traversable<ILogicalClass> existingSourceCodeClasses = executionContext.getClasses();
        final Traversable<IClassName> existingClassNames = existingSourceCodeClasses.map(ILogicalClass::getName);

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethod);
        final Traversable<IClassName> classesUsedInTestMethod = classNameFinder.findConstructedClasses(tokens);
        final Traversable<IClassName> classNamesUsedInTest = classesUsedInTestMethod.filterNot(existingClassNames::contains);

        final Map<IClassName, Traversable<IFunctionName>> functionsUsedInTestByClass = functionNameFinder.findFunctionsUsedInTest(classNamesUsedInTest, tokens);
        return functionsUsedInTestByClass
                .filterNot(t -> executionContext.allFunctionsAreInTheContext(t._1, t._2))
                .map(t -> generateCodeForClass(t._1, t._2));
    }

    private ILogicalClass generateCodeForClass(final IClassName className, final Traversable<IFunctionName> functionNames) {
        final LogicalClass.Builder builder = LogicalClass.builder()
                .withName(className)
                .withDefaultConstructor();
        final LogicalClass.Builder builder1 = functionNames.foldRight(builder, (name, br) -> br.withMethod(LogicalFunction.builder().withName(name).build()));
        return builder1.build();
    }

    public static class Builder {
        public Supplier<ITokeniser> tokeniserBuilder;
        public Supplier<IClassNameFinder> classNameFinder;
        public Supplier<IFunctionNameFinder> functionNameFinder;
        private IExecutionContext executionContext;

        public Builder withExecutionContext(final IExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        public Builder withClassNameFinderBuilder(final Supplier<IClassNameFinder> classNameFinder) {
            this.classNameFinder = classNameFinder;
            return this;
        }

        public Builder withFunctionNameFinderBuilder(final Supplier<IFunctionNameFinder> functionNameFinder) {
            this.functionNameFinder = functionNameFinder;
            return this;
        }

        public Builder withTokeniserBuilder(final Supplier<ITokeniser> tokeniserBuilder) {
            this.tokeniserBuilder = tokeniserBuilder;
            return this;
        }

        public CodeGenerator build() {
            return new CodeGenerator(this);
        }
    }

    public static class Factory {
        public ICodeGenerator create(final IExecutionContext executionContext) {
            return CodeGenerator.builder()
                    .withExecutionContext(executionContext)
                    .withTokeniserBuilder(Tokeniser::new)
                    .withClassNameFinderBuilder(ClassNameFinder::new)
                    .build();
        }
    }
}
