package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.annimon.stream.Stream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.DbUtils;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.CustomMatcher;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.UiUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.PreferencesUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cz.muni.fi.pv256.movio2.fk410022.ui.utils.EspressoUtils.clickOnMenuItem;
import static cz.muni.fi.pv256.movio2.fk410022.ui.utils.EspressoUtils.menuItemDisplayed;
import static cz.muni.fi.pv256.movio2.fk410022.ui.utils.EspressoUtils.menuItemNotDisplayed;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainViewTest {

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private static Context context = InstrumentationRegistry.getTargetContext();

    private Film film;

    @BeforeClass
    public static void setUp() {
        Configuration dbConfiguration = new Configuration.Builder(context).setDatabaseName(DbUtils.TEST_DB_NAME).create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    @AfterClass
    public static void afterClass() {
        ActiveAndroid.dispose();
        context.deleteDatabase(DbUtils.TEST_DB_NAME);
    }

    @Before
    public void init() throws Throwable {
        // set defaults - because we repeat test twice for phone/tablet
        uiThreadTestRule.runOnUiThread(() -> RxStore.SHOW_DISCOVER.onNext(true));
        uiThreadTestRule.runOnUiThread(() -> RxStore.SELECTED_FILM.onNext(SelectedFilm.EMPTY));
        // default theme
        new PreferencesUtils(context).setPrefTheme(Theme.APP_THEME);
        // init film
        film = UiUtils.getAnimatedFilm();
        film.save();
        Stream.of(film.getGenresToPersist()).forEach(genre -> new FilmGenre(film, genre).save());
    }

    @After
    public void tearDown() {
        new Delete().from(Favorite.class).execute();
        new Delete().from(FilmGenre.class).execute();
        new Delete().from(Film.class).execute();
    }

    @Test
    public void testToggleFavorites() throws Exception {
        // show favorites
        clickOnMenuItem(R.id.show_favorites, R.string.show_favorites);
        menuItemNotDisplayed(R.id.show_favorites, R.string.show_favorites);
        menuItemDisplayed(R.id.show_discover, R.string.show_discover);
        onView(withId(R.id.recycler_view_favorites)).check(matches(isDisplayed()));
        onView(CustomMatcher.childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(R.string.favorites)));

        // show discover
        clickOnMenuItem(R.id.show_discover, R.string.show_discover);
        menuItemNotDisplayed(R.id.show_discover, R.string.show_discover);
        menuItemDisplayed(R.id.show_favorites, R.string.show_favorites);
        onView(withId(R.id.recycler_view_favorites)).check(doesNotExist());
        onView(CustomMatcher.childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(R.string.movies)));
    }

    @Test
    public void testToggleTheme() throws Exception {
        onView(withId(android.R.id.content)).check(matches(CustomMatcher.withBackgroundColor(R.color.background)));
        clickOnMenuItem(R.id.change_theme, R.string.change_theme);
        onView(withId(android.R.id.content)).check(matches(CustomMatcher.withBackgroundColor(R.color.my_theme_background)));
        clickOnMenuItem(R.id.change_theme, R.string.change_theme);
        onView(withId(android.R.id.content)).check(matches(CustomMatcher.withBackgroundColor(R.color.background)));
    }

    @Test
    public void testInitialMenuState() throws Exception {
        onView(CustomMatcher.childAtPosition(withId(R.id.action_bar), 0)).check(matches(withText(R.string.movies)));
        menuItemNotDisplayed(R.id.close_detail, R.string.accessibility_close_detail);
        onView(withId(R.id.refresh)).check(matches(isDisplayed()));
        menuItemDisplayed(R.id.show_favorites, R.string.show_favorites);
        menuItemNotDisplayed(R.id.show_discover, R.string.show_discover);
        menuItemDisplayed(R.id.change_theme, R.string.change_theme);
    }

    @Test
    public void testInitialContent() throws Exception {
        onView(withId(R.id.recycler_view_favorites)).check(doesNotExist());

        onView(withId(R.id.popular_movies_title)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_popular_movies)).check(matches(isDisplayed()));

        onView(withId(R.id.current_year_popular_animated_movies_title)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_current_year_popular_animated_movies)).check(matches(isDisplayed()));

        onView(withId(R.id.scifi_movies_title)).check(matches(isDisplayed()));
        onView(withId(R.id.recycler_view_scifi_movies)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.view_item_message),
                isDescendantOfA(withId(R.id.recycler_view_scifi_movies))))
                .check(matches(withText(R.string.no_movies)));
    }

    @Test
    public void testAnimatedFilmDisplayed() throws Exception {
        onView(allOf(withId(R.id.view_item_title),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies)),
                withText(film.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.view_item_rating),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies)),
                withText(context.getString(R.string.rating_value, film.getRating()))))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.view_item_star),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies))))
                .check(matches(CustomMatcher.hasDrawable()));
    }
}
