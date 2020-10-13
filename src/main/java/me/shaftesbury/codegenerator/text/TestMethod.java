package me.shaftesbury.codegenerator.text;

import io.vavr.collection.List;
import io.vavr.control.Option;
import me.shaftesbury.codegenerator.VisibleForTesting;

import java.util.function.Function;

public class TestMethod implements ITestMethod {
    private final List<ILine> lines;

    @VisibleForTesting
    TestMethod(final List<ILine> lines) {
        this.lines = lines;
    }

    public String getClassName() {
        return "Test";
    }

    public String getMethodName() {
        return "test";
    }

    public static TestMethod createTestMethod(final List<String> lines) {
        return TestMethod.builder().withLines(lines).withLineBuilder(LineOfCode::new).build();
    }

    @Override
    public Option<Exception> execute() {
        return Option.none();
    }

    private static Builder builder() {
        return new Builder();
    }

    public String getMethod() {
        return "public class " + getClassName() + " { " +
                lines.foldRight("", (l1, l2) -> l1.toString() + " " + l2.toString()) +
                "}";
    }

    @Override
    public String toString() {
        return "TestMethod{" +
                "lines=" + lines +
                '}';
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
