package me.shaftesbury.codegenerator.text;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LineOfCodeTest {
    @Test
    void constructorThrows() {
        assertThat(assertThrows(NullPointerException.class, () -> new LineOfCode(null))).hasMessage("line must not be null");
    }
}

// NEW token
// NewStatement (A.class)
// generate class A {}
// run test
// execute A constructor
// run test