package me.shaftesbury.codegenerator;

import me.shaftesbury.codegenerator.tokeniser.Token;

import static me.shaftesbury.codegenerator.tokeniser.Token.PRIVATE;

public class TokenToAccessConverter {
    public static Access convert(final Token token) {
        return PRIVATE.equals(token) ? Access.PRIVATE_FINAL : null;
    }
}
