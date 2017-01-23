package cz.muni.fi.pv256.movio2.fk410022.ui.film_activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.SubscriptionPresenter;
import rx.Subscription;

public class FilmPresenter extends SubscriptionPresenter implements FilmContract.Presenter {
    private FilmContract.View view;
    private List<Subscription> subscriptions = new ArrayList<>();

    private Subscription activeFilmSubscription;

    public FilmPresenter(FilmContract.View view) {
        this.view = view;
    }

    @Override
    public FilmPresenter initialize() {
        subscriptions.add(RxStore.SELECTED_FILM
                .distinctUntilChanged()
                .subscribe(selected -> {
                    if (selected.isSelected()) {
                        subscribeToFilmTitle(selected);
                    } else {
                        view.finish();
                    }
                }));

        return this;
    }

    @Override
    public Collection<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void onBackPressed() {
        onHomePressed();
    }

    @Override
    public void onHomePressed() {
        RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);
    }

    @Override
    public void destroy() {
        subscriptions.add(activeFilmSubscription); // set to destroy
        super.destroy();
    }

    private void subscribeToFilmTitle(SelectedFilm selectedFilm) {
        if (activeFilmSubscription != null) {
            activeFilmSubscription.unsubscribe();
        }

        if (selectedFilm.isSelected()) {
            activeFilmSubscription = RxDbObservables.createFilmObservable(selectedFilm.id)
                    .subscribe(film -> {
                        refreshTitle(film);
                    });
        } else {
            refreshTitle(null);
        }
    }

    private void refreshTitle(Film film) {
        view.setTitle(film == null ? "" : film.getTitle());
    }
}
