package me.shaftesbury.codegenerator;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

@MockitoSettings
class PartialClassFactoryTest {

    @Test
    void preconditions() {
        assertThatNullPointerException().isThrownBy(() -> new PartialClassFactory().create(null)).withMessage("className must not be null");
    }

    @Test
    void create(@Mock final IClassName className) {
        final PartialClass partialClass = new PartialClassFactory().create(className);

        assertThat(partialClass)
                .satisfies(__ -> {
                    assertThat(partialClass).extracting(PartialClass::getClassName).isEqualTo(className);
//                    assertThat(partialClass).extracting(PartialClass::getBuilder).isEqualTo(...)
                });
    }
}