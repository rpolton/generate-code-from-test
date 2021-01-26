package me.shaftesbury.codegenerator.model;

import io.vavr.collection.List;
import me.shaftesbury.VisibleForTesting;
import me.shaftesbury.codegenerator.IClassName;
import me.shaftesbury.codegenerator.IExecutionContext;
import me.shaftesbury.codegenerator.Result;
import me.shaftesbury.codegenerator.text.ILine;
import me.shaftesbury.codegenerator.text.LineOfCode;
import me.shaftesbury.codegenerator.tokeniser.ClassName;

import java.util.function.Function;

public class TestMethod implements ITestMethod {
    private final List<ILine> lines;

    @VisibleForTesting
    TestMethod(final List<ILine> lines) {
        this.lines = lines;
    }

    public IClassName getClassName() {
        return ClassName.of("Test");
    }

    public IMethodName getMethodName() {
        return MethodName.of("test");
    }

    public Result execute(final IExecutionContext newContext) {
        return null;
    }

    private static Builder builder() {
        return new Builder();
    }

    public IMethod getMethod() {
        return Method.builder().withPublicClassName(getClassName()).withLines(lines);
    }

    @Override
    public String toString() {
        return "TestMethod{" +
                "lines=" + lines +
                '}';
    }

    public static class TestMethodFactory {
        public static TestMethod create(final List<String> lines) {
            return TestMethod.builder().withLines(lines).withLineBuilder(LineOfCode::new).build();
        }
    }

    public static class Builder {

        private List<String> lines;
        private Function<String, ILine> map;

        public Builder withLines(final List<String> lines) {
            this.lines = lines;
            return this;
        }

        public Builder withLineBuilder(final Function<String, ILine> map) {
            this.map = map;
            return this;
        }

        public TestMethod build() {
            return new TestMethod(lines.map(map));
        }
    }
}
