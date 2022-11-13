package me.shaftesbury.codegenerator.tokeniser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.ObjectCreationExprMetaModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;

public class Sample {

    ParseResult<CompilationUnit> fn(final InputStream input) throws IOException {
        InputStream in = null;
        ParseResult<CompilationUnit> cu = null;
        try {
//            in = new FileInputStream(filename);
            cu = new JavaParser().parse(input);
        } finally {
//            in.close();
        }
        return cu;
    }

    void t() throws IOException {
        //To invoke the visitor, do this:

        MethodVisitor visitor = new MethodVisitor();
        final ParseResult<CompilationUnit> pr = fn(new ByteArrayInputStream("".getBytes(StandardCharsets.UTF_8)));
        visitor.visit(pr.getResult().get(), null);
    }

    @Test
    @Disabled("syntax not recognised")
    void testClass() throws IOException {
        final String testFunction = "class TestClass { @Test void testFunction() { new A(); }}";
        final ParseResult<CompilationUnit> pr = fn(new ByteArrayInputStream(testFunction.getBytes(StandardCharsets.UTF_8)));
        final CompilationUnit cu = pr.getResult().get();
        new MethodVisitor().visit(cu, null);
    }

    @Test
    @Disabled("syntax not recognised")
    void testAnnotatedFunction() throws IOException {
        final String testFunction = "@Test void testFunction() { new A(); }";
        final ParseResult<CompilationUnit> pr = fn(new ByteArrayInputStream(testFunction.getBytes(StandardCharsets.UTF_8)));
        final CompilationUnit cu = pr.getResult().get();
        new MethodVisitor().visit(cu, null);
    }

    @Test
    @Disabled("syntax not recognised")
    void testFunction() throws IOException {
        final String testFunction = "void testFunction() { new A(); }";
        final ParseResult<CompilationUnit> pr = fn(new ByteArrayInputStream(testFunction.getBytes(StandardCharsets.UTF_8)));
        final CompilationUnit cu = pr.getResult().get();
        new MethodVisitor().visit(cu, null);
    }

    @Test
    void testMethodBlock() {
        final List<IToken> tokens = new ArrayList<>();
        final String testFunction = "{ new A(); }";
        final BlockStmt bs = StaticJavaParser.parseBlock(testFunction);
        new MethodVisitor().visit(bs, tokens);
    }

    public class MethodVisitor extends VoidVisitorAdapter {
        public void visit(MethodDeclaration n, Object arg) {
            // extract method information here.
            // put in to hashmap
            List.<IToken>of(n.getType().isVoidType() ? VOIDRETURNTYPE : ENDCLASS);
            n.getBody();
            n.getName();
            n.getParameters();
        }

        public void visit(final ObjectCreationExpr e, final Object o) {
            final ClassOrInterfaceType type = e.getType();
            final NodeList<Expression> arguments = e.getArguments();
            final Optional<NodeList<BodyDeclaration<?>>> anonymousClassBody = e.getAnonymousClassBody();
            final Optional<Expression> scope = e.getScope();
            final ObjectCreationExprMetaModel metaModel = e.getMetaModel();
            final Optional<NodeList<Type>> typeArguments = e.getTypeArguments();

            final List<IToken> l = (List<IToken>) o;
            l.addAll(
                    List.<IToken>of(NEW, ClassName.of(e.getType().getName().asString())));
        }
    }
}
