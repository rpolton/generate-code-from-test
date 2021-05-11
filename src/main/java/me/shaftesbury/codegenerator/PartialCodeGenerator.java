package me.shaftesbury.codegenerator;

import io.vavr.Tuple2;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;

import java.util.function.Function;

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

    public Function<Tuple2<IClassName, ? extends Traversable<IFunctionName>>, Tuple2<PartialClass, Traversable<IFunctionName>>> initialisePartialClass(final Traversable<PartialClass> constructorsForClassesUsedInTestMethod) {
        return t -> new Tuple2<>(
                initialisePartialClass(constructorsForClassesUsedInTestMethod, t._1),
                t._2);
    }

    private PartialClass initialisePartialClass(final Traversable<PartialClass> constructorsForClassesUsedInTestMethod, final IClassName className) {
        return constructorsForClassesUsedInTestMethod
                .find(ptl -> ptl.getClassName().equals(className))
                .getOrElse(() -> generateCodeForConstructor(className));
    }
}

