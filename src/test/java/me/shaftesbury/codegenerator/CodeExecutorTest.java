package me.shaftesbury.codegenerator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@ExtendWith(MockitoExtension.class)
class CodeExecutorTest {
    @Test
    void validateConstructorParameter() {
        assertThatNullPointerException()
                .isThrownBy(() -> new CodeExecutor(null))
                .withMessage("codeGeneratorFactory must not be null");
    }

    @Test
    void executeTestMethodReturnsErrorResult(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::isError).isEqualTo(true);
    }

    @Test
    void executeTestMethodReturnsNonErrorResult(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::isError).isEqualTo(false);
    }

    @Test
    void executeTestMethodReturnsResultContainingValue(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator, @Mock final Object expectedValue) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::getValue).isEqualTo(expectedValue);
    }

    @Test
    void executeTestMethodReturnsResultWithCodeIdentifier(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::isCode).isEqualTo(true);
    }

    @Test
    void executeTestMethodReturnsResultWithoutCodeIdentifier(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::isCode).isEqualTo(false);
    }

    @Test
    void executeTestMethodReturnsResultContainingCode(@Mock final IExecutionContext execContext, @Mock final CodeGenerator codeGenerator, @Mock final Object code) {
        final CodeExecutor codeExecutor = new CodeExecutor(ec -> codeGenerator);
        final String testMethod = "";
        final Result result = codeExecutor.execute(testMethod).inContext(execContext);
        assertThat(result).extracting(Result::getCode).isEqualTo(code);
    }
}