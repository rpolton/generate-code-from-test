package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class CodeGeneratorTest {

    @Test
    void builder() {
        final CodeGenerator.Builder builder = CodeGenerator.builder();

        assertThat(builder).isNotNull();
    }

    @Test
    void generateCodeForReturnsNoneWhenNoCodeNeedsToBeGenerated() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .build();

        final Seq<Class> code = codeGenerator.generateCodeFor(testMethod);

        assertThat(code.isEmpty()).isTrue();
    }

    @Test @Disabled("this is expected to fail because it's not implemented yet")
    void generateCodeForReturnsListContainingGeneratedCode() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .build();

        final Seq<Class> code = codeGenerator.generateCodeFor(testMethod);

        assertThat(code.isEmpty()).isFalse();
    }

    @Nested
    class Builder {
        @Test
        void buildWithStatementBuilder() {
            final IExecutionContext context = mock(IExecutionContext.class);

            final ICodeGenerator codeGenerator = CodeGenerator.builder().withExecutionContext(context).build();

            assertThat(codeGenerator.getExecutionContext()).isEqualTo(context);
        }
    }
}