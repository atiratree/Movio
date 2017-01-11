package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.annimon.stream.Stream;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.EnumMap;
import java.util.Map;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.store.FilmListStore;
import cz.muni.fi.pv256.movio2.fk410022.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public class DiscoverFragment extends Fragment {
    private static final String TAG = DiscoverFragment.class.getSimpleName();

    private FilmListStore filmListStore = FilmListStore.INSTANCE;
    private Map<FilmListType, RecyclerView> recyclerMap = new EnumMap<>(FilmListType.class);
    private Context context;

    public static DiscoverFragment newInstance() {
        return new DiscoverFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        initIndependentTitle(view);
        initRecyclerViews(view);
        return view;
    }

    private void initIndependentTitle(View view) {
        int year = DateUtils.getCurrentYear();
        TextView independentMoviesTitle = (TextView) view.findViewById(R.id.current_year_popular_animated_movies_title);
        independentMoviesTitle.setText(getString(R.string.current_year_popular_animated_movies, year));
        independentMoviesTitle.setContentDescription(getString(R.string.accessibility_current_year_popular_animated_movies, year));
    }

    private void initRecyclerViews(View view) {
        recyclerMap.put(FilmListType.RECENT_POPULAR_MOVIES, (RecyclerView) view.findViewById(R.id.recycler_view_popular_movies));
        recyclerMap.put(FilmListType.CURRENT_YEAR_POPULAR_ANIMATED_MOVIES, (RecyclerView) view.findViewById(R.id.recycler_view_current_year_popular_animated_movies));
        recyclerMap.put(FilmListType.HIGHLY_RATED_SCIFI_MOVIES, (RecyclerView) view.findViewById(R.id.recycler_view_scifi_movies));

        initBroadcasts();

        Stream.of(recyclerMap.keySet()).forEach(this::initializeRecyclerView);
    }

    private void initBroadcasts() {
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FilmListType type = (FilmListType) intent.getSerializableExtra(Constants.FILM_LIST_TYPE);
                refreshRecyclerView(type);
            }
        }, new IntentFilter(Constants.FILM_LIST_DOWNLOAD_FINISHED));
    }

    private void initializeRecyclerView(FilmListType type) {
        RecyclerView recyclerView = recyclerMap.get(type);
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

        boolean initialized = filmListStore.isInitialized(type);
        boolean hasConnection = Utils.isNetworkAvailable(context);

        if (initialized) {
            refreshRecyclerView(type);
        } else {
            refreshRecyclerView(type, hasConnection ? getString(R.string.downloading) : getString(R.string.no_connection));
        }
    }

    private void refreshRecyclerView(FilmListType type) {
        RecyclerView.Adapter adapter = new MovieAdapter(filmListStore.getAll(type), (MainActivity) getActivity(), context);
        recyclerMap.get(type).setAdapter(adapter);
    }

    private void refreshRecyclerView(FilmListType type, String errorMessage) {
        RecyclerView.Adapter adapter = new MovieAdapter(errorMessage, context);
        recyclerMap.get(type).setAdapter(adapter);
    }
}
