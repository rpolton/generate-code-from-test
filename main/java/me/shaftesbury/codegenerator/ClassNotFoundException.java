package me.shaftesbury.codegenerator;

public class ClassNotFoundException extends RuntimeException {
    private final String className;

    public ClassNotFoundException(final String className) {
        super("Class '" + className + "' was not found in the code context");
        this.className = className;
    }

    public String getClassName() {
        return className;
    }
}
