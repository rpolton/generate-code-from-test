package me.shaftesbury.codegenerator;

import io.vavr.control.Option;
import me.shaftesbury.codegenerator.model.ITestMethod;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class TestRunnerTest {

    @Test
    void executeTestMethod(@Mock final ExecutionContextExtender executionContextExtender, @Mock final ExecutionContext executionContext, @Mock final MethodInvocationUtilsProxy methodInvocationUtils, @Mock final ITestMethod testMethod) {
        final ITestRunner testRunner = TestRunner.builder()
                .withExecutionContext(executionContext)
                .withExecutionContextExtender(executionContextExtender)
                .withMethodInvocationUtils(methodInvocationUtils)
                .build();
        final String testClassName = "Test";
        final String testMethodName = "test";
        final Option<Exception> returnValue = Option.none();

//        when(testMethod.getClassName()).thenReturn(testClassName);
//        when(testMethod.getMethodName()).thenReturn(testMethodName);
//        when(executionContextExtender.addTestMethod(executionContext, testMethod)).thenReturn(executionContext);
//        when(methodInvocationUtils.invokeTestMethod(executionContext, testClassName, testMethodName)).thenReturn(returnValue);

        final Option<Exception> exception = testRunner.execute(testMethod);

//        verify(executionContextExtender).addTestMethod(executionContext, testMethod);
//        verify(methodInvocationUtils).invokeTestMethod(executionContext, testClassName, testMethodName);
//        verifyNoMoreInteractions(executionContextExtender, methodInvocationUtils);

        assertThat(exception).isEqualTo(returnValue);
    }
}