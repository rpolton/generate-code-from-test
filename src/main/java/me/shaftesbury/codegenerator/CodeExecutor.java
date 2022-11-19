package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.ILogicalClass;

import java.util.function.Function;

public class CodeExecutor {
    private final Function<IExecutionContext, CodeGenerator> codeGeneratorFactory;

    public CodeExecutor(final Function<IExecutionContext, CodeGenerator> codeGeneratorFactory) {
        this.codeGeneratorFactory = codeGeneratorFactory;
    }

    public Execution execute(final String testClass) {
        return executionContext -> {
            final ICodeGenerator codeGenerator = codeGeneratorFactory.apply(executionContext);
            final Seq<ILogicalClass> classes = codeGenerator.generateCodeSatisfying(testClass);
            final IExecutionContext newContext = new ExecutionContextExtender(executionContext).with(classes);
            return null;//testMethod.execute(newContext);
        };
    }

    public interface Execution {
        Result inContext(IExecutionContext executionContext);
    }
}
