package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
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
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class FunctionNameFinderTest {

    @Test
    void findFunctionsUsedInTest() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final IFunctionName f = FunctionName.of("f");
        final Traversable<IToken> tokens = List.of(f);

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly(Tuple.of(className, List.of(f)));
    }

    @Test
    void findFunctionsUsedInTestWhenThereAreTwoFuncsInOneClass() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final IFunctionName f1 = FunctionName.of("f");
        final IFunctionName f2 = FunctionName.of("g");
        final Traversable<IToken> tokens = List.of(className, f1, DOT, f2);

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly(Tuple.of(className, List.of(f1, f2)));
    }

    @Test
    void findFunctionsUsedInTestWhenThereAreTwoClasses() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className1 = ClassName.of("A");
        final IClassName className2 = ClassName.of("B");
        final IFunctionName f1 = FunctionName.of("f");
        final IFunctionName f2 = FunctionName.of("g");
        final Traversable<IToken> tokens = List.of(
                className1, DOT, f1, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON,
                className2, DOT, f2, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON);

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly(Tuple.of(className1, List.of(f1)), Tuple.of(className2, List.of(f2)));
    }

    @Test
    void findFunctionsUsedInTestWhenGivenATestMethod(@Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);

        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly(Tuple.of(className, List.of(doTheThing)));
    }
}