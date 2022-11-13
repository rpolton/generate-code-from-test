package me.shaftesbury.codegenerator.model;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.IClassName;
import me.shaftesbury.codegenerator.text.ILine;

import java.util.stream.Collectors;

public class Method implements IMethod {
    private final IClassName className;
    private final List<ILine> lines;

    public Method(final Builder builder) {
        this.className = builder.className;
        this.lines = builder.lines;
    }

    public static Builder builder() {
        return new Builder();
    }

    public IClassName getClassName() {
        return className;
    }

    public List<ILine> getLines() {
        return lines;
    }

    @Override
    public String getBody() {
        return lines.map(ILine::getString).collect(Collectors.joining());
    }

    public static final class Builder {

        private IClassName className;
        private List<ILine> lines;

        public Builder withPublicClassName(final IClassName className) {
            this.className = className;
            return this;
        }

        public Builder withLines(final List<ILine> lines) {
            this.lines = lines;
            return this;
        }

        public Method build() {
            return new Method(this);
        }
    }
}
