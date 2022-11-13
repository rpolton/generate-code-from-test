package me.shaftesbury.codegenerator;

import io.vavr.control.Option;
import me.shaftesbury.codegenerator.model.ITestMethod;

import static java.util.Objects.requireNonNull;

public class TestRunner implements ITestRunner {
    private final IExecutionContext executionContext;
    private final IExecutionContextExtender executionContextExtender;
    private final MethodInvocationUtilsProxy methodInvocationUtils;

    private TestRunner(final Builder builder) {
        executionContext = builder.executionContext;
        executionContextExtender = builder.executionContextExtender;
        methodInvocationUtils = builder.methodInvocationUtils;
    }

    /*
    Use this for execute() {}

        private static void simpleExample() {
            String classNameA = "ExampleClass";
            String codeA =
                    "public class ExampleClass {" + "\n" +
                            "    public static void exampleMethod(String name) {" + "\n" +
                            "        System.out.println(\"Hello, \"+name);" + "\n" +
                            "    }" + "\n" +
                            "}" + "\n";

            RuntimeCompiler r = new RuntimeCompiler();
            r.addClass(classNameA, codeA);
            r.compile();

            MethodInvocationUtils.invokeStaticMethod(
                    r.getCompiledClass(classNameA),
                    "exampleMethod", "exampleParameter");
        }
     */
    public Option<Exception> execute(final ITestMethod testMethod) {
        final IExecutionContext executionContext = executionContextExtender.addTestMethod(this.executionContext, testMethod);
        return methodInvocationUtils.invokeTestMethod(executionContext, testMethod.getClassName(), testMethod.getMethodName());
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private IExecutionContext executionContext;
        private IExecutionContextExtender executionContextExtender;
        private MethodInvocationUtilsProxy methodInvocationUtils;

        public Builder withExecutionContext(final IExecutionContext executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        public Builder withExecutionContextExtender(final IExecutionContextExtender executionContextExtender) {
            this.executionContextExtender = executionContextExtender;
            return this;
        }

        public Builder withMethodInvocationUtils(final MethodInvocationUtilsProxy methodInvocationUtils) {
            this.methodInvocationUtils = methodInvocationUtils;
            return this;
        }

        public ITestRunner build() {
            requireNonNull(executionContext, "executionContext must not be null");
            requireNonNull(executionContextExtender, "executionContextExtender must not be null");
            requireNonNull(methodInvocationUtils, "methodInvocationUtils must not be null");
            return new TestRunner(this);
        }
    }
}
