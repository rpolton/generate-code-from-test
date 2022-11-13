package me.shaftesbury.codegenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReferenceTest {

    @Test
    void of() {
        assertThat(Reference.of("A")).isInstanceOf(Reference.class).extracting("name").isEqualTo("A");
    }

    @Test
    void getName() {
        assertThat(Reference.of("A").getName()).isEqualTo("A");
    }
}