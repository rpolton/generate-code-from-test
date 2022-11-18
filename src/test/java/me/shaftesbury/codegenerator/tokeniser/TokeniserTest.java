package me.shaftesbury.codegenerator.tokeniser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import me.shaftesbury.codegenerator.Reference;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static me.shaftesbury.codegenerator.tokeniser.Token.ASSIGNMENT;
import static me.shaftesbury.codegenerator.tokeniser.Token.CLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.DOT;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.INT;
import static me.shaftesbury.codegenerator.tokeniser.Token.NEW;
import static me.shaftesbury.codegenerator.tokeniser.Token.PUBLIC;
import static me.shaftesbury.codegenerator.tokeniser.Token.SEMICOLON;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTCLASS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTION;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.TESTANNOTATION;
import static me.shaftesbury.codegenerator.tokeniser.Token.VOIDRETURNTYPE;
import static org.assertj.core.api.Assertions.assertThat;

class TokeniserTest {

//    @Test
//    void newTokenAndClassName() {
//        final List<IToken> tokens = new ArrayList<>();
//        final String testFunction = "new A()";
//        final Expression exp = StaticJavaParser.parseExpression(testFunction);
//
//        new MethodVisitor().visit(exp, tokens);
//
//        assertThat(tokens).containsExactly(NEW, ClassName.of("A"));
//    }

//    @Test
//    void testMethodBlockContainingDefaultConstructor() {
//        final List<IToken> tokens = new ArrayList<>();
//        final String testFunction = "{ new A(); }";
//        final BlockStmt bs = StaticJavaParser.parseBlock(testFunction);
//
//        new Tokeniser().visit(bs, tokens);
//
//        assertThat(tokens).containsExactly(NEW, ClassName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);
//    }

    @Test
    void simplestClass() {
        final List<IToken> tokens = new ArrayList<>();
        final String testMethodBody = "public class A { }";

        final ParseResult<CompilationUnit> pr = new JavaParser().parse(testMethodBody);
        final CompilationUnit cu = pr.getResult().get();

        new Tokeniser().visit(cu, tokens);

        assertThat(tokens).containsExactly(PUBLIC, CLASS, ClassName.of("A"), STARTCLASS, ENDCLASS);
    }

    @Test
    void simpleClass() {
        final List<IToken> tokens = new ArrayList<>();
        final String testMethodBody = "public class A { public A(){} }";

        final ParseResult<CompilationUnit> pr = new JavaParser().parse(testMethodBody);
        final CompilationUnit cu = pr.getResult().get();

        new Tokeniser().visit(cu, tokens);

        assertThat(tokens).containsExactly(PUBLIC, CLASS, ClassName.of("A"), STARTCLASS, PUBLIC, FunctionName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, ENDFUNCTION, ENDCLASS);
    }

    @Test
    void simpleClassWithSimpleConstructor() {
        final List<IToken> tokens = new ArrayList<>();
        final String testMethodBody = "public class A { public A(){ final int i = 1;} }";

        final ParseResult<CompilationUnit> pr = new JavaParser().parse(testMethodBody);
        final CompilationUnit cu = pr.getResult().get();

        new Tokeniser().visit(cu, tokens);

        assertThat(tokens).containsExactly(PUBLIC, CLASS, ClassName.of("A"), STARTCLASS, PUBLIC, FunctionName.of("A"), STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, INT, Reference.of("i"), ASSIGNMENT, Value.of(1), ENDFUNCTION, ENDCLASS);
    }

    //    @Test
//    void assignmentFunction() {
//        final String testMethodBody = "final int a = 10;";
//
//        final ITokeniser tokeniser = new Tokeniser();
//
//        final Traversable<IToken> tokens = tokeniser.tokenise(testMethodBody);
//
//        assertThat(tokens.toJavaList()).containsExactly(FINAL, INT, Reference.of("a"), ASSIGNMENT, Value.of(10), SEMICOLON);
//    }

    @Test
    void testFunctionContainingFunctionCall() {
        final List<IToken> tokens = new ArrayList<>();
        final String testClass = "public class ATest { @Test void testFunction() { new A().doTheThing(); }}";
        final java.util.List<IToken> expectedTokens = io.vavr.collection.List.of(
                        PUBLIC, CLASS, ClassName.of("ATest"), STARTCLASS,
                        TESTANNOTATION, VOIDRETURNTYPE, FunctionName.of("testFunction"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, STARTFUNCTION, NEW, FunctionName.of("A"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, DOT, FunctionName.of("doTheThing"),
                        STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON, ENDFUNCTION, ENDCLASS)
                .toJavaList();
        final ParseResult<CompilationUnit> pr = new JavaParser().parse(testClass);
        final CompilationUnit cu = pr.getResult().get();

        new Tokeniser().visit(cu, tokens);

        assertThat(tokens).containsExactlyElementsOf(expectedTokens);
    }
}

