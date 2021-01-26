package me.shaftesbury.codegenerator.model;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.text.ILine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@MockitoSettings
class TestMethodTest {
    @Test
    void getClassName(@Mock final ILine line) {
        assertThat(new TestMethod(List.of(line)).getClassName()).isEqualTo("Test");
    }

    @Test
    void getMethodName(@Mock final ILine line) {
        assertThat(new TestMethod(List.of(line)).getMethodName()).isEqualTo("test");
    }

    @Test
    void getMethod(@Mock final ILine line1, @Mock final ILine line2, @Mock final ILine line3) {
        when(line1.toString()).thenReturn("void test() {");
        when(line2.toString()).thenReturn("new A();");
        when(line3.toString()).thenReturn("}");
        assertThat(new TestMethod(List.of(line1, line2, line3)).getMethod()).isEqualTo("public class Test { void test() { new A(); } }");
    }

    /*
    @Test
    void executeReturnsNone() {
        final Function lineBuilder = mock(Function.class);
        final List<String> lines = List.of("@Test");

        final Option<Exception> results = new TestMethod(lineBuilder, lines).execute();

        assertThat(results).isEqualTo(Option.none());
        lines.forEach(line -> then(lineBuilder).should().apply(line));
        then(lineBuilder).shouldHaveNoMoreInteractions();
    }

    @Test
    void executeReturnsException() {
        assertThat(new TestMethod(mock(Function.class), List.of("@Test")).execute().get()).isEqualTo(new Exception());
    }*/

    @Test
    void test() {
        final String testSource =
                "class MyTestClass extends TestClass {" + System.lineSeparator() +
                        "@Test" + System.lineSeparator() +
                        "void test() {" + System.lineSeparator() +
                        "    new A();" + System.lineSeparator() +
                        "}" + System.lineSeparator() +
                        "}";
        final JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();
        //new SimpleJavaFileObject();
    }

    @Test
    @Disabled
    void cachedCompiler() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // dynamically you can call
        final String className = "mypackage.MyClass";
        final String javaCode = "package mypackage;\n" +
                "class MyClass extends TestClass {\n" +
                "@Test \n" +
                "    void run() {\n" +
                "        System.out.println(\"Hello World\");\n" +
                "    }\n" +
                "}\n";
//        final java.lang.Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
//        final Runnable runner = (Runnable) aClass.newInstance();
//        runner.run();
    }

    @Test
    void writeToFile() {
//        final CachedCompiler JCC = CompilerUtils.DEBUGGING
//                ? new CachedCompiler(new File(parent, "src/test/java"), new File(parent, "target/compiled"))
//                : CompilerUtils.CACHED_COMPILER;
    }

    abstract class ParentClass implements Runnable {
        public abstract void run();
    }

    @Test
    @Disabled
    void cachedCompiler1() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // dynamically you can call
        final String className = "me.shaftesbury.codegenerator.MyClass";
        final String javaCode =
                "package me.shaftesbury.codegenerator;\n" +
                        "class MyClass extends me.shaftesbury.codegenerator.text.TestMethodTest.ParentClass implements Runnable {\n" +
                        "    public void run() {\n" +
                        "        System.out.println(\"Hello World\");\n" +
                        "    }\n" +
                        "}\n";
//        final java.lang.Class aClass = CompilerUtils.CACHED_COMPILER.loadFromJava(className, javaCode);
//        final Runnable myClass = (Runnable) aClass.newInstance();
//        myClass.run();
    }


/*
String comparableClassName = ...; // the class name of the objects you wish to compare
String comparatorClassName = ...; // something random to avoid class name conflicts
String source = "public class " + comparatorClassName + " implements Comparable<" + comparableClassName + "> {" +
                "    public int compare(" + comparableClassName + " a, " + comparableClassName + " b) {" +
                "        return " + expression + ";" +
                "    }" +
                "}";

JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();


Writer out = null;
    JavaFileManager fileManager = null;
    DiagnosticListener<? super JavaFileObject> diagnosticListener = null;
    Iterable<String> options = null;
    Iterable<String> classes = null;
    Iterable<? extends JavaFileObject> compilationUnits = new ArrayList<? extends JavaFileObject>();
compilationUnits.add(
        new SimpleJavaFileObject() {
        // See the JavaDoc page for more details on loading the source String
        }
        );

        compiler.getTask(out, fileManager, diagnosticListener, options, classes, compilationUnits).call();

        Comparator comparator = (Comparator) Class.forName(comparableClassName).newInstance();
 */
}