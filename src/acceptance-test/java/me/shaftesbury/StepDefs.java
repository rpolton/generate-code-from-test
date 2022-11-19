package me.shaftesbury;

import com.github.javaparser.JavaParser;
import io.cucumber.java8.En;
import io.vavr.collection.List;
import me.shaftesbury.codegenerator.ClassTransformer;
import me.shaftesbury.codegenerator.CodeExecutor;
import me.shaftesbury.codegenerator.ExecutionContext;
import me.shaftesbury.codegenerator.IExecutionContext;
import me.shaftesbury.codegenerator.Result;
import me.shaftesbury.codegenerator.model.ITestMethod;
import me.shaftesbury.codegenerator.model.TestMethod;
import me.shaftesbury.codegenerator.tokeniser.ITokeniser;
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;
import me.shaftesbury.codegenerator.tokeniser.TokeniserImpl;

import java.util.Arrays;
import java.util.function.Function;

import static me.shaftesbury.utils.functional.UsingWrapper.using;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class StepDefs implements En {

    private static final ClassTransformer classTransformer = new ClassTransformer();
    private static ITokeniser tokeniser = new Tokeniser(text -> new JavaParser().parse(text).getResult().get(), new TokeniserImpl());
    private ITestMethod testMethod;
    private final ExecutionContext.Factory executionContextFactory = new ExecutionContext.Factory();
    private IExecutionContext executionContext;
    private final CodeExecutor codeExecutor = new CodeExecutor(mock(Function.class));
    private Result result;

    public StepDefs() {
        Given("a unit test", (String test) ->
                testMethod = using(test.split(";" + System.lineSeparator()))
                        .map(Arrays::asList)
                        .map(List::ofAll)
                        .in(TestMethod.TestMethodFactory::create));

        Given("an execution context containing the class definition", (String docString) ->
                executionContext = using(docString)
                        .map(tokeniser::tokenise)
                        .map(classTransformer::transform)
                        .in(executionContextFactory::create));

        Given("an empty execution context", () ->
                executionContext = executionContextFactory.create());

        When("the test is run in the supplied context", () ->
                result = codeExecutor.execute(testMethod).inContext(executionContext));

        Then("the test should execute without errors", () ->
                assertThat(result).extracting(Result::isError, Result::getValue).containsExactly(false, "this is the expected output"));

        Then("the application should generate code", (String docString) ->
                assertThat(result).extracting(Result::isCode, Result::getCode).containsExactly(true, docString));
    }
}
