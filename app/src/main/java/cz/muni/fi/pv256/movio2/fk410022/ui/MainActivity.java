package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.util.HashMap;
import java.util.Map;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListStore;
import cz.muni.fi.pv256.movio2.fk410022.model.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadFilmListTask;
import cz.muni.fi.pv256.movio2.fk410022.ui.adapter.MovieAdapter;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailFragment;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmClickListener;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmListDownload;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public class MainActivity extends MainMenuActivity implements OnFilmClickListener, OnFilmListDownload {
    private static final String TAG = MainActivity.class.getSimpleName();

    private boolean isTablet;
    private FilmListStore filmListStore = FilmListStore.INSTANCE;
    private Map<FilmListType, RecyclerView> recyclerMap = new HashMap<>(3);
    private Map<FilmListType, DownloadFilmListTask.Cancelable> tasks = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView) findViewById(R.id.current_year_popular_independent_title))
                .setText(getString(R.string.current_year_popular_independent_movies, Utils.getCurrentYear()));

        recyclerMap.put(FilmListType.RECENT_POPULAR_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_popular_movies));
        recyclerMap.put(FilmListType.CURRENT_YEAR_POPULAR_INDEPENDENT_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_current_year_popular_independent_movies));
        recyclerMap.put(FilmListType.HIGHLY_RATED_SCIFI_MOVIES, (RecyclerView) findViewById(R.id.recycler_view_popular_shows));

        initializeRecyclerView(FilmListType.RECENT_POPULAR_MOVIES);
        initializeRecyclerView(FilmListType.CURRENT_YEAR_POPULAR_INDEPENDENT_MOVIES);
        initializeRecyclerView(FilmListType.HIGHLY_RATED_SCIFI_MOVIES);
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
            new DownloadFilmListTask(this, type).execute();
        }
    }

    private void refreshRecyclerView(FilmListType type) {
        RecyclerView.Adapter adapter = new MovieAdapter(filmListStore.getAll(type), type, this, this);
        recyclerMap.get(type).setAdapter(adapter);
    }

    private void refreshRecyclerView(FilmListType type, String errorMessage) {
        RecyclerView.Adapter adapter = new MovieAdapter(errorMessage, type, this);
        recyclerMap.get(type).setAdapter(adapter);
    }

    @Override
    public void onStartDownload(FilmListType type, DownloadFilmListTask.Cancelable cancelable) {
        tasks.put(type, cancelable);
    }

    @Override
    public void onFinishedDownload(FilmListType type) {
        tasks.remove(type);
        refreshRecyclerView(type);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (DownloadFilmListTask.Cancelable cancelable : tasks.values()) {
            cancelable.cancel();
        }
    }
}
