package me.shaftesbury.codegenerator;

public class MethodNotFoundException extends RuntimeException {
    public MethodNotFoundException(final String className, final String methodName) {
        super("Could not find method '" + methodName + "' in class '" + className + "'");
    }
}
