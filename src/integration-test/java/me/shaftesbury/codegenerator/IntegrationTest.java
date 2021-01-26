package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.model.TestMethod;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Supplier;

import static me.shaftesbury.utils.functional.UsingWrapper.using;
import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest {

    private static final ClassTransformer classTransformer = new ClassTransformer();
    private static ITokeniser tokeniser2 = new Tokeniser();
    private final ExecutionContext.Factory executionContextFactory = new ExecutionContext.Factory();
    private final IClassNameFinder classNameFinder = new ClassNameFinder();
    private final Supplier<IClassNameFinder> classNameFinderBuilder = () -> classNameFinder;
    private final ITokeniser tokeniser = new Tokeniser();
    private final Supplier<ITokeniser> tokeniserBuilder = () -> tokeniser;
    private final IFunctionNameFinder functionNameFinder = new FunctionNameFinder();
    private final Supplier<IFunctionNameFinder> functionNameFinderBuilder = () -> functionNameFinder;

    private final CodeExecutor codeExecutor = new CodeExecutor(executionContext -> CodeGenerator.builder()
            .withExecutionContext(executionContext)
            .withTokeniserBuilder(tokeniserBuilder)
            .withClassNameFinderBuilder(classNameFinderBuilder)
            .withFunctionNameFinderBuilder(functionNameFinderBuilder)
            .build());

    @Test
    void runATestWhenTheContextContainsTheClassCode() {
        final String test =
                "@Test" +
                        "void test() { " +
                        "    new A(); " +
                        "}";

        final ITestMethod testMethod = givenAUnitTest(test);

        final String docString = "public class A { }";

        final IExecutionContext executionContext = givenAnExecutionContextContainingTheClassDefinition(docString);

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
