package me.shaftesbury.codegenerator.model;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.Field;
import me.shaftesbury.codegenerator.IClassName;
import me.shaftesbury.codegenerator.ILogicalFunction;

public interface ILogicalClass {

    IClassName getName();

    Traversable<ILogicalFunction> getMethods();

    Traversable<IConstructor> getConstructors();

    Traversable<Field> getFields();
}
