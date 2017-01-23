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

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.sync.SyncIntent;
import cz.muni.fi.pv256.movio2.fk410022.ui.BaseMenuActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.custom.LockableScrollView;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_activity.FilmActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment.FilmDetailFragment;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.film_list_fragments.DiscoverFragment;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.film_list_fragments.FavoritesFragment;

public class MainActivity extends BaseMenuActivity implements MainContract.View {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String FAVORITE_FRAGMENT_TAG = "FAVORITE_FRAGMENT_TAG";
    private static final String DISCOVER_FRAGMENT_TAG = "DISCOVER_FRAGMENT_TAG";

    private static final int DETAIL_ANIMATION_DURATION_MS = 100;

    private Boolean showDiscoverButton;
    private Boolean showFavoritesButton;
    private Boolean showCloseDetailButton;

    private MainContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter(this, new SyncIntent(this)).initialize();
    }

    @Override
    protected void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void startDetailActivity() {
        startActivity(new Intent(this, FilmActivity.class));
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
    }

    @Override
    public void setFavoritesTitle() {
        setTitle(getString(R.string.favorites));
    }

    @Override
    public void setDiscoverTitle() {
        setTitle(getString(R.string.movies));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void refreshMenu(boolean showDiscoverButton, boolean showFavoritesButton, boolean showCloseDetailButton) {
        this.showDiscoverButton = showDiscoverButton;
        this.showFavoritesButton = showFavoritesButton;
        this.showCloseDetailButton = showCloseDetailButton;
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (showDiscoverButton != null) {
            menu.findItem(R.id.show_discover).setVisible(showDiscoverButton);
        }
        if (showFavoritesButton != null) {
            menu.findItem(R.id.show_favorites).setVisible(showFavoritesButton);
        }
        if (showCloseDetailButton != null) {
            menu.findItem(R.id.close_detail).setVisible(showCloseDetailButton);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                presenter.onRefreshClicked();
                return true;
            case R.id.show_favorites:
                presenter.onShowFavoritesClicked();
                return true;
            case R.id.show_discover:
                presenter.onShowDiscoverClicked();
                return true;
            case R.id.close_detail:
                presenter.onCloseDetailClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (getSupportFragmentManager().findFragmentByTag(FAVORITE_FRAGMENT_TAG) != null) {
            setFragmentsScrollable(false);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void replaceFilmLists(boolean showDiscover) {
        if (showDiscover) {
            setFragmentsScrollable(true);
            Fragment favorite = getSupportFragmentManager().findFragmentByTag(FAVORITE_FRAGMENT_TAG);
            Fragment discover = getSupportFragmentManager().findFragmentByTag(DISCOVER_FRAGMENT_TAG);

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            if (favorite != null) {
                transaction.remove(favorite);
            }

            if (discover == null) {
                transaction.add(R.id.movies_fragment_container, new DiscoverFragment(), DISCOVER_FRAGMENT_TAG);
            }

            transaction.commit();
        } else if (getSupportFragmentManager().findFragmentByTag(FAVORITE_FRAGMENT_TAG) == null) {
            setFragmentsScrollable(false);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movies_fragment_container, new FavoritesFragment(), FAVORITE_FRAGMENT_TAG).commit();
        }
    }

    // small hack to have fragments of different heights in the view, if one cannot be scrollable (i.e. FAVORITE_FRAGMENT_TAG)
    private void setFragmentsScrollable(boolean scrollable) {
        final LockableScrollView scrollView = (LockableScrollView) findViewById(R.id.movies_scroll_view);
        if (scrollView == null) {
            return;
        }

        if (!scrollable) {
            scrollView.scrollTo(0, 0);
        }
        scrollView.setScrollable(scrollable);
    }

    @Override
    public void toggleFilmDetail(boolean showDetail) {
        if (showDetail) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, new FilmDetailFragment()).commit();
        }

        final ScrollView scrollView = (ScrollView) findViewById(R.id.movies_scroll_view);
        final FrameLayout container = (FrameLayout) findViewById(R.id.detail_fragment_container);

        if (container == null || scrollView == null) {
            return;
        }

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                LinearLayout.LayoutParams containerParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, showDetail ? 2 - interpolatedTime : 1 + interpolatedTime);

                LinearLayout.LayoutParams scrollViewParam = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT, showDetail ? interpolatedTime : 1 - interpolatedTime);

                container.setLayoutParams(containerParam);
                scrollView.setLayoutParams(scrollViewParam);
            }
        };

        if (!showDetail) {
            setDestroyOnAnimationEndListener(a);
        }

        a.setDuration(DETAIL_ANIMATION_DURATION_MS);
        container.startAnimation(a);
    }

    private void setDestroyOnAnimationEndListener(Animation a) {
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                final Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container);
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }
}
