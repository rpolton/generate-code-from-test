package me.shaftesbury.codegenerator;

public class Field implements IField {
    private Field(final Type type, final String name) {

    }

    public static Builder withType(final Type type) {
        return name -> new Field(type, name);
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public interface Builder {
        Field withName(String name);
    }
}
