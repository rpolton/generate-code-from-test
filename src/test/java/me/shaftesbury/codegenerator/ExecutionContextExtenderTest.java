package me.shaftesbury.codegenerator;

import org.mockito.junit.jupiter.MockitoSettings;

@MockitoSettings
class ExecutionContextExtenderTest {

//    @Test
//    void addTestMethod(@Mock final ExecutionContext executionContext, @Mock final ExecutionContext.Builder builder, @Mock final RuntimeCompiler runtimeCompiler, @Mock final ITestMethod testMethod) {
//        final ExecutionContextExtender executionContextExtender = new ExecutionContextExtender();
//        final Seq<Class> testClass = List.empty();
//
//        when(executionContext.toBuilder()).thenReturn(builder);
//        when(builder.withAdditionalClasses(any(Seq.class))).thenReturn(builder);
//        when(builder.build()).thenReturn(executionContext);
//        when(executionContext.getRuntimeCompiler()).thenReturn(runtimeCompiler);
//        when(testMethod.getClassName()).thenReturn("Test");
//        when(testMethod.getMethodName()).thenReturn("test");
//        when(testMethod.getMethod()).thenReturn("@Test void test() { new A(); }");
//
//        final IExecutionContext actual = executionContextExtender.addTestMethod(executionContext, testMethod);
//
//        assertThat(actual).isEqualTo(executionContext);
//
//        verify(executionContext).toBuilder();
//        verify(builder).withAdditionalClasses(List.of(new Class("Test", List.of("test"), "")));
//        verify(builder).build();
//        verify(executionContext).getRuntimeCompiler();
//        verify(runtimeCompiler).addClass("Test", "@Test void test() { new A(); }");
//        verify(runtimeCompiler).compile();
//        verifyNoMoreInteractions(executionContext, runtimeCompiler, builder);
//    }
}