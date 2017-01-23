package cz.muni.fi.pv256.movio2.fk410022.utils;

import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.verification.api.VerificationData;
import org.mockito.invocation.Invocation;
import org.mockito.verification.VerificationMode;

import java.util.List;
import java.util.ListIterator;

public class LastMethodCall implements VerificationMode {
    public void verify(VerificationData data) {
        InvocationMatcher matcher = data.getWanted();

        final List<Invocation> invocations = data.getAllInvocations();
        final ListIterator<Invocation> listIterator = invocations.listIterator(invocations.size());
        ;

        while (listIterator.hasPrevious()) {
            final Invocation previous = listIterator.previous();
            if (previous.getMethod().equals(matcher.getMethod())) {
                if (matcher.matches(previous)) {
                    return;
                } else {
                    throw new MockitoException("Last call did not match!" +
                            "\nWanted: " + matcher.toString() + "\n But got: " + previous.toString());
                }
            }
        }

        throw new MockitoException("Did not gat any last parametrized call!" +
                "\nWanted: " + matcher.toString());
    }
}
