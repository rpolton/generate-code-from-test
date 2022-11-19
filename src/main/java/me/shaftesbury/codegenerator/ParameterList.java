package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ParameterList {
    private final Seq<IFunctionParameter> parameters;

    public ParameterList() {
        parameters = List.empty();
    }

    private ParameterList(final Builder builder) {
        parameters = builder.parameters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Seq<IFunctionParameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("parameters", parameters)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final ParameterList that = (ParameterList) o;

        return new EqualsBuilder().append(parameters, that.parameters).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(parameters).toHashCode();
    }

    public ParameterList append(final FunctionParameter parameter) {
        return createBuilderFromThis().withParameter(parameter).build();
    }

    private Builder createBuilderFromThis() {
        return parameters.foldRight(builder(), (p, b) -> b.withParameter(p));
    }

    public static class Builder {

        private List<IFunctionParameter> parameters = List.empty();

        public Builder withParameter(final IFunctionParameter parameter) {
            parameters = parameters.append(parameter);
            return this;
        }

        public ParameterList build() {
            return new ParameterList(this);
        }
    }
}
