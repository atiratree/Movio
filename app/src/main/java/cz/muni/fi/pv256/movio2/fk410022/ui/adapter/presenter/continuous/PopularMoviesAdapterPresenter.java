package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous;

import android.content.Context;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import rx.Observable;

public class PopularMoviesAdapterPresenter extends ContinuousFilmAdapterPresenter {

    public PopularMoviesAdapterPresenter(Context context) {
        super(context);
    }

    @Override
    protected Observable<List<Film>> createFilmListObservable() {
        return RxDbObservables.createPopularFilmsObservable();
    }

    @Override
    protected FilmListType getType() {
        return FilmListType.RECENT_POPULAR_MOVIES;
    }
}
