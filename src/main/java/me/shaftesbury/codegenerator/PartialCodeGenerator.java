package me.shaftesbury.codegenerator;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;

public class PartialCodeGenerator {
    public Traversable<PartialClass> generateConstructorCodeForClasses(final Traversable<IClassName> classesUsedInTestMethod) {
        return classesUsedInTestMethod.map(this::generateCodeForConstructor);
    }

    public PartialClass generateCodeForConstructor(final IClassName className) {
        return new PartialClass(className, LogicalClass.builder()
                .withName(className)
                .withDefaultConstructor());
    }

    public ILogicalClass generateCodeForClass(final PartialClass partialClass, final Traversable<IFunctionName> functionNames) {
        return functionNames
                .foldRight(partialClass.getBuilder(), (name, br) -> br.withMethod(LogicalFunction.builder().withName(name).build()))
                .build();
    }
}

