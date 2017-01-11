package cz.muni.fi.pv256.movio2.fk410022.ui.film_detail;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annimon.stream.Stream;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FavoritesLoader;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.FilmLoader;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.image.ImageHelper;

public class FilmDetailFragment extends Fragment implements FilmLoader.FilmListener {
    private static final String TAG = FilmDetailFragment.class.getSimpleName();

    private static final int FILM_LOADER = 0;
    private static final int FAVORITES_LOADER = 1;

    private Context context;

    private View view;

    private FloatingActionButton fab;

    public static FilmDetailFragment newInstance(long movieDbId) {
        Bundle args = new Bundle();

        FilmDetailFragment fragment = new FilmDetailFragment();
        args.putLong(FilmLoader.MOVIE_DB_ID_PARAM, movieDbId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext().getApplicationContext();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(FILM_LOADER, getArguments(), new FilmLoader(this, context));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_film_detail, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.add_to_favorites);

        return view;
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
    public void onLoadFinished(final Film film) {
        renderFilm(film);
        Bundle bundle = new Bundle();
        bundle.putLong(FavoritesLoader.FILM_ID_PARAM, film.getId());
        getLoaderManager().initLoader(FAVORITES_LOADER, bundle, new FavoritesLoader(favorite -> {
            if (fab != null) {
                boolean isFavorite = favorite != null && favorite.isFavorite();
                fab.setImageResource(isFavorite ? R.drawable.ic_clear_white_24dp : R.drawable.ic_add_white_24dp);

                fab.setOnClickListener(v -> {
                    if (isFavorite) {
                        favorite.delete();
                    } else {
                        Favorite newFavorite = new Favorite();
                        newFavorite.setFavorite(true);
                        newFavorite.setFilm(film);
                        newFavorite.save();
                    }
                });
            }
        }, context));
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
        }
    }
}
