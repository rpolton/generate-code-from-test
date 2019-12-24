package me.shaftesbury.codegenerator.text;

import io.vavr.collection.List;
import io.vavr.control.Option;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class TestMethod implements ITestMethod {
    private Function<String, ILine> lineBuilder;
    private final List<String> lines;

    public TestMethod(final Function<String, ILine> lineBuilder, final List<String> lines) {
        requireNonNull(lineBuilder, "lineBuilder must not be null");
        requireNonNull(lines, "lines must not be null");
        if (lines.isEmpty()) throw new IllegalArgumentException("lines must not be empty");

        this.lineBuilder = lineBuilder;
        this.lines = lines;
    }

    public String getClassName() {
        return "Test";
    }

    public String getMethodName() {
        return "test";
    }

    public String getMethod() {
        return "public class " + getClassName() + " { " + String.join("; ", lines) + " }";
    }

    @Override
    public Option<Exception> execute() {
        return Option.none();
    }

    @Override
    public String toString() {
        return "TestMethod{" +
                "lineBuilder=" + lineBuilder +
                ", lines=" + lines +
                '}';
    }
}
