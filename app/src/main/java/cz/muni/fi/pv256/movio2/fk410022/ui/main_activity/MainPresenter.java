package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.MenuState;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.sync.SyncIntent;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.SubscriptionPresenter;
import rx.Observable;
import rx.Subscription;

public class MainPresenter extends SubscriptionPresenter implements MainContract.Presenter {
    private List<Subscription> subscriptions = new ArrayList<>();
    private Subscription activeFilmSubscription;

    private MainContract.View view;

    private SyncIntent syncIntent;
    private boolean isTablet;

    public MainPresenter(MainContract.View view, SyncIntent syncIntent) {
        this.view = view;
        this.isTablet = App.isTablet();
        this.syncIntent = syncIntent;
    }

    @Override
    public MainPresenter initialize() {
        final Observable<MenuState> menuState = Observable.combineLatest(RxStore.SHOW_DISCOVER,
                RxStore.SELECTED_FILM, MenuState::new);

        // set menu + default titles
        subscriptions.add(menuState.distinctUntilChanged()
                .subscribe(action -> {
                    view.refreshMenu(!action.showDiscover, action.showDiscover, isTablet ? action.filmSelected : false);
                    refreshTitle(action);
                }));

        // change lists
        subscriptions.add(RxStore.SHOW_DISCOVER.distinctUntilChanged()
                .subscribe(showDiscover -> view.replaceFilmLists(showDiscover)));

        // set film title
        subscriptions.add(RxStore.SELECTED_FILM
                .distinctUntilChanged()
                .subscribe((selectedFilm) -> {
                    if (isTablet) {
                        subscribeToFilmTitle(selectedFilm);
                    } else if (selectedFilm.isSelected()) {
                        view.startDetailActivity();
                    }
                }));

        if (isTablet) {
            // show hide detail
            subscriptions.add(RxStore.SELECTED_FILM.map(SelectedFilm::isSelected)
                    .skipWhile(selected -> !selected)
                    .distinctUntilChanged()
                    .subscribe((selected) -> {
                        view.toggleFilmDetail(selected);
                    }));
        }

        return this;
    }

    @Override
    public Collection<Subscription> getSubscriptions() {
        return subscriptions;
    }

    @Override
    public void onRefreshClicked() {
        syncIntent.sync();
    }

    @Override
    public void onShowFavoritesClicked() {
        RxStore.SHOW_DISCOVER.onNext(false);
        RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);
    }

    @Override
    public void onShowDiscoverClicked() {
        RxStore.SHOW_DISCOVER.onNext(true);
        RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);
    }

    @Override
    public void onCloseDetailClicked() {
        RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY);
    }

    @Override
    public void destroy() {
        if (activeFilmSubscription != null) {
            subscriptions.add(activeFilmSubscription); // set to destroy
        }
        super.destroy();
    }

    private void subscribeToFilmTitle(SelectedFilm selectedFilm) {
        if (activeFilmSubscription != null) {
            activeFilmSubscription.unsubscribe();
        }

        if (selectedFilm.isSelected()) {
            activeFilmSubscription = RxDbObservables.createFilmObservable(selectedFilm.id)
                    .subscribe(this::refreshTitle);
        }
    }

    private void refreshTitle(Film film) {
        if (film != null) {
            view.setTitle(film.getTitle());
        }
    }

    private void refreshTitle(MenuState action) {
        if (action.filmSelected) {
            return;
        }

        if (action.showDiscover) {
            view.setDiscoverTitle();
        } else {
            view.setFavoritesTitle();
        }
    }
}
