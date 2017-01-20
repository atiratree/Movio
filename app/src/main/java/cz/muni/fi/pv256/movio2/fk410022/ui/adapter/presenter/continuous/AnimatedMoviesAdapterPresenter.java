package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous;

import android.content.Context;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import rx.Observable;

public class AnimatedMoviesAdapterPresenter extends ContinuousFilmAdapterPresenter {

    public AnimatedMoviesAdapterPresenter(Context context) {
        super(context);
    }

    @Override
    protected Observable<List<Film>> createFilmListObservable() {
        return RxDbObservables.createAnimatedFilmsObservable();
    }

    @Override
    protected FilmListType getType() {
        return FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES;
    }
}
