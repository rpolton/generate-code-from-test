Feature: automatically generate code given unit tests that describe how the application should behave

  Scenario: run a test when the context contains the class code
    Given a unit test
    """
    //import org.junit.jupiter.api.Test;
    //@Test
    void test() {
        new A();
    }
    """
    And an execution context containing class A and its definition
    """
    public class A { }
    """
    When I run the test in the supplied context
    Then the test should execute without errors

  Scenario: run a test when the context does not contain the class code, so we have to generate it and add it to the context
    Given a unit test
    """
    @Test
    void test() {
        new A();
    }
    """
    And an empty execution context
    When I run the test in the supplied context
    Then the application should generate code
    """
    public class A { }
    """
    And the test should execute without errors