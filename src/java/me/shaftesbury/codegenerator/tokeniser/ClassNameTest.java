package me.shaftesbury.codegenerator.tokeniser;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassNameTest {

    @Test
    void of() {
        final ClassName name = ClassName.of("name");
        assertThat(name).isNotNull();
    }

    @Test
    void getName() {
        final ClassName name = ClassName.of("name");
        assertThat(name).extracting(ClassName::getName).isEqualTo("name");
    }

    @Test
    void testToString() {
        final ClassName name = ClassName.of("name");
        final String s = name.toString();
        assertThat(s).isEqualTo("ClassName{name='name'}");
    }

    @Test
    void testEquals() {
        final ClassName name1 = ClassName.of("name");
        final ClassName name2 = ClassName.of("name");
        assertThat(name1).isEqualTo(name2);
    }

    @Test
    void testHashCode() {
        final ClassName name = ClassName.of("name");
        final int s = name.hashCode();
    }
}