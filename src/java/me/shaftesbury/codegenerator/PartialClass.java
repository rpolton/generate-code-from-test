package me.shaftesbury.codegenerator;

public class PartialClass {
    private final IClassName className;
    private final LogicalClass.Builder builder;

    public PartialClass(final IClassName className, final LogicalClass.Builder builder) {
        this.className = className;
        this.builder = builder;
    }

    public IClassName getClassName() {
        return className;
    }

    public LogicalClass.Builder getBuilder() {
        return builder;
    }
}