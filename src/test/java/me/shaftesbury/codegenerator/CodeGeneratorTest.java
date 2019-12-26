package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.text.Class;
import me.shaftesbury.codegenerator.text.ITestMethod;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CodeGeneratorTest {

    @Test
    void builder() {
        final CodeGenerator.Builder builder = CodeGenerator.builder();

        assertThat(builder).isNotNull();
    }

    @Test
    void generateCodeForReturnsNoneWhenNoCodeNeedsToBeGeneratedBecauseTheMethodAlreadyExists() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .build();
        final Class sourceCodeClass = mock(Class.class);

        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getMethod()).thenReturn("void test() { new A(); }");
        when(sourceCodeClass.getName()).thenReturn("A");
        when(sourceCodeClass.getBody()).thenReturn("public class A { public A() {} }");

        final Seq<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass).getName();
        verify(sourceCodeClass).getBody();
        verify(testMethod).getMethod();

        assertThat(code.isEmpty()).isTrue();
    }

    @Test
    void generateCodeForReturnsCodeWhenTheClassAlreadyExistsButTheMethodDoesNot() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .build();
        final Class sourceCodeClass = mock(Class.class);

        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getClassName()).thenReturn("Test");
        when(testMethod.getMethodName()).thenReturn("doSomethingElse");
        when(sourceCodeClass.getName()).thenReturn("Test");
        when(sourceCodeClass.getBody()).thenReturn("void test() { new A(); }");

        final Seq<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass).getName();
        verify(sourceCodeClass).getBody();
        verify(testMethod).getClassName();
        verify(testMethod).getMethodName();

        assertThat(code.isEmpty()).isFalse();
        assertThat(code.toJavaList()).hasSize(1);
        final Class expectedClass = new Class("a", "b");
        assertThat(code.toJavaList().get(0)).isEqualTo(expectedClass);
    }

    @Test
    void generateCodeForReturnsListContainingGeneratedCodeWhenTheClassDoesNotAlreadyExist() {
        final IExecutionContext executionContext = mock(IExecutionContext.class);
        final ITestMethod testMethod = mock(ITestMethod.class);
        final ICodeGenerator codeGenerator = CodeGenerator.builder()
                .withExecutionContext(executionContext)
                .build();
        final Class sourceCodeClass = mock(Class.class);

        when(executionContext.getContext()).thenReturn(List.of(sourceCodeClass));
        when(testMethod.getClassName()).thenReturn("Test");
        when(testMethod.getMethodName()).thenReturn("test");
        when(sourceCodeClass.getName()).thenReturn("Test");
        when(sourceCodeClass.getBody()).thenReturn("void test() { new A(); }");

        final Seq<Class> code = codeGenerator.generateCodeFor(testMethod);

        verify(executionContext).getContext();
        verify(sourceCodeClass).getName();
        verify(sourceCodeClass).getBody();
        verify(testMethod).getClassName();
        verify(testMethod).getMethodName();

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