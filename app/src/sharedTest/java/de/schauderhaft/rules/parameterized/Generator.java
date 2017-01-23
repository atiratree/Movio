package de.schauderhaft.rules.parameterized;

import org.junit.rules.TestRule;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 */
public interface Generator<T> extends TestRule {
    public T value();

    public int runIndex();
}
