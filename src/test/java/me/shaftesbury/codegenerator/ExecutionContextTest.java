package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class ExecutionContextTest {
    @Test
    void builder() {
        assertThat(ExecutionContext.builder()).isNotNull().isInstanceOf(ExecutionContext.Builder.class);
    }


    @Test
    void buildWithoutContextFails() {
        assertThatNullPointerException().isThrownBy(() -> ExecutionContext.builder().withCompiler(mock(RuntimeCompiler.class)).build())
                .withMessage("context must not be null");
    }

    @Test
    void buildWithoutCompilerFails() {
        assertThatNullPointerException().isThrownBy(() -> ExecutionContext.builder().withContext(List.empty()).build())
                .withMessage("runtimeCompiler must not be null");
    }

    @Test
    void buildWithCompiler() {
        final RuntimeCompiler compiler = mock(RuntimeCompiler.class);
        final Class class1 = mock(Class.class);
        final Class class2 = mock(Class.class);
        final List<Class> context = List.of(class1, class2);

        when(class1.getName()).thenReturn("class1");
        when(class1.getBody()).thenReturn("body1");
        when(class2.getName()).thenReturn("class2");
        when(class2.getBody()).thenReturn("body2");

        final ExecutionContext executionContext = ExecutionContext.builder().withContext(context).withCompiler(compiler).build();

        assertThat(executionContext.getContext()).isSameAs(context);
        assertThat(executionContext.getRuntimeCompiler()).isSameAs(compiler);
        verify(compiler).addClass("class1", "body1");
        verify(compiler).addClass("class2", "body2");
        verify(compiler).compile();
        verifyNoMoreInteractions(compiler);
    }

    @Test
    void toBuilder() {
        final ExecutionContext executionContext = (ExecutionContext) ExecutionContext.builder().withContext(mock(Seq.class)).withCompiler(mock(RuntimeCompiler.class)).build();

        final IExecutionContext actual = executionContext.toBuilder().build();

        assertThat(actual).isEqualTo(executionContext);
    }
}