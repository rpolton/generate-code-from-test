package me.shaftesbury.codegenerator;

import io.vavr.collection.HashSet;
import me.shaftesbury.codegenerator.model.IConstructor;
import me.shaftesbury.codegenerator.model.ILogicalClass;
import org.junit.jupiter.api.Test;

import static me.shaftesbury.codegenerator.model.Constructor.DEFAULT_CONSTRUCTOR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class LogicalClassTest {

    @Test
    void ofReturnsLogicalClass() {
        final ILogicalClass of = LogicalClass.of(mock(IClassName.class));
        assertThat(of).isInstanceOf(LogicalClass.class);
    }

    @Test
    void withName() {
        final IClassName className = mock(IClassName.class);
        final ILogicalClass iLogicalClass = LogicalClass.builder().withName(className).build();
        assertThat(iLogicalClass).extracting(ILogicalClass::getName).isEqualTo(className);
    }

    @Test
    void withDefaultConstructor() {
        final IClassName className = mock(IClassName.class);
        final ILogicalClass iLogicalClass = LogicalClass.builder().withDefaultConstructor().build();
        assertThat(iLogicalClass).extracting(ILogicalClass::getConstructors).isEqualTo(HashSet.of(DEFAULT_CONSTRUCTOR));
    }

    @Test
    void withConstructor() {
        final IClassName className = mock(IClassName.class);
        final IConstructor constructor = mock(IConstructor.class);
        final ILogicalClass iLogicalClass = LogicalClass.builder().withConstructor(constructor).build();
        assertThat(iLogicalClass).extracting(ILogicalClass::getConstructors).isEqualTo(HashSet.of(constructor));
    }

    @Test
    void withFunction() {
        final IClassName className = mock(IClassName.class);
        final ILogicalFunction function = mock(ILogicalFunction.class);
        final ILogicalClass iLogicalClass = LogicalClass.builder().withMethod(function).build();
        assertThat(iLogicalClass).extracting(ILogicalClass::getMethods).isEqualTo(HashSet.of(function));
    }

    @Test
    void withField() {
        final IClassName className = mock(IClassName.class);
        final ILogicalFunction function = mock(ILogicalFunction.class);
        final Field field = mock(Field.class);
        final ILogicalClass iLogicalClass = LogicalClass.builder().withFields(Fields.builder().withField(field).build()).build();
        assertThat(iLogicalClass).extracting(ILogicalClass::getFields).isEqualTo(HashSet.of(field));
    }

//    @Test
//    void build() {
//        final IClassName className = mock(IClassName.class);
//        final ILogicalClass iLogicalClass = LogicalClass.builder().withName(className).build();
//        assertThat(iLogicalClass).extracting(ILogicalClass::getName).isEqualTo(className);
//    }
}