package me.shaftesbury.codegenerator;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static me.shaftesbury.codegenerator.tokeniser.Token.ENDFUNCTIONPARAMETERS;
import static me.shaftesbury.codegenerator.tokeniser.Token.STARTFUNCTIONPARAMETERS;
import static org.assertj.core.api.Assertions.assertThat;

@MockitoSettings
class FunctionNameFinderTest {

    @Test
    void findFunctionsUsedInTest() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final IClassName className = ClassName.of("A");
        final IFunctionName f = FunctionName.of("f");
        final Traversable<IToken> tokens = List.of(f);

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        // Todo why won't Tuple2<> compile here?
        assertThat(functionsUsedInTest).containsExactly(new Tuple2(className, List.of(f)));
    }

    @Test
    void findFunctionsUsedInTestWhenPresentedWithALotOfTokens(@Mock final IClassName className, @Mock final IFunctionName doTheThing) {
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION, Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.SEMICOLON, Token.ENDFUNCTION);

        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        // Todo why won't Tuple2<> compile here?
        assertThat(functionsUsedInTest).containsExactly(new Tuple2(className, List.of(doTheThing)));
    }
}