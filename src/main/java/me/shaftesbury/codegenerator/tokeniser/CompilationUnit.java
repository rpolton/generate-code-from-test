package me.shaftesbury.codegenerator.tokeniser;

import com.github.javaparser.JavaParser;

public class CompilationUnit {
    private final com.github.javaparser.ast.CompilationUnit cu;

    public CompilationUnit(final com.github.javaparser.ast.CompilationUnit cu) {
        this.cu = cu;
    }

    public CompilationUnit(final String text) {
        cu = new JavaParser().parse(text).getResult().get();
    }

    public static CompilationUnit create(final com.github.javaparser.ast.CompilationUnit cu) {
        return new CompilationUnit(cu);
    }

    public com.github.javaparser.ast.CompilationUnit getCU() {
        return cu;
    }
}
