package cz.muni.fi.pv256.movio2.fk410022.ui.adapter;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewChildAttachStateChangeEvent;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.presenter.BasePresenter;
import rx.Observable;

public interface FilmAdapterContract {

    interface View {
        Observable<RecyclerViewChildAttachStateChangeEvent> createAttachViewObservable();

        void refreshDataSet(List<Film> dataSet);
    }

    interface Presenter extends BasePresenter {

        Presenter setView(View view);

        void onFilmClicked(Long id);
    }
}
