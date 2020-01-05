package me.shaftesbury.codegenerator.text;

import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ClassTest {
    @Test
    void constructor() {
        final Class aClass = new Class("name", List.of("body"), "");

        assertAll(
                () -> assertThat(aClass.getName()).isEqualTo("name"),
                () -> assertThat(aClass.getPublicFunctions()).isEqualTo(List.of("body"))
        );
    }
}