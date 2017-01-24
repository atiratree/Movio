package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;
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
import cz.muni.fi.pv256.movio2.fk410022.ui.film_activity.FilmActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.CustomMatcher;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.UiUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.PreferencesUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static cz.muni.fi.pv256.movio2.fk410022.ui.utils.EspressoUtils.clickOnMenuItem;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainAndFilmViewTest {

    private static Boolean isTablet;

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private static Context context = InstrumentationRegistry.getTargetContext();

    private Film film;

    @BeforeClass
    public static void setUp() {
        isTablet = App.isTablet();

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
    public void testFilmDetailAndTheme() throws Throwable {
        // click on film
        if (!isTablet) {
            Intents.init();
        }
        onView(withId(R.id.recycler_view_current_year_popular_animated_movies))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        if (!isTablet) {
            // phone starts new activity
            intended(hasComponent(FilmActivity.class.getName()));
        }
        // check view container is present
        onView(withId(R.id.detail_fragment_container)).check(matches(isDisplayed()));

        // check content is set
        onView(allOf(withId(R.id.title),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(film.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.description),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(film.getDescription())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.release_date),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(DateUtils.convertToReadableString(film.getReleaseDate()))))
                .check(matches(isDisplayed()));

        if (isTablet) {
            // close detail button is showed
            onView(withId(R.id.close_detail)).check(matches(isDisplayed()));
        }

        // test color of add button
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.accent)));

        if (isTablet) {
            clickOnMenuItem(R.id.change_theme, R.string.change_theme);

            onView(allOf(withId(R.id.add_to_favorites),
                    isDescendantOfA(withId(R.id.detail_fragment_container))))
                    .check(matches(CustomMatcher.withBackgroundTintColor(R.color.my_theme_accent)));
        }
    }

    @Test
    public void testAddToFavorites() throws Throwable {
        onView(withId(R.id.close_detail)).check(doesNotExist());

        // click on film
        onView(withId(R.id.recycler_view_current_year_popular_animated_movies))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.accent)));

        // click on add to favorites
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.fab_warning)));

        // click on remove from favorites
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.accent)));

        // close detail
        if (isTablet) {
            onView(withId(R.id.close_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.close_detail)).perform(click());
            onView(withId(R.id.close_detail)).check(doesNotExist());
        } else {
            Espresso.pressBack();
        }
    }

    @Test
    public void testFavoriteList() throws Throwable {
        onView(withId(R.id.recycler_view_current_year_popular_animated_movies))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        if (!isTablet) {
            Espresso.pressBack();
        }
        clickOnMenuItem(R.id.show_favorites, R.string.show_favorites);
        // end initialization

        // close favorited detail
        onView(withId(R.id.close_detail)).check(doesNotExist());

        // detail is closed
        onView(allOf(withId(R.id.title),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(film.getTitle())))
                .check(doesNotExist());

        // navigated to favorites
        onView(withId(R.id.recycler_view_favorites)).check(matches(isDisplayed()));

        // there is our film in the list
        onView(allOf(withId(R.id.view_item_title),
                isDescendantOfA(withId(R.id.recycler_view_favorites)),
                withText(film.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.view_item_rating),
                isDescendantOfA(withId(R.id.recycler_view_favorites)),
                withText(context.getString(R.string.rating_value, film.getRating()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testRemoveFavoriteFromList() throws Throwable {
        final Favorite favorite = new Favorite();
        favorite.setFavorite(true);
        favorite.setFilm(film);
        favorite.save();
        // end initialization; this is tested in testAddToFavorites()

        // go to favorites and click on a movie
        clickOnMenuItem(R.id.show_favorites, R.string.show_favorites);
        onView(allOf(withId(R.id.view_item),
                isDescendantOfA(withId(R.id.recycler_view_favorites))))
                .perform(click());

        // check film detail
        onView(allOf(withId(R.id.title),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(film.getTitle())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.description),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(film.getDescription())))
                .check(matches(isDisplayed()));

        onView(allOf(withId(R.id.release_date),
                isDescendantOfA(withId(R.id.detail_fragment_container)),
                withText(DateUtils.convertToReadableString(film.getReleaseDate()))))
                .check(matches(isDisplayed()));

        // remove from favorites
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        if (!isTablet) {
            // go back to favorites
            Espresso.pressBack();
        }

        // check there are no movies in favorites
        onView(allOf(withId(R.id.view_item_message),
                isDescendantOfA(withId(R.id.recycler_view_favorites))))
                .check(matches(withText(R.string.no_favorites)));
    }
}
