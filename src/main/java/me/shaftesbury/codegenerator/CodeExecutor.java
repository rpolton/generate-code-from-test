package me.shaftesbury.codegenerator;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.model.ITestMethod;

import java.util.function.Function;

public class CodeExecutor {
    private final Function<IExecutionContext, CodeGenerator> codeGeneratorFactory;
    private ICodeGenerator codeGenerator;

    public CodeExecutor(final Function<IExecutionContext, CodeGenerator> codeGeneratorFactory) {
        this.codeGeneratorFactory = codeGeneratorFactory;
    }

    public Execution execute(final ITestMethod testMethod) {
        return executionContext -> {
            codeGenerator = codeGeneratorFactory.apply(executionContext);
            final Traversable<ILogicalClass> classes = codeGenerator.generateCode(testMethod);
            final IExecutionContext newContext = new ExecutionContextExtender(executionContext).with(classes);
            return testMethod.execute(newContext);
        };
    }

    public interface Execution {
        Result inContext(IExecutionContext executionContext);
    }
}
