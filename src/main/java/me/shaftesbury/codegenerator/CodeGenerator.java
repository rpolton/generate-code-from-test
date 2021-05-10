package me.shaftesbury.codegenerator;

import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;

import java.util.function.Function;
import java.util.function.Supplier;

public class CodeGenerator implements ICodeGenerator {
    private final IExecutionContext executionContext;
    private final Supplier<ITokeniser> tokeniserFactory;
    private final Supplier<IClassNameFinder> classNameFinderFactory;
    private final Supplier<IFunctionNameFinder> functionNameFinderBuilder;
    private final Supplier<PartialCodeGenerator> partialCodeGeneratorFactory;

    public CodeGenerator(final Builder builder) {
        executionContext = builder.executionContext;
        tokeniserFactory = builder.tokeniserBuilder;
        classNameFinderFactory = builder.classNameFinder;
        functionNameFinderBuilder = builder.functionNameFinder;
        partialCodeGeneratorFactory = builder.partialCodeGenerator;
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
    public Supplier<ITokeniser> getTokeniserFactory() {
        return tokeniserFactory;
    }

    @Override
    public Supplier<IClassNameFinder> getClassNameFinderFactory() {
        return classNameFinderFactory;
    }

    @Override
    public Traversable<ILogicalClass> generateCode(final ITestMethod testMethod) {
        final PartialCodeGenerator partialCodeGenerator = partialCodeGeneratorFactory.get();
        final ITokeniser tokeniser = tokeniserFactory.get();
        final IClassNameFinder classNameFinder = classNameFinderFactory.get();
        final IFunctionNameFinder functionNameFinder = functionNameFinderBuilder.get();

//        final Traversable<ILogicalClass> classesInContext = executionContext.getClasses();
//        final Traversable<IClassName> classNamesInContext = classesInContext.map(ILogicalClass::getName);

        final Traversable<IToken> tokens = tokeniser.tokenise(testMethod);
        final Traversable<IClassName> classesUsedInTestMethod = classNameFinder.findConstructedClasses(tokens);
        final Traversable<PartialClass> constructorsForClassesUsedInTestMethod = partialCodeGenerator.generateConstructorCodeForClasses(classesUsedInTestMethod);
//        final Traversable<IClassName> classNamesUsedInTestNotAlreadyInContext = classesUsedInTestMethod.filterNot(classNamesInContext::contains);

        final Map<IClassName, ? extends Traversable<IFunctionName>> classAndFnsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);
        return classAndFnsUsedInTest
                .map(generatePartialClass(partialCodeGenerator, constructorsForClassesUsedInTestMethod))
                .map(t -> partialCodeGenerator.generateCodeForClass(t._1, t._2));
//                .filterNot(t -> executionContext.allFunctionsAreInTheContext(t._1, t._2))
    }

    private Function<Tuple2<IClassName, ? extends Traversable<IFunctionName>>, Tuple2<PartialClass, Traversable<IFunctionName>>> generatePartialClass(final PartialCodeGenerator partialCodeGenerator, final Traversable<PartialClass> constructorsForClassesUsedInTestMethod) {
        return t -> new Tuple2<>(
                generatePartialClass(partialCodeGenerator, constructorsForClassesUsedInTestMethod, t._1),
                t._2);
    }

    private PartialClass generatePartialClass(final PartialCodeGenerator partialCodeGenerator, final Traversable<PartialClass> constructorsForClassesUsedInTestMethod, final IClassName className) {
        return constructorsForClassesUsedInTestMethod
                .find(ptl -> ptl.getClassName().equals(className))
                .getOrElse(() -> partialCodeGenerator.generateCodeForConstructor(className));
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

        public Builder withPartialCodeGenerator(final Supplier<PartialCodeGenerator> partialCodeGenerator) {
            this.partialCodeGenerator = partialCodeGenerator;
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
                    .withPartialCodeGenerator(PartialCodeGenerator::new)
                    .build();
        }
    }
}
