package me.shaftesbury;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Option;
import me.shaftesbury.codegenerator.CodeGenerator;
import me.shaftesbury.codegenerator.ExecutionContext;
import me.shaftesbury.codegenerator.ExecutionContextExtender;
import me.shaftesbury.codegenerator.ICodeGenerator;
import me.shaftesbury.codegenerator.IExecutionContext;
import me.shaftesbury.codegenerator.ITestRunner;
import me.shaftesbury.codegenerator.MethodInvocationUtilsProxy;
import me.shaftesbury.codegenerator.TestRunner;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import me.shaftesbury.codegenerator.text.LineOfCode;
import me.shaftesbury.codegenerator.text.TestMethod;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class StepDefs {

    private ITestMethod testMethod;
    private Option<Throwable> results;
    private ICodeGenerator codeGenerator;
    private IExecutionContext executionContext;

    @Given("a unit test")
    public void a_unit_test(final String test) {
        final List<String> lines = List.ofAll(Arrays.asList(test.split(";" + System.lineSeparator())));
        testMethod = new TestMethod(LineOfCode::new, lines);
    }

    @Given("an execution context containing class {word} and its definition")
    public void an_execution_context_containing(final String className, final String docString) {
        executionContext = ExecutionContext.builder()
                .withCompiler(new RuntimeCompiler())
                .withContext(List.of(new Class(className, docString)))
                .build();
    }

    @Given("an empty execution context")
    public void an_empty_execution_context() {
        executionContext = ExecutionContext.builder()
                .withContext(List.empty())
                .withCompiler(new RuntimeCompiler())
                .build();
    }

    @When("I run the test in the supplied context")
    public void i_run_the_test_in_the_supplied_context() {
        // the code generator should take the execution context of all the generated code thus far,
        // generate new code (if necessary) and execute the test
        codeGenerator = CodeGenerator.builder().withExecutionContext(executionContext).build();
        final Seq<Class> classes = //codeGenerator.generateCodeFor(testMethod);
                List.of(new Class(testMethod.getClassName(), testMethod.getMethod()));
        final IExecutionContext newExecutionContext = ((ExecutionContext) executionContext).toBuilder()
                .withAdditionalClasses(classes)
                .build();
        final ITestRunner testRunner = TestRunner.builder()
                .withExecutionContext(newExecutionContext)
                .withExecutionContextExtender(new ExecutionContextExtender())
                .withMethodInvocationUtils(new MethodInvocationUtilsProxy())
                .build();
        results = testRunner.execute(testMethod);
    }

    @Then("the test should execute without errors")
    public void the_test_should_execute_without_errors() {
        assertThat(results).isEmpty();
    }

    @Then("the application should generate code")
    public void the_application_should_generate_code(String docString) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
