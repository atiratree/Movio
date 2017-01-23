package de.schauderhaft.rules.parameterized;

import static java.util.Arrays.asList;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 */
public final class GeneratorFactory {
    private GeneratorFactory() {
    }

    public static <T> Generator<T> list(T... values) {
        return new ListGenerator<T>(asList(values));
    }
}
