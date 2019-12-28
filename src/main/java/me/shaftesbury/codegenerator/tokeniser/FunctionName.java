package me.shaftesbury.codegenerator.tokeniser;

public class FunctionName implements IToken {
    private final String name;

    private FunctionName(final String name) {
        this.name = name;
    }

    public static FunctionName of(final String name) {
        return new FunctionName(name);
    }
}
