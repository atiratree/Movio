package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FavoriteFilmsLoader;

public class FavoritesFragment extends Fragment implements FavoriteFilmsLoader.FavoriteFilmsListener {
    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private static final int FAVORITE_FILMS_LOADER = 4; // unique between fragments

    private RecyclerView recyclerView;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorites);
        Utils.initializeMovieRecyclerView(recyclerView, getContext());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getLoaderManager().restartLoader(FAVORITE_FILMS_LOADER, getArguments(), new FavoriteFilmsLoader(this, getContext()));
    }

    @Override
    public void onPause() {
        super.onPause();
        getLoaderManager().destroyLoader(FAVORITE_FILMS_LOADER);
    }

    @Override
    public void onLoadFinished(List<Film> favorites) {
        if (getContext() == null) {
            return;
        }
        if (favorites.isEmpty()) {
            recyclerView.setAdapter(new MovieAdapter(getString(R.string.no_favorites), getContext()));
        } else {
            recyclerView.setAdapter(new MovieAdapter(favorites, (MainActivity) getActivity(), getContext()));
        }
    }
}
