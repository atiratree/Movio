package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.fixed;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import rx.Observable;

public class FavoriteMoviesAdapterPresenter extends FixedFilmAdapterPresenter {

    @Override
    protected Observable<List<Film>> createFilmListObservable() {
        return RxDbObservables.createFavoriteFilmsObservable();
    }
}
