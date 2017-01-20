package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.fixed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.FilmAdapterContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.SubscriptionPresenter;
import rx.Observable;
import rx.Subscription;

public abstract class FixedFilmAdapterPresenter extends SubscriptionPresenter implements FilmAdapterContract.Presenter {

    private FilmAdapterContract.View view;
    private List<Subscription> subscriptions = new ArrayList<>();

    @Override
    public FixedFilmAdapterPresenter setView(FilmAdapterContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public FixedFilmAdapterPresenter initialize() {
        if (view == null) {
            throw new IllegalStateException("View is null");
        }

        subscriptions.add(createFilmListObservable()
                .subscribe(films -> view.refreshDataSet(films)));

        return this;
    }

    @Override
    public void onFilmClicked(Long id) {
        RxStore.SELECTED_FILM.onNext(new SelectedFilm(id));
    }

    @Override
    protected Collection<Subscription> getSubscriptions() {
        return subscriptions;
    }

    protected abstract Observable<List<Film>> createFilmListObservable();
}
