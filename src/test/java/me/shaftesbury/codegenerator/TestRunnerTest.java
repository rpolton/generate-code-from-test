package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import io.vavr.control.Option;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.ITestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TestRunnerTest {
    @Test
    void executeTestMethod() throws Exception {
        final ExecutionContextExtender executionContextExtender = mock(ExecutionContextExtender.class);
        final ExecutionContext executionContext = mock(ExecutionContext.class);
        final MethodInvocationUtilsProxy methodInvocationUtils = mock(MethodInvocationUtilsProxy.class);
        final ITestRunner testRunner = TestRunner.builder()
                .withExecutionContext(executionContext)
                .withExecutionContextExtender(executionContextExtender)
                .withMethodInvocationUtils(methodInvocationUtils)
                .build();
        final ITestMethod testMethod = mock(ITestMethod.class);
        final RuntimeCompiler runtimeCompiler = mock(RuntimeCompiler.class);
        final String testClassName = "Test";
        final String testMethodName = "test";
        final Seq context = mock(Seq.class);
        final ExecutionContext.Builder builder = mock(ExecutionContext.Builder.class);

        when(executionContext.getRuntimeCompiler()).thenReturn(runtimeCompiler);
        when(executionContext.getContext()).thenReturn(context);
        when(executionContext.toBuilder()).thenReturn(builder);
        when(builder.withAdditionalClasses(any(Seq.class))).thenReturn(builder);
        when(builder.build()).thenReturn(executionContext);
        when(testMethod.getClassName()).thenReturn(testClassName);
        when(testMethod.getMethodName()).thenReturn(testMethodName);
        when(testMethod.getMethod()).thenReturn("class Test { @Test void test() { new Object(); } }");
        when(executionContextExtender.addTestMethod(executionContext, testMethod)).thenReturn(executionContext);
        when(methodInvocationUtils.invokeTestMethod(executionContext, testClassName, testMethodName)).thenReturn(Option.none());

        final Option<Throwable> exception = testRunner.execute(testMethod);

        verify(executionContextExtender).addTestMethod(executionContext, testMethod);
        verify(methodInvocationUtils).invokeTestMethod(executionContext, testClassName, testMethodName);
        verifyNoMoreInteractions(executionContextExtender, methodInvocationUtils);

        assertThat(exception.isEmpty()).isTrue();
    }

    @Test
    void executeTestMethodRaisesExceptionWhenThereIsAProblem() {

//        assertThat(exception.isDefined()).isTrue();
    }
}