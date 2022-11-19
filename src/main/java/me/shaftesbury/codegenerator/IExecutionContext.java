package me.shaftesbury.codegenerator;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.imported.RuntimeCompiler;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.model.ILogicalClass;

public interface IExecutionContext {
    Seq<ILogicalClass> getClasses();

    RuntimeCompiler getRuntimeCompiler();

    // ToDo remove this in favour of allFns(LogCls)
    boolean allFunctionsAreInTheContext(IClassName iClassName, Seq<IFunctionName> iFunctionNames);

    boolean allFunctionsAreInTheContext(ILogicalClass cls);
}
