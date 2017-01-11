package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.annimon.stream.Stream;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadService;
import cz.muni.fi.pv256.movio2.fk410022.store.FilmListStore;
import cz.muni.fi.pv256.movio2.fk410022.store.FilmListType;
import cz.muni.fi.pv256.movio2.fk410022.ui.BaseMenuActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailFragment;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.util.Utils;

public class MainActivity extends BaseMenuActivity implements OnFilmClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SHOW_FAVORITES_KEY = "SHOW_FAVORITES_KEY";

    private boolean isTablet;
    private FilmListStore filmListStore = FilmListStore.INSTANCE;

    private boolean showFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            showFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES_KEY);
        }

        renderFragment();
        downloadMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_FAVORITES_KEY, showFavorites);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.show_discover).setVisible(showFavorites);
        menu.findItem(R.id.show_favorites).setVisible(!showFavorites);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                Stream.of(FilmListType.values()).forEach(this::downloadMovies);
                return true;
            case R.id.show_favorites:
                showFavorites = true;
                invalidateOptionsMenu();
                renderFragment();
                return true;
            case R.id.show_discover:
                showFavorites = false;
                invalidateOptionsMenu();
                renderFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderFragment() {
        Fragment fragment = showFavorites ? FavoritesFragment.newInstance() : DiscoverFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movies_fragment_container, fragment).commit();
    }

    @Override
    public void onItemClick(Film film) {
        if (!isTablet) {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.FILM_ID_PARAM, film.getMovieDbId());
            startActivity(intent);
        } else {
            FilmDetailFragment detailFragment = FilmDetailFragment.newInstance(film.getMovieDbId());
            if (findViewById(R.id.detail_fragment_container) == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_fragment_container, detailFragment).commit();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.detail_fragment_container, detailFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        }
    }

    private void downloadMovies() {
        boolean hasConnection = Utils.isNetworkAvailable(this);

        for (FilmListType type : FilmListType.values()) {
            if (!filmListStore.isInitialized(type) && hasConnection) {
                downloadMovies(type);
            }
        }
    }

    private void downloadMovies(FilmListType type) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(Constants.FILM_LIST_TYPE, type);
        startService(intent);
    }
}
