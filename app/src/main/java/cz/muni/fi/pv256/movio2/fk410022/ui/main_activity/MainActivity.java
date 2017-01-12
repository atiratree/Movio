package cz.muni.fi.pv256.movio2.fk410022.ui.main_activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.annimon.stream.Stream;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.network.DownloadService;
import cz.muni.fi.pv256.movio2.fk410022.ui.BaseMenuActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_detail.FilmDetailFragment;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnFilmClickListener;
import cz.muni.fi.pv256.movio2.fk410022.ui.listener.OnSwipeListener;
import cz.muni.fi.pv256.movio2.fk410022.util.Constants;
import cz.muni.fi.pv256.movio2.fk410022.network.FilmListType;

public class MainActivity extends BaseMenuActivity implements OnFilmClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String SHOW_FAVORITES_KEY = "SHOW_FAVORITES_KEY";
    private static final String VISIBLE_FILM_DB_ID_KEY = "VISIBLE_FILM_DB_ID_KEY";
    private static final long NOT_VISIBLE = -1;

    private boolean isTablet;
    private boolean showFavorites;
    private long visibleFilmDbId = NOT_VISIBLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isTablet = getResources().getBoolean(R.bool.isTablet);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            showFavorites = savedInstanceState.getBoolean(SHOW_FAVORITES_KEY);
            visibleFilmDbId = savedInstanceState.getLong(VISIBLE_FILM_DB_ID_KEY);
        }

        renderFragment();
        refreshDetailVisibility();
    }

    private void refreshDetailVisibility() {
        if (isDetailVisible()) {
            Long tmp = visibleFilmDbId;
            visibleFilmDbId = NOT_VISIBLE; // to trigger refresh
            onItemClick(tmp);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SHOW_FAVORITES_KEY, showFavorites);
        outState.putLong(VISIBLE_FILM_DB_ID_KEY, visibleFilmDbId);
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
        menu.findItem(R.id.close_detail).setVisible(isDetailVisible());
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
                showDetail(NOT_VISIBLE);
                renderFragment();
                return true;
            case R.id.show_discover:
                showFavorites = false;
                invalidateOptionsMenu();
                showDetail(NOT_VISIBLE);
                renderFragment();
                return true;
            case R.id.close_detail:
                showDetail(NOT_VISIBLE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void renderFragment() {
        Fragment fragment = showFavorites ? FavoritesFragment.newInstance() : DiscoverFragment.newInstance();
        refreshTitle();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.movies_fragment_container, fragment).commit();
    }

    @Override
    public void onItemClick(Long filmDbId) {
        if (!isTablet) {
            Intent intent = new Intent(this, FilmDetailActivity.class);
            intent.putExtra(FilmDetailActivity.FILM_ID_PARAM, filmDbId);
            startActivity(intent);
        } else {
            FilmDetailFragment detailFragment = FilmDetailFragment.newInstance(filmDbId, new OnSwipeListener() {
                @Override
                public void onSwipeRight() {
                    showDetail(NOT_VISIBLE);
                }
            });
            if (findViewById(R.id.detail_fragment_container) == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.detail_fragment_container, detailFragment).commit();
            } else {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.detail_fragment_container, detailFragment);
                transaction.commit();

                showDetail(filmDbId);
            }
        }
    }

    public void setVisibleFilmDbId(long visibleFilmDbId) {
        if (visibleFilmDbId == NOT_VISIBLE) {
            refreshTitle();
        }
        this.visibleFilmDbId = visibleFilmDbId;
    }

    public void refreshTitle() {
        setTitle(getString(showFavorites ? R.string.favorites : R.string.movies));
    }

    private boolean isDetailVisible() {
        return visibleFilmDbId != NOT_VISIBLE;
    }

    private void showDetail(long filmDbId) {
        // still invisible
        if (!isDetailVisible() && filmDbId == NOT_VISIBLE) {
            return;
        }

        // still visible, but with different id
        if (isDetailVisible() && filmDbId != NOT_VISIBLE) {
            setVisibleFilmDbId(filmDbId);
            return;
        }
        setVisibleFilmDbId(filmDbId);

        ScrollView scrollView = (ScrollView) findViewById(R.id.movies_scroll_view);
        FrameLayout container = (FrameLayout) findViewById(R.id.detail_fragment_container);

        if (container == null || scrollView == null) {
            return;
        }

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, isDetailVisible() ? 2 - interpolatedTime : 1 + interpolatedTime);

                LinearLayout.LayoutParams scrollViewParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, isDetailVisible() ? interpolatedTime : 1 - interpolatedTime);

                container.setLayoutParams(containerParam);
                scrollView.setLayoutParams(scrollViewParam);
            }
        };

        a.setDuration(100);
        container.startAnimation(a);

        if (!isDetailVisible()) {
            getSupportFragmentManager().beginTransaction()
                    .remove(getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container))
                    .commit();
        }

        invalidateOptionsMenu();
    }

    private void downloadMovies(FilmListType type) {
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(Constants.FILM_LIST_TYPE, type);
        startService(intent);
    }
}
