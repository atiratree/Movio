package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.film_list_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.RVUtils;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.fixed.FavoriteFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.view.FilmAdapterView;

public class FavoritesFragment extends Fragment {
    private static final String TAG = FavoritesFragment.class.getSimpleName();

    private FilmAdapterView adapterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        adapterView = bindViewToPresenter(view);
        adapterView.initialize();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        adapterView.destroy();
        super.onDestroyView();
    }

    private FilmAdapterView bindViewToPresenter(View view) {
        RecyclerView rv = (RecyclerView) view.findViewById(R.id.recycler_view_favorites);
        FavoriteFilmsAdapterPresenter presenter = new FavoriteFilmsAdapterPresenter();

        RVUtils.initializeMovieRecyclerView(rv, getContext());
        return RVUtils.bind(rv, presenter, getResources().getString(R.string.no_favorites));
    }
}
