package me.shaftesbury.codegenerator.tokeniser;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.ObjectCreationExprMetaModel;

import java.util.List;
import java.util.Optional;

import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.PUBLIC;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;

public class Tokeniser extends VoidVisitorAdapter {
    public void visit(MethodDeclaration n, Object arg) {
        // extract method information here.
        // put in to hashmap
        List.<IToken>of(n.getType().isVoidType() ? VOIDRETURNTYPE : ENDCLASS);
        n.getBody();
        n.getName();
        n.getParameters();
    }

//        public void visit(final Expression e, final Object o) {
//            final List<IToken> l = (List<IToken>) o;
//        }

    public void visit(final ClassOrInterfaceDeclaration decl, final Object o) {
        final List<IToken> l = (List<IToken>) o;
        decl.getModifiers().forEach(m -> m.accept(this, o));
        l.addAll(List.of(CLASS, ClassName.of(decl.getName().asString()), STARTCLASS, ENDCLASS));
    }

    public void visit(final Modifier m, final Object o) {
        final List<IToken> l = (List<IToken>) o;
        l.addAll(List.of(PUBLIC));
    }

    public void visit(final ConstructorDeclaration c, final Object o) {
        final List<IToken> l = (List<IToken>) o;
        l.addAll(List.of(FunctionName.of(c.getNameAsString()), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, ENDFUNCTION));
    }

    public void visit(final ObjectCreationExpr e, final Object o) {
        final ClassOrInterfaceType type = e.getType();
        final NodeList<Expression> arguments = e.getArguments();
        final Optional<NodeList<BodyDeclaration<?>>> anonymousClassBody = e.getAnonymousClassBody();
        final Optional<Expression> scope = e.getScope();
        final ObjectCreationExprMetaModel metaModel = e.getMetaModel();
        final Optional<NodeList<Type>> typeArguments = e.getTypeArguments();

        final List<IToken> l = (List<IToken>) o;
        l.addAll(List.<IToken>of(NEW, ClassName.of(e.getType().getName().asString())));
    }
}

