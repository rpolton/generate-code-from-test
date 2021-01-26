package me.shaftesbury.codegenerator.model;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.text.ILine;

public interface IMethod {
    IMethod withLines(List<ILine> lines);
}
