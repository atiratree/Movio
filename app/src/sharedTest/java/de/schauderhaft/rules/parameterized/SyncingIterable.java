package de.schauderhaft.rules.parameterized;

import java.util.Iterator;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 */
public class SyncingIterable<T> implements Iterable<T> {

    private final Iterable<T> values;
    private final ValueContainer<T> valueContainer;

    public SyncingIterable(Iterable<T> values,
                           ValueContainer<T> valueContainer) {
        this.values = values;
        this.valueContainer = valueContainer;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private final Iterator<T> delegate = values.iterator();
            private int runIndex;

            @Override
            public boolean hasNext() {
                return delegate.hasNext();
            }

            @Override
            public T next() {
                T next = delegate.next();
                valueContainer.set(next, runIndex++);
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException(
                        "Can't remove from this iterator");
            }
        };
    }
}


