package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.EnumMap;
import java.util.Map;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListStore;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadService;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.MovieAdapter;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailFragment;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmClickListener;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public class MainActivity extends BaseMenuActivity implements OnFilmClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean isTablet;
    private FilmListStore filmListStore = FilmListStore.INSTANCE;
    private Map<FilmListType, RecyclerView> recyclerMap = new EnumMap<>(FilmListType.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initIndependentTitle();
        initRecyclerViews();
    }

    private void initIndependentTitle() {
        int year = DateUtils.getCurrentYear();
        TextView independentMoviesTitle = (TextView) findViewById(R.id.current_year_popular_independent_movies_title);
        independentMoviesTitle.setText(getString(R.string.current_year_popular_independent_movies, year));
        independentMoviesTitle.setContentDescription(getString(R.string.accessibility_current_year_popular_independent_movies, year));
    }

    private void initRecyclerViews() {
        recyclerMap.put(FilmListType.RECENT_POPULAR_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_popular_movies));
        recyclerMap.put(FilmListType.CURRENT_YEAR_POPULAR_INDEPENDENT_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_current_year_popular_independent_movies));
        recyclerMap.put(FilmListType.HIGHLY_RATED_SCIFI_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_scifi_movies));

        initBroadcasts();

        for (FilmListType type : recyclerMap.keySet()) {
            initializeRecyclerView(type);
        }
    }

    private void initBroadcasts() {
        LocalBroadcastManager.getInstance(this).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                FilmListType type = (FilmListType) intent.getSerializableExtra(Constants.FILM_LIST_TYPE);
                refreshRecyclerView(type);
            }
        }, new IntentFilter(Constants.FILM_LIST_DOWNLOAD_FINISHED));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                for (FilmListType type : recyclerMap.keySet()) {
                    downloadMovies(type);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(Film film) {
        if (!isTablet) {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.FILM_PARAM, film);
            startActivity(intent);
        } else {
            FilmDetailFragment fragment = FilmDetailFragment.newInstance(film);
            if (findViewById(R.id.fragment_container) == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
            } else {
                android.app.FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    private void initializeRecyclerView(FilmListType type) {
        RecyclerView recyclerView = recyclerMap.get(type);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
        boolean hasConnection = Utils.isNetworkAvailable(getApplicationContext());

        if (initialized) {
            refreshRecyclerView(type);
        } else {
            refreshRecyclerView(type, hasConnection ? getString(R.string.downloading) : getString(R.string.no_connection));
        }

        if (!initialized && hasConnection) {
            downloadMovies(type);
        }
    }

    private void downloadMovies(FilmListType type) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(Constants.FILM_LIST_TYPE, type);
        startService(intent);
    }

    private void refreshRecyclerView(FilmListType type) {
        RecyclerView.Adapter adapter = new MovieAdapter(filmListStore.getAll(type), this, this);
        recyclerMap.get(type).setAdapter(adapter);
    }

    private void refreshRecyclerView(FilmListType type, String errorMessage) {
        RecyclerView.Adapter adapter = new MovieAdapter(errorMessage, this);
        recyclerMap.get(type).setAdapter(adapter);
    }
}
