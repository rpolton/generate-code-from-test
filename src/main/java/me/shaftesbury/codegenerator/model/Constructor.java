package me.shaftesbury.codegenerator.model;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.FunctionParameter;
import me.shaftesbury.codegenerator.ParameterList;
import me.shaftesbury.codegenerator.Type;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Constructor implements IConstructor {
    private final ParameterList functionParameters;

    private Constructor(final Builder builder) {
        functionParameters = builder.functionParameters;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toString() {
        return new ToStringBuilder("constructor with (functionParameters)").toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final Constructor constructor = (Constructor) o;

        return new EqualsBuilder()
                .append(functionParameters, constructor.functionParameters)
                .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(functionParameters)
                .toHashCode();
    }

    public static class Builder {

        private ParameterList functionParameters = new ParameterList();

        public Builder withParameter(final Type type, final String name) {
            functionParameters = functionParameters.append(FunctionParameter.of(type, name));
            return this;
        }

        public Builder withBody(final List<IToken> body) {
            return this;
        }

        public Builder withParameters(final ParameterList iFunctionParameters) {
            functionParameters = iFunctionParameters;
            return this;
        }

        public Constructor build() {
            return new Constructor(this);
        }
    }
}
