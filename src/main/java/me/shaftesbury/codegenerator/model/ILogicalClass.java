package me.shaftesbury.codegenerator.model;

import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.Fields;
import me.shaftesbury.codegenerator.IClassName;
import me.shaftesbury.codegenerator.ILogicalFunction;

public interface ILogicalClass {

    IClassName getName();

    Traversable<ILogicalFunction> getMethods();

    Traversable<IConstructor> getConstructors();

    Fields getFields();

    String asCode();
}
