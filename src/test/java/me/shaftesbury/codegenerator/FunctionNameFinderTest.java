package me.shaftesbury.codegenerator;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Seq;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import me.shaftesbury.codegenerator.tokeniser.Token;
import me.shaftesbury.codegenerator.tokeniser.Value;
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
    void onlyAClass(@Mock final IClassName className) {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final Seq<IToken> tokens = List.of(className);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).isEmpty();
    }

    @Test
    void classAndDot(@Mock final IClassName className) {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final Seq<IToken> tokens = List.of(className, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, DOT);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).isEmpty();
    }

    @Test
    void findStaticFunctionsUsedInTest(@Mock final IClassName className, @Mock final IFunctionName f) {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final Seq<IToken> tokens = List.of(className, DOT, f, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(f)));
    }

//    @Test
//    void findFunctionsUsedInTest() {
//        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
//        final Reference refName = Reference.of("a"); // Hmm, we need a "A a = new A();" somewhere first
//        final IFunctionName f = FunctionName.of("f");
//        final Seq<IToken> tokens = List.of(refName, DOT, f, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);
//
//        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);
//
//        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(refName, List.of(f)));
//    }

    @Test
    void findStaticFunctionFollowedByFunction(@Mock final IClassName className,
                                              @Mock final IFunctionName f1, @Mock final IFunctionName f2) {
        // This needs to be able to infer the return type of f() in order to derive the second class name
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final Seq<IToken> tokens = List.of(className, DOT, f1, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, DOT, f2, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(f1, f2)));
    }

    @Test
    void findFunctionsUsedInTestWhenThereAreTwoClasses(@Mock final IClassName c1, @Mock final IClassName c2,
                                                       @Mock final IFunctionName f, @Mock final IFunctionName g) {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final Seq<IToken> tokens = List.of(
                c1, DOT, f, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON,
                c2, DOT, g, STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, SEMICOLON);

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(c1, List.of(f)), (Tuple2) Tuple.of(c2, List.of(g)));
    }

    @Test
    void findFunctionsUsedInTestWhenGivenATestMethod(@Mock final IClassName className, @Mock final IFunctionName doTheThing) {
//        @Test
//        void test(){
//        assertThat(new C().doTheThing()).isEqualTo(10);
//        }
        final List<IToken> tokens = List.of(Token.TESTANNOTATION, Token.VOIDRETURNTYPE, FunctionName.of("test"),
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.STARTFUNCTION,
                FunctionName.of("assertThat"), STARTFUNCTIONPARAMETERS,
                Token.NEW, className,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS, Token.DOT, doTheThing,
                STARTFUNCTIONPARAMETERS, ENDFUNCTIONPARAMETERS,
                ENDFUNCTIONPARAMETERS, DOT, FunctionName.of("isEqualTo"), STARTFUNCTIONPARAMETERS, Value.of(10),
                ENDFUNCTIONPARAMETERS,
                Token.SEMICOLON, Token.ENDFUNCTION);

        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();

        final Map<IClassName, ? extends Seq<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsed(tokens);

        assertThat(functionsUsedInTest).containsExactly((Tuple2) Tuple.of(className, List.of(doTheThing)));
    }
}