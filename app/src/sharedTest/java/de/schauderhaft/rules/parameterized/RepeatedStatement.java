package de.schauderhaft.rules.parameterized;

import android.annotation.SuppressLint;
import android.support.v4.util.Pair;

import org.junit.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Source: https://github.com/schauder/parameterizedTestsWithRules
 * <p>
 * This class is modified to write better error messages, you should use Arrays to get nice output
 */
class RepeatedStatement<T> extends Statement {

    private final Statement test;
    private final Iterable<T> values;

    private final AccessibleErrorCollector errorCollector = new AccessibleErrorCollector();

    public RepeatedStatement(Statement test, Iterable<T> values
    ) {
        this.test = test;
        this.values = values;
    }

    @SuppressLint({"NewApi", "DefaultLocale"})
    @Override
    public void evaluate() throws Throwable {
        List<Pair<String, Throwable>> errors = new ArrayList<>();
        final Iterator<T> iterator = values.iterator();
        Set<Throwable> throwables = new HashSet<>();
        T value = null;
        int run = -1;

        while (iterator.hasNext()) {
            try {
                value = iterator.next();
                run++;
                test.evaluate();
            } catch (Throwable t) {
                if (!throwables.contains(t)) {
                    throwables.add(t);
                    String message = String.format("Run %d failed with values=%s", run,
                            (value instanceof Object[]) ? Arrays.toString((Object[]) value) : value.toString());
                    errorCollector.addError(new MessageException(message, t));
                }
            }
        }

        try {
            errorCollector.verify();
        } catch (MultipleFailureException exception) {
            final Iterator<Throwable> failureIterator = exception.getFailures().iterator();

            while (failureIterator.hasNext()) {
                Throwable failure = failureIterator.next();

                if (failureIterator.hasNext()) { // write all exceptions except the last one
                    logThrowable(failure);
                } else { //throw last one to notify test suite about failure
                    throwAndLog(failure);
                }
            }
        } catch (Throwable t) {
            throwAndLog(t);
        }
    }

    private static void logThrowable(Throwable t) throws Throwable {
        if (t instanceof MessageException) {
            logMessageException(t);
        }
        t = t.getCause();

        StringWriter writer = new StringWriter();
        t.printStackTrace(new PrintWriter(writer));
        System.err.println(writer.toString());
    }

    private static void throwAndLog(Throwable t) throws Throwable {
        logMessageException(t);
        throw t;
    }

    private static void logMessageException(Throwable x) {
        if (x instanceof MessageException) { // junit strips the message so log it
            System.err.println(String.format("\n\n%s\n", x.getMessage()));
        }
    }
}
