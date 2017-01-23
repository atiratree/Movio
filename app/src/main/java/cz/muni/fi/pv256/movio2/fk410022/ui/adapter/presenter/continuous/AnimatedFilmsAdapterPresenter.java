package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadNextPageIntent;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import rx.Observable;

public class AnimatedFilmsAdapterPresenter extends ContinuousFilmAdapterPresenter {

    public AnimatedFilmsAdapterPresenter(DownloadNextPageIntent downloadNextPageIntent) {
        super(downloadNextPageIntent);
    }

    @Override
    protected Observable<List<Film>> createFilmListObservable() {
        return RxDbObservables.createAnimatedFilmsObservable();
    }
}
