package me.shaftesbury.codegenerator.tokeniser;

public class ClassName implements IToken {
    private final String name;

    private ClassName(final String name) {
        this.name = name;
    }

    public static ClassName of(final String name) {
        return new ClassName(name);
    }
}
