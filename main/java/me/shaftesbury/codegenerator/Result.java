package me.shaftesbury.codegenerator;

public interface Result {
    boolean isError();

    String getCode();

    Object getValue();

    boolean isCode();
}
