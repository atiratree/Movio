package cz.muni.fi.pv256.movio2.fk410022.ui;

import android.content.Context;
import android.content.Intent;
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
import org.junit.rules.TestName;
import org.junit.runner.RunWith;

import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.R;
import cz.muni.fi.pv256.movio2.fk410022.db.DbUtils;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.db.model.FilmGenre;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.main_activity.MainActivity;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.CustomMatcher;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.MockUtils;
import cz.muni.fi.pv256.movio2.fk410022.ui.utils.UiUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.DateUtils;
import cz.muni.fi.pv256.movio2.fk410022.util.PreferencesUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.ParamUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainAndFilmViewTest {
    @Rule
    public TestName name = new TestName();

    private Boolean isTablet;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Boolean[]> parameters = new ListGenerator(ParamUtils.getTruthTable(1));

    private void initializeParams() {
        final Boolean[] params = parameters.value();
        isTablet = params[0];
        UiUtils.setIsTablet(isTablet);
        System.out.println(String.format(Locale.ENGLISH, "%s: run %d isTablet=%b",
                name.getMethodName(), parameters.runIndex(), isTablet));
    }

    @Rule
    public final ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    @Rule
    public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

    private static Context context = InstrumentationRegistry.getTargetContext();

    private MainActivity mainActivity;

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
    public void init() throws Exception {
        initializeParams();
        rule.getActivity().finish();

        // set defaults - because we repeat test twice for phone/tablet
        MockUtils.setFinalStatic(RxStore.class, "SHOW_DISCOVER", new SerializedSubject<>(BehaviorSubject.create(true)));
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(BehaviorSubject.create(SelectedFilm.EMPTY)));

        // default theme
        new PreferencesUtils(context).setPrefTheme(Theme.APP_THEME);

        // start activity again as phone/tablet
        rule.launchActivity(new Intent(context, MainActivity.class));

        // init film
        film = UiUtils.getAnimatedFilm();
        film.save();
        Stream.of(film.getGenresToPersist()).forEach(genre -> new FilmGenre(film, genre).save());

        mainActivity = rule.getActivity();
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
        onView(allOf(withId(R.id.view_item),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies))))
                .perform(click());
        // check view container is present (phone's in new activity)
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
            onView(withId(R.id.changeTheme)).perform(click());

            onView(allOf(withId(R.id.add_to_favorites),
                    isDescendantOfA(withId(R.id.detail_fragment_container))))
                    .check(matches(CustomMatcher.withBackgroundTintColor(R.color.my_theme_accent)));
        }
    }

    @Test
    public void testAddToFavorites() throws Throwable {
        onView(withId(R.id.close_detail)).check(doesNotExist());

        // click on film
        onView(allOf(withId(R.id.view_item),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies))))
                .perform(click());

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.accent)));

        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withDrawableResource(R.drawable.ic_add_white_24dp)));

        // click on add to favorites
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.fab_warning)));

        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withDrawableResource(R.drawable.ic_clear_white_24dp)));

        // click on remove from favorites
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        // check color and drawable of add to favorite
        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withBackgroundTintColor(R.color.accent)));

        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .check(matches(CustomMatcher.withDrawableResource(R.drawable.ic_add_white_24dp)));

        // close detail
        if (isTablet) {
            onView(withId(R.id.close_detail)).check(matches(isDisplayed()));
            onView(withId(R.id.close_detail)).perform(click());
            onView(withId(R.id.close_detail)).check(doesNotExist());
        } else {
            onView(allOf(withContentDescription("Navigate up"),
                    isDescendantOfA(withId(R.id.action_bar))))
                    .perform(click());
        }
    }

    @Test
    public void testFavoriteList() throws Throwable {
        onView(allOf(withId(R.id.view_item),
                isDescendantOfA(withId(R.id.recycler_view_current_year_popular_animated_movies))))
                .perform(click());

        onView(allOf(withId(R.id.add_to_favorites),
                isDescendantOfA(withId(R.id.detail_fragment_container))))
                .perform(click());

        if (!isTablet) {
            onView(allOf(withContentDescription("Navigate up"),
                    isDescendantOfA(withId(R.id.action_bar))))
                    .perform(click());
        }
        onView(withId(R.id.show_favorites)).perform(click());
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

        // there is a our film in the list
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
        onView(withId(R.id.show_favorites)).perform(click());
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
            onView(allOf(withContentDescription("Navigate up"),
                    isDescendantOfA(withId(R.id.action_bar))))
                    .perform(click());
        }

        // check there are no movies in favorites
        onView(allOf(withId(R.id.view_item_message),
                isDescendantOfA(withId(R.id.recycler_view_favorites))))
                .check(matches(withText(R.string.no_favorites)));
    }
}
