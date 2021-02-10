package me.shaftesbury.codegenerator;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Traversable;
import me.shaftesbury.codegenerator.model.IFunctionName;
import me.shaftesbury.codegenerator.tokeniser.ClassName;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import me.shaftesbury.codegenerator.tokeniser.IToken;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FunctionNameFinderTest {

    @Test
    void findFunctionsUsedInTest() {
        final FunctionNameFinder functionNameFinder = new FunctionNameFinder();
        final ClassName className = ClassName.of("A");
        final Traversable<IClassName> classNamesUsedInTest = List.of(className);
        final FunctionName f = FunctionName.of("f");
        final Traversable<IToken> tokens = List.of(f);

        final Map<IClassName, ? extends Traversable<IFunctionName>> functionsUsedInTest = functionNameFinder.findFunctionsUsedInTest(classNamesUsedInTest, tokens);

        final Traversable<IFunctionName> functions = List.of(f);
        assertThat(functionsUsedInTest).containsExactly(new Tuple2(className, functions));
    }
}