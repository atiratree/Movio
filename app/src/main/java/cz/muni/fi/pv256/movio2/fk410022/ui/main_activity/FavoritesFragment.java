package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FavoriteFilmsLoader;

public class FavoritesFragment extends Fragment implements FavoriteFilmsLoader.FavoriteFilmsListener {
    private static final String TAG = FavoritesFragment.class.getSimpleName();
    private static final int FAVORITE_FILMS_LOADER = 0;

    private Context context;

    private View view;

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FAVORITE_FILMS_LOADER, getArguments(), new FavoriteFilmsLoader(this, context));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
        context = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        getLoaderManager().destroyLoader(FAVORITE_FILMS_LOADER);
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(FAVORITE_FILMS_LOADER, getArguments(), new FavoriteFilmsLoader(this, context));
    }

    @Override
    public void onLoadFinished(List<Film> favorites) {
        initializeRecyclerView(favorites);
    }

    private void initializeRecyclerView(List<Film> favorites) {
        if (view == null) {
            return;
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_favorites);
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

        if (favorites.isEmpty()) {
            recyclerView.setAdapter(new MovieAdapter(getString(R.string.no_favorites), context));
        } else {
            recyclerView.setAdapter(new MovieAdapter(favorites, (MainActivity) getActivity(), context));
        }
    }
}
