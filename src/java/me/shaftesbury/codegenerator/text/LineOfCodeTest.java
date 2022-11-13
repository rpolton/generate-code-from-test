package me.shaftesbury.codegenerator.text;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@Disabled
class LineOfCodeTest {
    @Test
    void constructorThrows() {
        assertThatNullPointerException().isThrownBy(() -> new LineOfCode(null))
                .withMessage("line must not be null");
    }
}

// NEW token
// NewStatement (A.class)
// generate class A {}
// run test
// execute A constructor
// run test