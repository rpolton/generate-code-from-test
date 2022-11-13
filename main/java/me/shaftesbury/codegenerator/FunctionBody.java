package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FunctionBody {
    private final List<IToken> tokens;

    private FunctionBody(final Builder builder) {
        tokens = builder.tokens;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        final FunctionBody that = (FunctionBody) o;

        return new EqualsBuilder().append(tokens, that.tokens).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(tokens).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tokens", tokens)
                .toString();
    }

    public static class Builder {

        private List<IToken> tokens = List.empty();

        public Builder withTokens(final List<IToken> tokens) {
            this.tokens = tokens;
            return this;
        }

        public FunctionBody build() {
            if (tokens == null) throw new IllegalArgumentException("busted");
            return new FunctionBody(this);
        }

        public Builder withToken(final IToken token) {
            this.tokens = tokens.append(token);
            return this;
        }
    }
}
