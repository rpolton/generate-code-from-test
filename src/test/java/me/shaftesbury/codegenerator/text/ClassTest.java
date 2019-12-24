package me.shaftesbury.codegenerator.text;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class ClassTest {
    @Test
    void constructor() {
        final Class aClass = new Class("name", "body");

        assertAll(
                () -> assertThat(aClass.getName()).isEqualTo("name"),
                () -> assertThat(aClass.getBody()).isEqualTo("body")
        );
    }
}