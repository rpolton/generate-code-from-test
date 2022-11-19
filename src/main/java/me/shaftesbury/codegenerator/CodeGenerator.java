package me.shaftesbury.codegenerator;

import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;

import java.util.function.Supplier;

public class CodeGenerator implements ICodeGenerator {
    private final IExecutionContext executionContext;
    private final Supplier<ITokeniser> tokeniserFactory;
    private final Supplier<IClassNameFinder> classNameFinderFactory;
    private final Supplier<IFunctionNameFinder> functionNameFinderFactory;
    private final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory;

    public CodeGenerator(final Builder builder) {
        executionContext = builder.executionContext;
        tokeniserFactory = builder.tokeniserBuilder;
        classNameFinderFactory = builder.classNameFinder;
        functionNameFinderFactory = builder.functionNameFinder;
        partialCodeGeneratorFactory = builder.partialCodeGenerator;
    }

//    private Option<Exception> tryAgain(final Seq<ILogicalClass> classes) {
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
    public Supplier<ITokeniser> getTokeniserFactory() {
        return tokeniserFactory;
    }

    @Override
    public Supplier<IClassNameFinder> getClassNameFinderFactory() {
        return classNameFinderFactory;
    }

    @Override
    public Supplier<IFunctionNameFinder> getFunctionNameFinderFactory() {
        return functionNameFinderFactory;
    }

    @Override
    public Supplier<PartialCodeGenerator> getPartialCodeGeneratorFactory() {
        return partialCodeGeneratorFactory;
    }

    @Override
    public Seq<ILogicalClass> generateCodeSatisfying(final String testClass) {
        final PartialCodeGenerator partialCodeGenerator = partialCodeGeneratorFactory.get();
        final ITokeniser tokeniser = tokeniserFactory.get();
        final IClassNameFinder classNameFinder = classNameFinderFactory.get();
        final IFunctionNameFinder functionNameFinder = functionNameFinderFactory.get();

//        final Seq<ILogicalClass> classesInContext = executionContext.getClasses();
//        final Seq<IClassName> classNamesInContext = classesInContext.map(ILogicalClass::getName);

        final Seq<IToken> tokens = tokeniser.tokenise(testClass);
        final Seq<? extends IClassName> classesUsedInTestMethod = classNameFinder.findConstructedClasses(tokens);
        final Seq<PartialClass> constructorsForClassesUsedInTestMethod = partialCodeGenerator.generateConstructorCodeForClasses(classesUsedInTestMethod);
//        final Seq<IClassName> classNamesUsedInTestNotAlreadyInContext = classesUsedInTestMethod.filterNot(classNamesInContext::contains);

        final Map<IClassName, ? extends Seq<IFunctionName>> classAndFnsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);
        return classAndFnsUsedInTest
                .map(partialCodeGenerator.initialisePartialClass(constructorsForClassesUsedInTestMethod))
                .map(t -> partialCodeGenerator.generateCodeForClass(t._1, t._2))
                .filterNot(executionContext::allFunctionsAreInTheContext);
    }

    public static class Builder {
        public Supplier<ITokeniser> tokeniserBuilder;
        public Supplier<IClassNameFinder> classNameFinder;
        public Supplier<IFunctionNameFinder> functionNameFinder;
        public Supplier<PartialCodeGenerator> partialCodeGenerator;
        private IExecutionContext executionContext;

        public Builder withExecutionContext(final IExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        public Builder withClassNameFinderFactory(final Supplier<IClassNameFinder> classNameFinder) {
            this.classNameFinder = classNameFinder;
            return this;
        }

        public Builder withFunctionNameFinderFactory(final Supplier<IFunctionNameFinder> functionNameFinder) {
            this.functionNameFinder = functionNameFinder;
            return this;
        }

        public Builder withTokeniserFactory(final Supplier<ITokeniser> tokeniserBuilder) {
            this.tokeniserBuilder = tokeniserBuilder;
            return this;
        }

        public Builder withPartialCodeGeneratorFactory(final Supplier<PartialCodeGenerator> partialCodeGenerator) {
            this.partialCodeGenerator = partialCodeGenerator;
            return this;
        }

        public CodeGenerator build() {
            return new CodeGenerator(this);
        }
    }
}
