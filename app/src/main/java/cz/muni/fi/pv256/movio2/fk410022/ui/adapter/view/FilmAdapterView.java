package cz.muni.fi.pv256.movio2.fk410022.ui.adapter.view;

import android.support.v7.widget.RecyclerView;

import com.jakewharton.rxbinding.support.v7.widget.RecyclerViewChildAttachStateChangeEvent;
import com.jakewharton.rxbinding.support.v7.widget.RxRecyclerView;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.FilmAdapterContract;
import rx.Observable;

public class FilmAdapterView implements FilmAdapterContract.View {

    private RecyclerView recyclerView;

    public FilmAdapterView(RecyclerView recyclerView) {
        cz.muni.fi.pv256.movio2.fk410022.util.Utils.checkNotNull(recyclerView);
        this.recyclerView = recyclerView;
    }

    public FilmAdapterView setPresenter(FilmAdapterContract.Presenter presenter) {
        ((MovieAdapter) recyclerView.getAdapter()).setPresenter(presenter);
        return this;
    }

    public FilmAdapterView setEmptyMessage(String emptyMessage) {
        ((MovieAdapter) recyclerView.getAdapter()).setEmptyMessage(emptyMessage);
        return this;
    }

    @Override
    public Observable<RecyclerViewChildAttachStateChangeEvent> createAttachViewObservable() {
        return RxRecyclerView.childAttachStateChangeEvents(recyclerView).asObservable();
    }

    @Override
    public void refreshDataSet(List<Film> dataSet) {
        ((MovieAdapter) recyclerView.getAdapter()).refreshDataset(dataSet);
    }

    public void initialize() {
        ((MovieAdapter) recyclerView.getAdapter()).initialize();
    }

    public void destroy() {
        ((MovieAdapter) recyclerView.getAdapter()).destroy();
        recyclerView = null;
    }
}
