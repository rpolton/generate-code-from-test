package me.shaftesbury.codegenerator;

import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import io.vavr.collection.Set;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;

import java.util.function.Supplier;

public class CodeGenerator implements ICodeGenerator {
    private final IExecutionContext executionContext;
    private final Supplier<ITokeniser> tokeniserBuilder;
    private final Supplier<IClassNameFinder> classNameFinder;

    public CodeGenerator(final Builder builder) {
        executionContext = builder.executionContext;
        tokeniserBuilder = builder.tokeniserBuilder;
        classNameFinder = builder.classNameFinder;
    }

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
    public Supplier<IClassNameFinder> getClassNameFinder() {
        return classNameFinder;
    }

    @Override
    public Traversable<Class> generateCodeFor(final ITestMethod testMethod) {
        final ITokeniser tokeniser = tokeniserBuilder.get();
        final IClassNameFinder classNameFinder = this.classNameFinder.get();

        final Seq<IToken> tokens = tokeniser.tokenise(testMethod.getMethod());
        final Seq<String> classesUsedInTestMethod = tokens
                .filter(token -> token instanceof ClassName)
                .map(token -> ((ClassName) token).getName());
        // somehow we need the class name as well as the function name
        final Seq<String> functionNamesUsedInTestMethod =
                classNameFinder.findClassNamesPrecededByNew(tokens)
                       /* .appendAll(tokens
                                .filter(token -> token instanceof FunctionName)
                                .filter(token -> !token.equals(testMethod.getMethodName()))
                                .map(token -> ((FunctionName) token).getName()))*/;

        final Seq<Class> existingSourceCodeClasses = executionContext.getContext();
        final Seq<Class> definedClasses = existingSourceCodeClasses.filter(cl -> classesUsedInTestMethod.contains(cl.getName()));
//        final List<String> classDefinitions = definedClasses.map(Class::getBody).toList();
        final Map<String, Seq<String>> functionNamesInSourceCodeClasses = definedClasses.groupBy(Class::getName)
                .mapValues(cls -> cls.flatMap(Class::getPublicFunctions));

        Set<Class> generatedClasses = HashSet.empty();
        for (final String fnUsedInTest : functionNamesUsedInTestMethod) {
            for (final String className : classesUsedInTestMethod) {
                if (functionNamesInSourceCodeClasses.containsKey(className)) {
                    final Seq<String> functionNames = functionNamesInSourceCodeClasses.get(className).get();
                    if (!functionNames.contains(fnUsedInTest)) {
                        generatedClasses = generatedClasses.addAll(generateCodeFor(className, fnUsedInTest));
                    }
                } else {
                    generatedClasses = generatedClasses.addAll(generateCodeFor(className, fnUsedInTest));
                }
            }
        }
        return generatedClasses;
    }

    private Seq<Class> generateCodeFor(final String className, final String methodName) {
        return methodName.equals(className)
                ? List.of(new Class(className, List.of(className), "public " + className + "() {}"))
                : List.of(new Class(className, List.of("void " + methodName + "() {}"), ""));
    }

    public static class Builder {
        public Supplier<ITokeniser> tokeniserBuilder;
        public Supplier<IClassNameFinder> classNameFinder;
        private IExecutionContext executionContext;

        public Builder withExecutionContext(final IExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        public Builder withClassNameFinder(final Supplier<IClassNameFinder> classNameFinder) {
            this.classNameFinder = classNameFinder;
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
}
