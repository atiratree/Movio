package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadNextPageIntent;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.FilmAdapterContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.SubscriptionPresenter;
import rx.Observable;
import rx.Subscription;

public abstract class ContinuousFilmAdapterPresenter extends SubscriptionPresenter implements FilmAdapterContract.Presenter {

    public static final int FIRST_NEW_DOWNLOAD_PAGE_POSITION = 40; // should be FilmListType default pages * 20 at maximum
    private static final int START_DOWNLOAD_BEFORE_LIST_END = 40;

    private DownloadNextPageIntent nextPageIntent;
    private FilmAdapterContract.View view;
    private List<Subscription> subscriptions = new ArrayList<>();

    public ContinuousFilmAdapterPresenter(Context context) {
        nextPageIntent = new DownloadNextPageIntent(context, getType());
    }

    @Override
    public ContinuousFilmAdapterPresenter setView(FilmAdapterContract.View view) {
        this.view = view;
        return this;
    }

    @Override
    public ContinuousFilmAdapterPresenter initialize() {
        if (view == null) {
            throw new IllegalStateException("View is null");
        }

        subscriptions.add(createFilmListObservable()
                .subscribe(films -> view.refreshDataSet(films))
        );

        subscriptions.add(view.createAttachViewObservable()
                .filter(v -> {
                    int position = v.view().getChildAdapterPosition(v.child());
                    return position >= FIRST_NEW_DOWNLOAD_PAGE_POSITION - 1 &&
                            position >= v.view().getAdapter().getItemCount() - START_DOWNLOAD_BEFORE_LIST_END;
                })
                .subscribe(v -> nextPageIntent.downloadNewMovies())
        );

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

    protected abstract FilmListType getType();
}
