package me.shaftesbury.codegenerator;

import io.vavr.control.Option;
import me.shaftesbury.codegenerator.text.ITestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class TestRunnerTest {

    @Test
    void executeTestMethod() {
        final ExecutionContextExtender executionContextExtender = mock(ExecutionContextExtender.class);
        final ExecutionContext executionContext = mock(ExecutionContext.class);
        final MethodInvocationUtilsProxy methodInvocationUtils = mock(MethodInvocationUtilsProxy.class);
        final ITestRunner testRunner = TestRunner.builder()
                .withExecutionContext(executionContext)
                .withExecutionContextExtender(executionContextExtender)
                .withMethodInvocationUtils(methodInvocationUtils)
                .build();
        final ITestMethod testMethod = mock(ITestMethod.class);
        final String testClassName = "Test";
        final String testMethodName = "test";
        final Option<Exception> returnValue = Option.none();

        when(testMethod.getClassName()).thenReturn(testClassName);
        when(testMethod.getMethodName()).thenReturn(testMethodName);
        when(executionContextExtender.addTestMethod(executionContext, testMethod)).thenReturn(executionContext);
        when(methodInvocationUtils.invokeTestMethod(executionContext, testClassName, testMethodName)).thenReturn(returnValue);

        final Option<Exception> exception = testRunner.execute(testMethod);

        verify(executionContextExtender).addTestMethod(executionContext, testMethod);
        verify(methodInvocationUtils).invokeTestMethod(executionContext, testClassName, testMethodName);
        verifyNoMoreInteractions(executionContextExtender, methodInvocationUtils);

        assertThat(exception).isEqualTo(returnValue);
    }
}