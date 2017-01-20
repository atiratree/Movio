package cz.muni.fi.pv256.movio2.fk410022.ui.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.view.FilmAdapterView;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.view.MovieAdapter;

public class RVUtils {

    public static void initializeMovieRecyclerView(RecyclerView recyclerView, Context context) {
        if (recyclerView == null || context == null) {
            return;
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        final PauseOnScrollListener listener = new PauseOnScrollListener(ImageLoader.getInstance(), false, false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                listener.onScrollStateChanged(null, newState);
            }
        });
        recyclerView.setAdapter(new MovieAdapter().setContext(context));
    }

    public static FilmAdapterView bind(RecyclerView recyclerView, FilmAdapterContract.Presenter presenter, String emptyMessage) {
        FilmAdapterView adapterView = new FilmAdapterView(recyclerView)
                .setPresenter(presenter)
                .setEmptyMessage(emptyMessage);

        presenter.setView(adapterView);

        return adapterView;
    }
}
