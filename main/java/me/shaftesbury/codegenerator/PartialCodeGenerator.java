package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class PartialCodeGenerator {
    private final PartialClassFactory partialClassFactory;

    public PartialCodeGenerator(final PartialClassFactory partialClassFactory) {
        this.partialClassFactory = requireNonNull(partialClassFactory, "partialClassFactory must not be null");
    }

    public Traversable<PartialClass> generateConstructorCodeForClasses(final Traversable<IClassName> classesUsedInTestMethod) {
        return classesUsedInTestMethod.map(this::generateCodeForConstructor);
    }

    public PartialClass generateCodeForConstructor(final IClassName className) {
        return partialClassFactory.create(className);
    }

    public ILogicalClass generateCodeForClass(final PartialClass partialClass, final Traversable<IFunctionName> functionNames) {
        return functionNames
                .foldRight(partialClass.getBuilder(), (name, br) -> br.withMethod(LogicalFunction.builder().withName(name).build()))
                .build();
    }

    public Function<Tuple2<IClassName, ? extends Traversable<IFunctionName>>, Tuple2<PartialClass, Traversable<IFunctionName>>> initialisePartialClass(final Traversable<PartialClass> constructorsForClassesUsedInTestMethod) {
        return t -> Tuple.of(
                initialisePartialClass(constructorsForClassesUsedInTestMethod, t._1),
                t._2);
    }

    private PartialClass initialisePartialClass(final Traversable<PartialClass> constructorsForClassesUsedInTestMethod, final IClassName className) {
        return constructorsForClassesUsedInTestMethod
                .find(ptl -> ptl.getClassName().equals(className))
                .getOrElse(() -> generateCodeForConstructor(className));
    }
}

