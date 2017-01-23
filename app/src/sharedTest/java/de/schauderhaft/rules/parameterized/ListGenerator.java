package de.schauderhaft.rules.parameterized;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 */
public class ListGenerator<T> implements Generator<T> {

    private final ValueContainer<T> currentValue = new ValueContainer<T>();

    private final Iterable<T> values;

    public ListGenerator(Iterable<T> values) {
        this.values = values;
    }

    @Override
    public T value() {
        return currentValue.get();
    }

    @Override
    public int runIndex() {
        return currentValue.getRunIndex();
    }

    @Override
    public Statement apply(Statement test, Description description) {
        return new RepeatedStatement<>(test, new SyncingIterable<>(values, currentValue));
    }
}
