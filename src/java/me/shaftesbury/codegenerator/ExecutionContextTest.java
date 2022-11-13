package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExecutionContextTest {

    public static final ClassName CLASS_NAME = ClassName.of("SourceCode");
    public static final FunctionName FUNCTION_NAME = FunctionName.of("doTheThing");

    @Test
    void builder() {
        assertThat(ExecutionContext.builder()).isNotNull().isInstanceOf(ExecutionContext.Builder.class);
    }

    @Test
    void buildWithCompiler(@Mock final RuntimeCompiler compiler, @Mock final ILogicalClass class1, @Mock final ILogicalClass class2) {
        final Seq<ILogicalClass> context = List.of(class1, class2);

//        when(class1.getName()).thenReturn("class1");
//        when(class1.getPublicFunctions()).thenReturn(List.of("fn1"));
//        when(class1.getBody()).thenReturn("body1");
//        when(class2.getName()).thenReturn("class2");
//        when(class2.getPublicFunctions()).thenReturn(List.of("fn2"));
//        when(class2.getBody()).thenReturn("body2");

        final ExecutionContext executionContext = ExecutionContext.builder().withContext(context).withCompiler(compiler).build();

        assertThat(executionContext.getClasses()).isSameAs(context);
        assertThat(executionContext.getRuntimeCompiler()).isSameAs(compiler);
//        verify(compiler).addClass("class1", "body1");
//        verify(compiler).addClass("class2", "body2");
//        verify(compiler).compile();
//        verifyNoMoreInteractions(compiler);
    }

    @Test
    void getMethodsReturnsTrueWhenAllMethodsOfAClassAreInTheContext(@Mock final ILogicalClass classAsDefinedInContext,
                                                                    @Mock final ILogicalFunction method1,
                                                                    @Mock final ILogicalFunction method2) {
        when(classAsDefinedInContext.getName()).thenReturn(CLASS_NAME);
        when(classAsDefinedInContext.getMethods()).thenReturn(List.of(method1, method2));
        when(method1.getName()).thenReturn(FUNCTION_NAME);
        when(method2.getName()).thenReturn(FunctionName.of("theOtherThing"));

        final ExecutionContext executionContext = ExecutionContext.builder().withContext(List.of(classAsDefinedInContext)).build();

        final boolean result = executionContext.allFunctionsAreInTheContext(CLASS_NAME, List.of(FUNCTION_NAME, FunctionName.of("theOtherThing")));

        assertThat(result).isTrue();
    }

//    @Test
//    void toBuilder(@Mock final ILogicalClass aClass, @Mock final RuntimeCompiler runtimeCompiler) {
//        final ExecutionContext executionContext = ExecutionContext.builder()
//                .withContext(List.of(aClass))
//                .withCompiler(runtimeCompiler)
//                .build();
//
////        when(aClass.getPublicFunctions()).thenReturn(List.of("test"));
//
//        final IExecutionContext actual = executionContext.toBuilder().build();
//
//        assertThat(actual).isEqualTo(executionContext);
//    }

    @Test
    void getMethodsReturnsFalseWhenNoMethodsOfAClassAreInTheContext(@Mock final ILogicalClass classAsDefinedInContext) {
        when(classAsDefinedInContext.getName()).thenReturn(CLASS_NAME);
        when(classAsDefinedInContext.getMethods()).thenReturn(List.empty());

        final ExecutionContext executionContext = ExecutionContext.builder().withContext(List.of(classAsDefinedInContext)).build();

        final boolean result = executionContext.allFunctionsAreInTheContext(CLASS_NAME, List.of(FUNCTION_NAME));

        assertThat(result).isFalse();
    }

    @Test
    void getMethodsReturnsFalseWhenSomeMethodsOfAClassAreInTheContext(@Mock final ILogicalClass classAsDefinedInContext,
                                                                      @Mock final ILogicalFunction method) {
        when(classAsDefinedInContext.getName()).thenReturn(CLASS_NAME);
        when(classAsDefinedInContext.getMethods()).thenReturn(List.of(method));
        when(method.getName()).thenReturn(FUNCTION_NAME);

        final ExecutionContext executionContext = ExecutionContext.builder().withContext(List.of(classAsDefinedInContext)).build();

        final boolean result = executionContext.allFunctionsAreInTheContext(CLASS_NAME, List.of(FUNCTION_NAME, FunctionName.of("theOtherThing")));

        assertThat(result).isFalse();
    }

    @Nested
    class Preconditions {
        @Test
        void buildWithoutContextFails(@Mock final RuntimeCompiler runtimeCompiler) {
            assertThatNullPointerException().isThrownBy(() -> ExecutionContext.builder().withCompiler(runtimeCompiler).build())
                    .withMessage("context must not be null");
        }

        @Test
        void buildWithoutCompilerFails() {
            assertThatNullPointerException().isThrownBy(() -> ExecutionContext.builder().withContext(List.empty()).build())
                    .withMessage("runtimeCompiler must not be null");
        }
    }
}