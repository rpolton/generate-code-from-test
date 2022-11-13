package me.shaftesbury.codegenerator;

import static java.util.Objects.requireNonNull;

public class PartialClassFactory {
    public PartialClass create(final IClassName className) {
        requireNonNull(className, "className must not be null");
        return new PartialClass(className, LogicalClass.builder()
                .withName(className)
                .withDefaultConstructor());
    }
}
