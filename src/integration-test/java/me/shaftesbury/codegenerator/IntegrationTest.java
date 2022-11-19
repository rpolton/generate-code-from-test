package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.model.TestMethod;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.ManualTokeniser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static me.shaftesbury.utils.functional.UsingWrapper.using;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private static final ClassTransformer classTransformer = new ClassTransformer();
    private static ITokeniser tokeniser2 = new ManualTokeniser();
    private final ExecutionContext.Factory executionContextFactory = new ExecutionContext.Factory();
    private final IClassNameFinder classNameFinder = new ClassNameFinder();
    private final Supplier<IClassNameFinder> classNameFinderBuilder = () -> classNameFinder;
    private final ITokeniser tokeniser = new ManualTokeniser();
    private final Supplier<ITokeniser> tokeniserFactory = () -> tokeniser;
    private final IFunctionNameFinder functionNameFinder = new FunctionNameFinder();
    private final Supplier<IFunctionNameFinder> functionNameFinderFactory = () -> functionNameFinder;
    private final PartialClassFactory partialClassFactory = new PartialClassFactory();
    private final Supplier<PartialCodeGenerator> partialCodeGeneratorBuilder = () -> new PartialCodeGenerator(partialClassFactory);

    private final CodeExecutor codeExecutor = new CodeExecutor(executionContext -> CodeGenerator.builder()
            .withExecutionContext(executionContext)
            .withTokeniserFactory(tokeniserFactory)
            .withClassNameFinderFactory(classNameFinderBuilder)
            .withFunctionNameFinderFactory(functionNameFinderFactory)
            .withPartialCodeGeneratorFactory(partialCodeGeneratorBuilder)
            .build());

    @Test
    void runATestWhenTheContextContainsTheClassCode() {
        final String test =
                "@Test " +
                        "void test() { " +
                        "    new A(); " +
                        "}";

        final ITestMethod testMethod = givenAUnitTest(test);

        final String existingContext = "public class A { }";

        final IExecutionContext executionContext = givenAnExecutionContextContainingTheClassDefinition(existingContext);

        final Result result = whenTheTestIsRunInTheSuppliedContext(testMethod, executionContext);

        assertThatTheTestShouldExecuteWithoutErrors(result);
    }

    private void assertThatTheTestShouldExecuteWithoutErrors(final Result result) {
        assertThat(result).extracting(Result::isError, Result::getValue).containsExactly(false, "this is the expected output");
    }

    private Result whenTheTestIsRunInTheSuppliedContext(final ITestMethod testMethod, final IExecutionContext executionContext) {
        return codeExecutor.execute(testMethod).inContext(executionContext);
    }

    private IExecutionContext givenAnExecutionContextContainingTheClassDefinition(final String docString) {
        return using(docString)
                .map(tokeniser::tokenise)
                .map(classTransformer::transform)
                .in(executionContextFactory::create);
    }

    private TestMethod givenAUnitTest(final String test) {
        return using(test.split(";" + System.lineSeparator()))
                .map(Arrays::asList)
                .map(List::ofAll)
                .in(TestMethod.TestMethodFactory::create);
    }
}
