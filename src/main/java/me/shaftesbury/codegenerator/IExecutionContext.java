package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.text.Class;

public interface IExecutionContext {
    Seq<Class> getContext();
    RuntimeCompiler getRuntimeCompiler();
}
