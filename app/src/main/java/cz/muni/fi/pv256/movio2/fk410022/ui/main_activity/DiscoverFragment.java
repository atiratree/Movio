package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.AnimatedFilmsLoader;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.EntitiesLoader;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.PopularFilmsLoader;
import cz.muni.fi.pv256.movio2.fk410022.ui.loaders.ScifiFilmsLoader;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class DiscoverFragment extends Fragment {
    private static final String TAG = DiscoverFragment.class.getSimpleName();

    private static final int RECENT_POPULAR_MOVIES_LOADER = 0; // unique between fragments
    private static final int CURRENT_YEAR_POPULAR_ANIMATED_MOVIES_LOADER = 1;
    private static final int HIGHLY_RATED_SCIFI_MOVIES_LOADER = 2;

    private FilmListWrapper[] wrappers;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initIndependentTitle(view);
        wrappers = getWrappers(view);
        Stream.of(wrappers).forEach(w -> Utils.initializeRecyclerView(w.recyclerView, getContext()));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wrappers = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Stream.of(wrappers).forEach(w -> getLoaderManager().restartLoader(w.loaderId, null, w.loader));
    }

    @Override
    public void onPause() {
        super.onPause();
        Stream.of(wrappers).forEach(w -> getLoaderManager().destroyLoader(w.loaderId));
    }

    private void initIndependentTitle(View view) {
        int year = DateUtils.getCurrentYearAsInt();
        TextView independentMoviesTitle = (TextView) view.findViewById(R.id.current_year_popular_animated_movies_title);
        independentMoviesTitle.setText(getString(R.string.current_year_popular_animated_movies, year));
        independentMoviesTitle.setContentDescription(getString(R.string.accessibility_current_year_popular_animated_movies, year));
    }

    private FilmListWrapper[] getWrappers(View view) {

        FilmListWrapper recentPopularWrapper = new FilmListWrapper(RECENT_POPULAR_MOVIES_LOADER,
                (RecyclerView) view.findViewById(R.id.recycler_view_popular_movies));
        recentPopularWrapper.setLoader(new PopularFilmsLoader(recentPopularWrapper, getContext()));

        FilmListWrapper animatedWrapper = new FilmListWrapper(CURRENT_YEAR_POPULAR_ANIMATED_MOVIES_LOADER,
                (RecyclerView) view.findViewById(R.id.recycler_view_current_year_popular_animated_movies));
        animatedWrapper.setLoader(new AnimatedFilmsLoader(animatedWrapper, getContext()));

        FilmListWrapper scifiWrapper = new FilmListWrapper(HIGHLY_RATED_SCIFI_MOVIES_LOADER,
                (RecyclerView) view.findViewById(R.id.recycler_view_scifi_movies));
        scifiWrapper.setLoader(new ScifiFilmsLoader(scifiWrapper, getContext()));

        return new FilmListWrapper[]{
                recentPopularWrapper,
                animatedWrapper,
                scifiWrapper
        };
    }

    private class FilmListWrapper implements EntitiesLoader.EntitiesListener<Film> {
        RecyclerView recyclerView;
        int loaderId;
        EntitiesLoader loader;

        public FilmListWrapper(int loaderId, RecyclerView recyclerView) {
            if (recyclerView == null) {
                throw new IllegalArgumentException("recyclerView cannot be null!");
            }

            this.recyclerView = recyclerView;
            this.loaderId = loaderId;
        }

        public void setLoader(EntitiesLoader loader) {
            this.loader = loader;
        }

        @Override
        public void onLoadFinished(List<Film> films) {
            if (getContext() == null) {
                return;
            }
            if (films.isEmpty()) {
                recyclerView.setAdapter(new MovieAdapter(getString(R.string.no_movies), getContext()));
            } else {
                recyclerView.setAdapter(new MovieAdapter(films, (MainActivity) getActivity(), getContext()));
            }
        }
    }
}
