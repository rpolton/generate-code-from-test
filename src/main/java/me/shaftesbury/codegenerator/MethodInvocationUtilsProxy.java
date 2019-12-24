package me.shaftesbury.codegenerator;

import io.vavr.control.Option;

import java.lang.reflect.Method;

import static io.vavr.control.Option.none;
import static io.vavr.control.Option.some;

public class MethodInvocationUtilsProxy {
    public Option<Boolean> invokeTestMethod(final IExecutionContext executionContext, final String testClassName, final String testMethodName) throws IllegalAccessException {
        final Class<?> testClass = executionContext.getRuntimeCompiler().getCompiledClass(testClassName);
        return findFirstMatchingMethod(testClass, testMethodName).map(m -> {
            try {
                m.invoke(testClass);
                return true;
            } catch (final Exception e) {
                return false;
            }
        });
    }

    private static Option<Method> findFirstMatchingMethod(final Class<?> c, final String methodName) {
        final Method methods[] = c.getDeclaredMethods();
        for (final Method m : methods) {
            if (m.getName().equals(methodName)) {
                return some(m);
            }
        }
        return none();
    }
}
