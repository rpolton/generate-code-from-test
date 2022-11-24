package me.shaftesbury.codegenerator;

public enum ReturnType {
    VOID;

    public String asCode() {
        return name().toLowerCase();
    }
}
