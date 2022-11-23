package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static me.shaftesbury.codegenerator.tokeniser.Token.DOT;
import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.SEMICOLON;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static org.assertj.vavr.api.VavrAssertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FunctionNameFinderTest {

    @Test
    void onlyAClass() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final Seq<IToken> tokens = List.of(className);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).isEmpty();
    }

    @Test
    void classAndDot() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final Seq<IToken> tokens = List.of(className, DOT);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).isEmpty();
    }

    @Test
    void findFunctionsUsedInTest() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final IFunctionName f = FunctionName.of("f");
        final Seq<IToken> tokens = List.of(className, DOT, f, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(f)));
    }

    @Test
    void findFunctionsUsedInTestWhenThereAreTwoFuncsInOneClass() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final IFunctionName f1 = FunctionName.of("f");
        final IFunctionName f2 = FunctionName.of("g");
        final Seq<IToken> tokens = List.of(className, DOT, f1, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, DOT, f2, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(f1, f2)));
    }

    @Test
        // This needs to be able to infer the return type of f() in order to derive the second class name
    void findFunctionsUsedInTestWhenThereAreTwoClasses() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName c1 = ClassName.of("A");
        final IClassName c2 = ClassName.of("B");
        final IFunctionName f = FunctionName.of("f");
        final IFunctionName g = FunctionName.of("g");
        final Seq<IToken> tokens = List.of(
                c1, DOT, f, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON,
                c2, DOT, g, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(c1, List.of(f)), (Tuple2) Tuple.of(c2, List.of(g)));
    }

    @Test
    void findFunctionsUsedInTestWhenGivenATestMethod(@Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);

        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(doTheThing)));
    }
}