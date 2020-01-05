package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ExecutionContextExtenderTest {

    @Test
    void addTestMethod() {
        final ExecutionContextExtender executionContextExtender = new ExecutionContextExtender();
        final ExecutionContext executionContext = mock(ExecutionContext.class);
        final ExecutionContext.Builder builder = mock(ExecutionContext.Builder.class);
        final RuntimeCompiler runtimeCompiler = mock(RuntimeCompiler.class);
        final Seq<Class> testClass = List.empty();
        final ITestMethod testMethod = mock(ITestMethod.class);

        when(executionContext.toBuilder()).thenReturn(builder);
        when(builder.withAdditionalClasses(any(Seq.class))).thenReturn(builder);
        when(builder.build()).thenReturn(executionContext);
        when(executionContext.getRuntimeCompiler()).thenReturn(runtimeCompiler);
        when(testMethod.getClassName()).thenReturn("Test");
        when(testMethod.getMethodName()).thenReturn("test");
        when(testMethod.getMethod()).thenReturn("@Test void test() { new A(); }");

        final IExecutionContext actual = executionContextExtender.addTestMethod(executionContext, testMethod);

        assertThat(actual).isEqualTo(executionContext);

        verify(executionContext).toBuilder();
        verify(builder).withAdditionalClasses(List.of(new Class("Test", List.of("test"), "")));
        verify(builder).build();
        verify(executionContext).getRuntimeCompiler();
        verify(runtimeCompiler).addClass("Test", "@Test void test() { new A(); }");
        verify(runtimeCompiler).compile();
        verifyNoMoreInteractions(executionContext, runtimeCompiler, builder);
    }
}