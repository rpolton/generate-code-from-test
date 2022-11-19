package me.shaftesbury.codegenerator.tokeniser;

import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.ITestMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Tokeniser implements ITokeniser {

    private final Function<String, CompilationUnit> javaParser;
    private final Supplier<TokeniserImpl> tokeniserImpl;

    public Tokeniser(final Function<String, CompilationUnit> javaParser, final Supplier<TokeniserImpl> tokeniserImpl) {
        this.javaParser = javaParser;
        this.tokeniserImpl = tokeniserImpl;
    }

    @Override
    public Seq<IToken> tokenise(final String text) {
        final CompilationUnit cu = javaParser.apply(text);

        final List<IToken> tokens = new ArrayList<>();
        tokeniserImpl.get().visit(cu.getCU(), tokens);

        return io.vavr.collection.List.ofAll(tokens);
    }

    @Override
    public Seq<IToken> tokenise(final ITestMethod testMethod) {
        return null;
    }
}