package me.shaftesbury.codegenerator.model;

import me.shaftesbury.codegenerator.FunctionBody;
import me.shaftesbury.codegenerator.FunctionParameter;
import me.shaftesbury.codegenerator.ParameterList;
import me.shaftesbury.codegenerator.Type;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Constructor implements IConstructor {
    private final ParameterList functionParameters;
    private final FunctionBody body;

    private Constructor(final Builder builder) {
        functionParameters = builder.functionParameters;
        body = builder.body;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("functionParameters", functionParameters)
                .append("body", body)
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Constructor that = (Constructor) o;

        return new EqualsBuilder().append(functionParameters, that.functionParameters).append(body, that.body).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(functionParameters).append(body).toHashCode();
    }

    public static class Builder {

        private ParameterList functionParameters = new ParameterList();
        private FunctionBody body;

        public Builder withParameter(final Type type, final String name) {
            functionParameters = functionParameters.append(FunctionParameter.of(type, name));
            return this;
        }

        public Builder withBody(final FunctionBody body) {
            this.body = body;
            return this;
        }

        public Builder withParameters(final ParameterList iFunctionParameters) {
            functionParameters = iFunctionParameters;
            return this;
        }

        public Constructor build() {
            if (body == null) throw new IllegalArgumentException("oops busted");
            return new Constructor(this);
        }
    }
}
