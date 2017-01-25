package cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.SubscriptionPresenter;
import rx.Subscription;
import timber.log.Timber;

public class FilmDetailPresenter extends SubscriptionPresenter implements FilmDetailContract.Presenter {
    private FilmDetailContract.View view;
    private List<Subscription> subscriptions = new ArrayList<>();

    private Subscription activeFilmSubscription;
    private Subscription activeFavoriteSubscription;

    private Favorite favorite;
    private Film film;

    public FilmDetailPresenter(FilmDetailContract.View view) {
        this.view = view;
    }

    @Override
    public FilmDetailPresenter initialize() {
        subscriptions.add(RxStore.SELECTED_FILM
                .distinctUntilChanged()
                .subscribe(this::subscribeToFilmAndFavorite));

        return this;
    }

    @Override
    public Collection<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void destroy() {
        subscriptions.add(activeFilmSubscription); // set to destroy
        subscriptions.add(activeFavoriteSubscription);
        super.destroy();
    }

    @Override
    public void toggleFavorite() {
        if (favorite != null) {
            favorite.delete();
            Timber.i("Deleted favorite %s", favorite.getFilm().getTitle());
            favorite = null;
        } else if (film != null) {
            favorite = new Favorite();
            favorite.setFavorite(true);
            favorite.setFilm(film);
            favorite.save();
            Timber.i("Added favorite %s", favorite.getFilm().getTitle());
        }
    }

    @Override
    public void onSwipeRight() {
        RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);
    }

    private void subscribeToFilmAndFavorite(SelectedFilm selectedFilm) {
        if (activeFilmSubscription != null) {
            activeFilmSubscription.unsubscribe();
        }
        if (activeFavoriteSubscription != null) {
            activeFavoriteSubscription.unsubscribe();
        }

        if (selectedFilm.isSelected()) {
            activeFilmSubscription = RxDbObservables.createFilmObservable(selectedFilm.id)
                    .subscribe(film -> {
                        this.film = film;
                        view.showMovie(film);
                    });

            activeFavoriteSubscription = RxDbObservables.createFavoriteObservable(selectedFilm.id)
                    .subscribe(favorite -> {
                        this.favorite = favorite;
                        view.showFavorite(favorite != null && favorite.isFavorite());
                    });
        }
    }
}
