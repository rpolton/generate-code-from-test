package me.shaftesbury;

import io.cucumber.java8.En;
import io.cucumber.java8.PendingException;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Traversable;
import io.vavr.control.Option;
import me.shaftesbury.codegenerator.ClassNameFinder;
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
import me.shaftesbury.codegenerator.tokeniser.Tokeniser;

import java.util.Arrays;

import static me.shaftesbury.codegenerator.text.TestMethod.createTestMethod;
import static org.assertj.core.api.Assertions.assertThat;

public class StepDefs implements En {

    private ITestMethod testMethod;
    private Option<Exception> results;
    private ICodeGenerator codeGenerator;
    private IExecutionContext executionContext;

    public StepDefs() {
        Given("a unit test", (String test) -> {
            final List<String> lines = List.ofAll(Arrays.asList(test.split(";" + System.lineSeparator())));
            testMethod = createTestMethod(lines);
        });

        Given("an execution context containing class {word} and its definition",
                (String className, String docString) -> {
                    executionContext = ExecutionContext.builder()
                            .withCompiler(new RuntimeCompiler())
                            .withContext(List.of(new Class(className, List.of(docString), "")))
                            .build();
                });

        Given("an empty execution context", () -> {
            executionContext = ExecutionContext.builder()
                    .withContext(List.empty())
                    .withCompiler(new RuntimeCompiler())
                    .build();
        });

        When("the test is run in the supplied context", () -> {
            // the code generator should take the execution context of all the generated code thus far,
            // generate new code (if necessary) and execute the test
            codeGenerator = CodeGenerator.builder()
                    .withExecutionContext(executionContext)
                    .withTokeniserBuilder(Tokeniser::new)
                    .withClassNameFinder(ClassNameFinder::new)
                    .build();
            final Seq<Class> classes = //codeGenerator.generateCodeFor(testMethod);
                    List.empty();
            tryAgain(classes);
        });

        Then("the test should execute without errors", () -> {
            assertThat(results).isEmpty();
        });

        Then("the application should generate code", (String docString) -> {
            // Write code here that turns the phrase above into concrete actions
            throw new PendingException();
        });
    }

    private void tryAgain(final Traversable<Class> classes) {
        final IExecutionContext newExecutionContext = ((ExecutionContext) executionContext).toBuilder()
                .withAdditionalClasses(classes)
                .build();
        final ITestRunner testRunner = TestRunner.builder()
                .withExecutionContext(newExecutionContext)
                .withExecutionContextExtender(new ExecutionContextExtender())
                .withMethodInvocationUtils(new MethodInvocationUtilsProxy())
                .build();
        results = testRunner.execute(testMethod);
        if (results.isDefined()) {
            final Traversable<Class> classes1 = codeGenerator.generateCodeFor(testMethod);
            tryAgain(classes1);
        }
    }
}
