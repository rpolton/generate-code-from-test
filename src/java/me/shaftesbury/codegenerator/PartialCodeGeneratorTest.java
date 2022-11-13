package me.shaftesbury.codegenerator;

import io.vavr.collection.List;
import io.vavr.collection.Traversable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartialCodeGeneratorTest {

    @Test
    void generateConstructorCodeForClassesGivenClassNameReturnsPartialClassContainingTheDefaultConstructor(@Mock final IClassName className,
                                                                                                           @Mock final PartialClassFactory partialClassFactory, @Mock final PartialClass partialClass) {

        when(partialClassFactory.create(className)).thenReturn(partialClass);
        final PartialCodeGenerator generator = new PartialCodeGenerator(partialClassFactory);

        final Traversable<PartialClass> partialClasses = generator.generateConstructorCodeForClasses(List.of(className));

        assertThat(partialClasses).containsExactly(partialClass);
    }

    @Test
    void generateCodeForConstructor() {
        fail("not implemented");
    }

    @Test
    void generateCodeForClass() {
        fail("not implemented");
    }

    @Test
    void initialisePartialClass() {
        fail("not implemented");
    }
}