package me.shaftesbury.codegenerator;

import me.shaftesbury.codegenerator.tokeniser.IToken;

public class Reference implements IToken {
    private final String name;

    private Reference(final String name) {
        this.name = name;
    }

    public static Reference of(final String name) {
        return new Reference(name);
    }

    public String getName() {
        return name;
    }
}
