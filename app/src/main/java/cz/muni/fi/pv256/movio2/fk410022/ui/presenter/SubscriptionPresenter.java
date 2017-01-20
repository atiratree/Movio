package cz.muni.fi.pv256.movio2.fk410022.ui.presenter;

import com.annimon.stream.Stream;

import java.util.Collection;

import rx.Subscription;

public abstract class SubscriptionPresenter implements BasePresenter {

    protected abstract Collection<Subscription> getSubscriptions();

    @Override
    public void destroy() {
        final Collection<Subscription> subscriptions = getSubscriptions();
        if (subscriptions == null) {
            return;
        }

        Stream.of(subscriptions)
                .filter(s -> s != null && !s.isUnsubscribed())
                .forEach(Subscription::unsubscribe);
        subscriptions.clear();
    }
}
