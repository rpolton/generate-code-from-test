package me.shaftesbury.codegenerator.tokeniser;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.CompactConstructorDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.PatternExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.SwitchExpr;
import com.github.javaparser.ast.expr.TextBlockLiteralExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsDirective;
import com.github.javaparser.ast.modules.ModuleOpensDirective;
import com.github.javaparser.ast.modules.ModuleProvidesDirective;
import com.github.javaparser.ast.modules.ModuleRequiresDirective;
import com.github.javaparser.ast.modules.ModuleUsesDirective;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.LocalRecordDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.stmt.YieldStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VarType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.metamodel.ObjectCreationExprMetaModel;
import me.shaftesbury.codegenerator.Reference;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static me.shaftesbury.codegenerator.tokeniser.Token.ASSIGNMENT;
import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.INT;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.PUBLIC;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.TESTANNOTATION;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;

public class Tokeniser extends VoidVisitorAdapter<List<IToken>> {
    private static class NameTokeniser extends VoidVisitorAdapter<StringBuilder> {
        @Override
        public void visit(final Name n, final StringBuilder arg) {
            n.getQualifier().ifPresent(l -> l.accept(this, arg));
            n.getComment().ifPresent(l -> l.accept(this, arg));
            arg.append(n.getIdentifier());
        }

        @Override
        public void visit(final SimpleName n, final StringBuilder arg) {
            n.getComment().ifPresent(l -> l.accept(this, arg));
            arg.append(n.getIdentifier());
        }
    }

    private final Supplier<NameTokeniser> nameTokeniserFactory = NameTokeniser::new;

    @Override
    public void visit(final AnnotationDeclaration n, final List<IToken> arg) {
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        if (name.toString().equals("Test"))
            arg.add(TESTANNOTATION);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final AnnotationMemberDeclaration n, final List<IToken> arg) {
        n.getDefaultValue().ifPresent(l -> l.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getType().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ArrayAccessExpr n, final List<IToken> arg) {
        n.getIndex().accept(this, arg);
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ArrayCreationExpr n, final List<IToken> arg) {
        n.getElementType().accept(this, arg);
        n.getInitializer().ifPresent(l -> l.accept(this, arg));
        n.getLevels().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ArrayInitializerExpr n, final List<IToken> arg) {
        n.getValues().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final AssertStmt n, final List<IToken> arg) {
        n.getCheck().accept(this, arg);
        n.getMessage().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final AssignExpr n, final List<IToken> arg) {
        n.getTarget().accept(this, arg);
        n.getValue().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final BinaryExpr n, final List<IToken> arg) {
        n.getLeft().accept(this, arg);
        n.getRight().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final BlockComment n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final BlockStmt n, final List<IToken> arg) {
        n.getStatements().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final BooleanLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final BreakStmt n, final List<IToken> arg) {
        n.getLabel().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final CastExpr n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final CatchClause n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getParameter().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final CharLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ClassExpr n, final List<IToken> arg) {
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ClassOrInterfaceDeclaration n, final List<IToken> arg) {
        n.getExtendedTypes().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        arg.add(CLASS);
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        arg.add(ClassName.of(name.toString()));
        arg.addAll(List.of(STARTCLASS));
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
        arg.addAll(List.of(ENDCLASS));
    }

    @Override
    public void visit(final ClassOrInterfaceType n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getScope().ifPresent(l -> l.accept(this, arg));
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final CompilationUnit n, final List<IToken> arg) {
        n.getImports().forEach(p -> p.accept(this, arg));
        n.getModule().ifPresent(l -> l.accept(this, arg));
        n.getPackageDeclaration().ifPresent(l -> l.accept(this, arg));
        n.getTypes().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ConditionalExpr n, final List<IToken> arg) {
        n.getCondition().accept(this, arg);
        n.getElseExpr().accept(this, arg);
        n.getThenExpr().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ConstructorDeclaration n, final List<IToken> arg) {
        n.getModifiers().forEach(m -> m.accept(this, arg));
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        arg.addAll(List.of(FunctionName.of(name.toString()), STARTFUNCTIONPARAMETERS));

        n.getTypeParameters().forEach(p -> p.accept(this, arg));

        arg.addAll(List.of(ENDFUNCTIONPARAMETERS, STARTFUNCTION));

        n.getBody().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getReceiverParameter().ifPresent(p -> p.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(p -> p.accept(this, arg));

        arg.addAll(List.of(ENDFUNCTION));
    }

    @Override
    public void visit(final ContinueStmt n, final List<IToken> arg) {
        n.getLabel().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final DoStmt n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final DoubleLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final EmptyStmt n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final EnclosedExpr n, final List<IToken> arg) {
        n.getInner().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final EnumConstantDeclaration n, final List<IToken> arg) {
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getClassBody().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final EnumDeclaration n, final List<IToken> arg) {
        n.getEntries().forEach(p -> p.accept(this, arg));
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ExplicitConstructorInvocationStmt n, final List<IToken> arg) {
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getExpression().ifPresent(l -> l.accept(this, arg));
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ExpressionStmt n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getComment().ifPresent(c -> c.accept(this, arg));
    }

    @Override
    public void visit(final FieldAccessExpr n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getScope().accept(this, arg);
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final FieldDeclaration n, final List<IToken> arg) {
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ForEachStmt n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getIterable().accept(this, arg);
        n.getVariable().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ForStmt n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getCompare().ifPresent(l -> l.accept(this, arg));
        n.getInitialization().forEach(p -> p.accept(this, arg));
        n.getUpdate().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final IfStmt n, final List<IToken> arg) {
        n.getCondition().accept(this, arg);
        n.getElseStmt().ifPresent(l -> l.accept(this, arg));
        n.getThenStmt().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final InitializerDeclaration n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final InstanceOfExpr n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getPattern().ifPresent(l -> l.accept(this, arg));
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final IntegerLiteralExpr n, final List<IToken> arg) {
        arg.addAll(List.of(ASSIGNMENT, Value.of(n.asNumber().intValue())));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final JavadocComment n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LabeledStmt n, final List<IToken> arg) {
        n.getLabel().accept(this, arg);
        n.getStatement().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LineComment n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LongLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final MarkerAnnotationExpr n, final List<IToken> arg) {
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        if ("Test".equals(name.toString()))
            arg.add(TESTANNOTATION);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final MemberValuePair n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getValue().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final MethodCallExpr n, final List<IToken> arg) {
        n.getArguments().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getScope().ifPresent(l -> l.accept(this, arg));
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final MethodDeclaration n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getType().accept(this, arg);
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        arg.addAll(List.of(FunctionName.of(name.toString()), STARTFUNCTIONPARAMETERS));
        n.getParameters().forEach(p -> p.accept(this, arg));
        arg.add(ENDFUNCTIONPARAMETERS);
        n.getReceiverParameter().ifPresent(l -> l.accept(this, arg));
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        arg.add(STARTFUNCTION);
        n.getBody().ifPresent(l -> l.accept(this, arg));
        arg.add(ENDFUNCTION);
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final NameExpr n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final NormalAnnotationExpr n, final List<IToken> arg) {
        n.getPairs().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final NullLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ObjectCreationExpr n, final List<IToken> arg) {
        final ClassOrInterfaceType type = n.getType();
        final NodeList<Expression> arguments = n.getArguments();
        final Optional<NodeList<BodyDeclaration<?>>> anonymousClassBody = n.getAnonymousClassBody();
        final Optional<Expression> scope = n.getScope();
        final ObjectCreationExprMetaModel metaModel = n.getMetaModel();
        final Optional<NodeList<Type>> typeArguments = n.getTypeArguments();

        arg.addAll(List.of(NEW, ClassName.of(n.getType().getName().asString())));
    }

    @Override
    public void visit(final PackageDeclaration n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final Parameter n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getType().accept(this, arg);
        n.getVarArgsAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final PrimitiveType n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
        if (n.getType().asString().equals("int"))
            arg.add(INT);
    }

    @Override
    public void visit(final Name n, final List<IToken> arg) {
        n.getQualifier().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SimpleName n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ArrayType n, final List<IToken> arg) {
        n.getComponentType().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ArrayCreationLevel n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getDimension().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final IntersectionType n, final List<IToken> arg) {
        n.getElements().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final UnionType n, final List<IToken> arg) {
        n.getElements().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ReturnStmt n, final List<IToken> arg) {
        n.getExpression().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SingleMemberAnnotationExpr n, final List<IToken> arg) {
        n.getMemberValue().accept(this, arg);
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final StringLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SuperExpr n, final List<IToken> arg) {
        n.getTypeName().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SwitchEntry n, final List<IToken> arg) {
        n.getLabels().forEach(p -> p.accept(this, arg));
        n.getStatements().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SwitchStmt n, final List<IToken> arg) {
        n.getEntries().forEach(p -> p.accept(this, arg));
        n.getSelector().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final SynchronizedStmt n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getExpression().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ThisExpr n, final List<IToken> arg) {
        n.getTypeName().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ThrowStmt n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final TryStmt n, final List<IToken> arg) {
        n.getCatchClauses().forEach(p -> p.accept(this, arg));
        n.getFinallyBlock().ifPresent(l -> l.accept(this, arg));
        n.getResources().forEach(p -> p.accept(this, arg));
        n.getTryBlock().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LocalClassDeclarationStmt n, final List<IToken> arg) {
        n.getClassDeclaration().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LocalRecordDeclarationStmt n, final List<IToken> arg) {
        n.getRecordDeclaration().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final TypeParameter n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getTypeBound().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final UnaryExpr n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final UnknownType n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final VariableDeclarationExpr n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getVariables().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final VariableDeclarator n, final List<IToken> arg) {
        n.getType().accept(this, arg);
        final StringBuilder name = new StringBuilder();
        n.getName().accept(nameTokeniserFactory.get(), name);
        arg.add(Reference.of(name.toString()));
        n.getInitializer().ifPresent(l -> l.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final VoidType n, final List<IToken> arg) {
        arg.add(VOIDRETURNTYPE);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final WhileStmt n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getCondition().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final WildcardType n, final List<IToken> arg) {
        n.getExtendedType().ifPresent(l -> l.accept(this, arg));
        n.getSuperType().ifPresent(l -> l.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final LambdaExpr n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final MethodReferenceExpr n, final List<IToken> arg) {
        n.getScope().accept(this, arg);
        n.getTypeArguments().ifPresent(l -> l.forEach(v -> v.accept(this, arg)));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final TypeExpr n, final List<IToken> arg) {
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final NodeList n, final List<IToken> arg) {
        for (Object node : n) {
            ((Node) node).accept(this, arg);
        }
    }

    @Override
    public void visit(final ImportDeclaration n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    public void visit(final ModuleDeclaration n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getDirectives().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    public void visit(final ModuleRequiresDirective n, final List<IToken> arg) {
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ModuleExportsDirective n, final List<IToken> arg) {
        n.getModuleNames().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ModuleProvidesDirective n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getWith().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ModuleUsesDirective n, final List<IToken> arg) {
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ModuleOpensDirective n, final List<IToken> arg) {
        n.getModuleNames().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final UnparsableStmt n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final ReceiverParameter n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final VarType n, final List<IToken> arg) {
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final Modifier n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
        if (n.getKeyword().name().equals("PUBLIC"))
            arg.add(PUBLIC);
    }

    @Override
    public void visit(final SwitchExpr n, final List<IToken> arg) {
        n.getEntries().forEach(p -> p.accept(this, arg));
        n.getSelector().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final TextBlockLiteralExpr n, final List<IToken> arg) {
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final YieldStmt n, final List<IToken> arg) {
        n.getExpression().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final PatternExpr n, final List<IToken> arg) {
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getType().accept(this, arg);
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final RecordDeclaration n, final List<IToken> arg) {
        n.getImplementedTypes().forEach(p -> p.accept(this, arg));
        n.getParameters().forEach(p -> p.accept(this, arg));
        n.getReceiverParameter().ifPresent(l -> l.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getMembers().forEach(p -> p.accept(this, arg));
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }

    @Override
    public void visit(final CompactConstructorDeclaration n, final List<IToken> arg) {
        n.getBody().accept(this, arg);
        n.getModifiers().forEach(p -> p.accept(this, arg));
        n.getName().accept(this, arg);
        n.getThrownExceptions().forEach(p -> p.accept(this, arg));
        n.getTypeParameters().forEach(p -> p.accept(this, arg));
        n.getAnnotations().forEach(p -> p.accept(this, arg));
        n.getComment().ifPresent(l -> l.accept(this, arg));
    }
}

