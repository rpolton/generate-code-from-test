package me.shaftesbury.codegenerator;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class NewStatementTest implements ExecutableStatement {
    interface TheClass {
    }

    class NewStatement {
        public TheClass apply(final Class<TheClass> createThis) {
            return null;
        }
    }

    @Test
    void createANewObject(){
        final NewStatement newStatement = new NewStatement();

        final TheClass theClass = newStatement.apply(TheClass.class);

        assertThat(theClass).isNotNull();
    }
}
