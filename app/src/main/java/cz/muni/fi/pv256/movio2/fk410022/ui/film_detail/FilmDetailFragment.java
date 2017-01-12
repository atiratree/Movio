package cz.muni.fi.pv256.movio2.fk410022.ui.film_detail;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.manager.FavoritesManager;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnSwipeListener;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnSwipeTouchListener;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FavoriteLoader;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FilmLoader;
import cz.muni.fi.pv256.movio2.fk410022.util.ColorUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.image.ImageHelper;

public class FilmDetailFragment extends Fragment implements FilmLoader.FilmListener {
    private static final String TAG = FilmDetailFragment.class.getSimpleName();

    private static final int FILM_LOADER = 0;
    private static final int FAVORITES_LOADER = 1;

    private Context context;

    private View view;

    private FloatingActionButton fab;

    private View.OnTouchListener onSwipeTouchListener;

    private OnSwipeListener onSwipeListener;

    public static FilmDetailFragment newInstance(long movieDbId, OnSwipeListener listener) {
        Bundle args = new Bundle();

        FilmDetailFragment fragment = new FilmDetailFragment();
        args.putLong(FilmLoader.MOVIE_DB_ID_PARAM, movieDbId);
        fragment.setArguments(args);
        fragment.setListener(listener);
        return fragment;
    }

    public void setListener(OnSwipeListener listener) {
        onSwipeListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext().getApplicationContext();

        onSwipeTouchListener = new OnSwipeTouchListener(context, onSwipeListener);
    }

    @Override
    public void onDestroyView() {
        cancelImageLoad();
        // free
        view = null;
        fab = null;
        context = null;
        onSwipeListener = null;
        super.onDestroyView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_film_detail, container, false);

        view.setOnTouchListener(onSwipeTouchListener);
        fab = (FloatingActionButton) view.findViewById(R.id.add_to_favorites);
        fab.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FILM_LOADER, getArguments(), new FilmLoader(this, context));
    }

    @Override
    public void onPause() {
        super.onPause();
        Stream.of(FILM_LOADER, FAVORITES_LOADER)
                .forEach(id -> getLoaderManager().destroyLoader(id));
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(FILM_LOADER, getArguments(), new FilmLoader(this, context));
    }

    @Override
    public void onLoadFinished(final Film entity) {
        renderFilm(entity);
        Bundle bundle = new Bundle();
        bundle.putLong(FavoriteLoader.FILM_ID_PARAM, entity.getId());
        getLoaderManager().initLoader(FAVORITES_LOADER, bundle, new FavoriteLoader(favorite -> {
            if (fab != null) {
                boolean isFavorite = favorite != null && favorite.isFavorite();
                fab.setImageResource(isFavorite ? R.drawable.ic_clear_white_24dp : R.drawable.ic_add_white_24dp);

                int backgroundColor = ContextCompat.getColor(context,
                        isFavorite ? R.color.fab_warning : ColorUtils.getThemeAccentResourceId(context));
                fab.setBackgroundTintList(ColorStateList.valueOf(backgroundColor));

                fab.setOnClickListener(v -> {
                    if (isFavorite) {
                        FavoritesManager.delete(favorite);
                    } else {
                        Favorite newFavorite = new Favorite();
                        newFavorite.setFavorite(true);
                        newFavorite.setFilm(entity);
                        FavoritesManager.save(newFavorite);
                    }
                });
            }
        }, context));
    }

    private void cancelImageLoad() {
        ImageView poster = (ImageView) view.findViewById(R.id.poster);
        ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);

        ImageHelper.cancelDisplay(poster);
        ImageHelper.cancelDisplay(backdrop);
    }

    private void renderFilm(Film film) {
        if (film != null) {
            getActivity().setTitle(film.getTitle());

            ImageView poster = (ImageView) view.findViewById(R.id.poster);
            ImageView backdrop = (ImageView) view.findViewById(R.id.backdrop);

            poster.setContentDescription(getString(R.string.accessibility_poster_of, film.getTitle()));
            backdrop.setContentDescription(getString(R.string.accessibility_backdrop_of, film.getTitle()));

            ImageHelper.displayPoster(film, poster);
            ImageHelper.displayBackdrop(film, backdrop);

            TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(film.getTitle());

            TextView releaseDate = (TextView) view.findViewById(R.id.release_date);
            releaseDate.setText(DateUtils.convertToReadableString(film.getReleaseDate()));

            TextView description = (TextView) view.findViewById(R.id.description);
            description.setText(String.valueOf(film.getDescription()));
            description.setOnTouchListener(onSwipeTouchListener);

            NestedScrollView nestedScrollView = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
            nestedScrollView.setOnTouchListener(onSwipeTouchListener);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.add_to_favorites);
        }
    }
}
