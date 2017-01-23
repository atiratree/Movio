package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.film_list_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadNextPageIntent;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.RVUtils;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.AnimatedFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.ContinuousFilmAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.PopularFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.presenter.continuous.ScifiFilmsAdapterPresenter;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.view.FilmAdapterView;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;

public class DiscoverFragment extends Fragment {
    private static final String TAG = DiscoverFragment.class.getSimpleName();

    private Collection<FilmAdapterView> adapterViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initIndependentTitle(view);
        adapterViews = bindViewsToPresenters(view);
        Stream.of(adapterViews).forEach(FilmAdapterView::initialize);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Stream.of(adapterViews).forEach(FilmAdapterView::destroy);
        super.onDestroyView();
    }

    private void initIndependentTitle(View view) {
        int year = DateUtils.getCurrentYearAsInt();
        TextView independentMoviesTitle = (TextView) view.findViewById(R.id.current_year_popular_animated_movies_title);
        independentMoviesTitle.setText(getString(R.string.current_year_popular_animated_movies, year));
        independentMoviesTitle.setContentDescription(getString(R.string.accessibility_current_year_popular_animated_movies, year));
    }

    private Collection<FilmAdapterView> bindViewsToPresenters(View view) {
        List<Pair<Integer, ContinuousFilmAdapterPresenter>> viewAndData = new ArrayList<>(3);
        viewAndData.add(new Pair<>(R.id.recycler_view_popular_movies,
                new PopularFilmsAdapterPresenter(new DownloadNextPageIntent(getContext(), FilmListType.RECENT_POPULAR_MOVIES))));
        viewAndData.add(new Pair<>(R.id.recycler_view_current_year_popular_animated_movies,
                new AnimatedFilmsAdapterPresenter(new DownloadNextPageIntent(getContext(), FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES))));
        viewAndData.add(new Pair<>(R.id.recycler_view_scifi_movies,
                new ScifiFilmsAdapterPresenter(new DownloadNextPageIntent(getContext(), FilmListType.HIGHLY_RATED_SCIFI_MOVIES))));

        String noMoviesMessage = getResources().getString(R.string.no_movies);

        return Stream.of(viewAndData)
                .map(p -> {
                    RecyclerView rv = (RecyclerView) view.findViewById(p.first);
                    RVUtils.initializeMovieRecyclerView(rv, getContext());
                    return RVUtils.bind(rv, p.second, noMoviesMessage);
                })
                .collect(Collectors.toList());
    }
}
