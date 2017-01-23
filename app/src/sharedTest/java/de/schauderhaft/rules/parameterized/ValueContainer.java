package de.schauderhaft.rules.parameterized;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 */
public class ValueContainer<T> {
    private T value = null;
    private int runIndex;

    public void set(T t, int runIndex) {
        value = t;
        this.runIndex = runIndex;
    }

    public T get() {
        return value;
    }

    public int getRunIndex() {
        return runIndex;
    }
}
