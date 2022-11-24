package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static me.shaftesbury.codegenerator.tokeniser.Token.EMPTY;

public class LogicalFunction implements ILogicalFunction {
    private final IFunctionName name;
    private final List<IFunctionParameter> parameters;
    private final ReturnType returnType;
    private final List<IToken> body;

    private LogicalFunction(final Builder builder) {
        this.name = builder.name;
        this.parameters = builder.params;
        this.returnType = builder.returnType;
        this.body = builder.body;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public IFunctionName getName() {
        return name;
    }

    @Override
    public Seq<IFunctionParameter> getParameters() {
        return parameters;
    }

    @Override
    public ReturnType getReturnType() {
        return returnType;
    }

    @Override
    public List<IToken> getBody() {
        return body;
    }

    @Override
    public String asCode() {
        return returnType.asCode() + " " + name.asCode() + "() { }";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 3)
                .append(getName())
                .append(getParameters())
                .append(getReturnType())
                .append(getBody())
                .toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final LogicalFunction logicalFunction = (LogicalFunction) obj;
        return new EqualsBuilder()
                .append(getName(), logicalFunction.getName())
                .append(getParameters(), logicalFunction.getParameters())
                .append(getReturnType(), logicalFunction.getReturnType())
                .append(getBody(), logicalFunction.getBody())
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append(getName())
                .append(getParameters())
                .append(getReturnType())
                .append(getBody())
                .toString();
    }

    public static class Builder {
        private IFunctionName name;
        private List<IFunctionParameter> params = List.empty();
        private ReturnType returnType = ReturnType.VOID;
        private List<IToken> body = List.of(EMPTY);

        public Builder withName(final IFunctionName doTheThing) {
            this.name = doTheThing;
            return this;
        }

        public Builder havingNoParameters() {
            this.params = List.empty();
            return this;
        }

        public Builder returning(final ReturnType returnType) {
            this.returnType = returnType;
            return this;
        }

        public Builder withParameters(final Type type, final String name) {
            return this;
        }

        public Builder withBody(final List<IToken> body) {
            this.body = body;
            return this;
        }

        public LogicalFunction build() {
            return new LogicalFunction(this);
        }
    }
}
