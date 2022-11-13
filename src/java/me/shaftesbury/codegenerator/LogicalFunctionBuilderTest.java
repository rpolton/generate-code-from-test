package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import me.shaftesbury.codegenerator.tokeniser.FunctionName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LogicalFunctionBuilderTest {

    @Test
    void withBody() {
        final FunctionName fnName = FunctionName.of("fnName");
        final ReturnType returnType = ReturnType.VOID;
        final ILogicalFunction fn = LogicalFunction.builder().withName(fnName).havingNoParameters().returning(returnType).withBody(List.empty()).build();

        assertThat(fn)
                .extracting(ILogicalFunction::getName, ILogicalFunction::getParameters, ILogicalFunction::getReturnType, ILogicalFunction::getBody)
                .containsExactly(fnName, List.empty(), ReturnType.VOID, List.empty());
    }
}