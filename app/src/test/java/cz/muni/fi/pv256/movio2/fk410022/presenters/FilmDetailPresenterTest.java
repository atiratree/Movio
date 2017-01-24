package cz.muni.fi.pv256.movio2.fk410022.presenters;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Locale;

import cz.muni.fi.pv256.movio2.fk410022.App;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Favorite;
import cz.muni.fi.pv256.movio2.fk410022.db.model.Film;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxDbObservables;
import cz.muni.fi.pv256.movio2.fk410022.rx.RxStore;
import cz.muni.fi.pv256.movio2.fk410022.rx.message.SelectedFilm;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment.FilmDetailContract;
import cz.muni.fi.pv256.movio2.fk410022.ui.film_fragment.FilmDetailPresenter;
import cz.muni.fi.pv256.movio2.fk410022.utils.DataUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.LastMethodCall;
import cz.muni.fi.pv256.movio2.fk410022.utils.MockUtils;
import cz.muni.fi.pv256.movio2.fk410022.utils.ParamUtils;
import de.schauderhaft.rules.parameterized.Generator;
import de.schauderhaft.rules.parameterized.ListGenerator;
import rx.subjects.BehaviorSubject;
import rx.subjects.SerializedSubject;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {App.class, RxStore.class, RxDbObservables.class, Film.class})
public class FilmDetailPresenterTest {

    @Rule
    public TestName name = new TestName();

    private Boolean isTablet;

    @Rule
    @SuppressWarnings("unchecked")
    public Generator<Boolean[]> parameters = new ListGenerator(ParamUtils.getTruthTable(1));

    private void initializeParams() {
        final Boolean[] params = parameters.value();
        isTablet = params[0];
        System.out.println(String.format(Locale.ENGLISH, "%s: run %d isTablet=%b",
                name.getMethodName(), parameters.runIndex(), isTablet));
    }

    @Mock
    private FilmDetailContract.View view;

    @Mock
    private Film filmOne;

    @Mock
    private Film filmTwo;

    @Mock
    private Favorite filmOneFavorite;

    @Mock
    private Favorite filmTwoFavorite;

    private FilmDetailContract.Presenter presenter;

    @Before
    public void setUp() throws Exception {
        initializeParams();

        DataUtils.setFilmOneValues(filmOne);
        DataUtils.setFavoriteOneValues(filmOneFavorite, filmOne);
        DataUtils.setFavoriteTwoValues(filmTwoFavorite);
        DataUtils.setFilmTwoValues(filmTwo);

        PowerMockito.mockStatic(RxStore.class);

        PowerMockito.mockStatic(App.class);
        PowerMockito.when(App.isTablet()).thenReturn(isTablet);

        PowerMockito.mockStatic(RxDbObservables.class);
        PowerMockito.when(RxDbObservables.createFilmObservable(filmOne.getId())).thenReturn(rx.Observable.just(filmOne));
        PowerMockito.when(RxDbObservables.createFilmObservable(filmTwo.getId())).thenReturn(rx.Observable.just(filmTwo));
        PowerMockito.when(RxDbObservables.createFilmObservable(null)).thenReturn(rx.Observable.just(null));

        PowerMockito.when(RxDbObservables.createFavoriteObservable(filmOne.getId())).thenReturn(rx.Observable.just(filmOneFavorite));
        PowerMockito.when(RxDbObservables.createFavoriteObservable(filmTwo.getId())).thenReturn(rx.Observable.just(null));
    }

    @Test
    public void initializeFilmNoFavorite() throws Exception {
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(
                BehaviorSubject.create(new SelectedFilm(filmTwo.getId()))));

        presenter = new FilmDetailPresenter(view).initialize();

        verify(view, new LastMethodCall()).showMovie(filmTwo);
        verify(view, new LastMethodCall()).showFavorite(false);
    }

    @Test
    public void initializeFilmFavorite() throws Exception {
        initializeDefaultSelectedFilm();

        presenter = new FilmDetailPresenter(view).initialize();

        verify(view, new LastMethodCall()).showMovie(filmOne);
        verify(view, new LastMethodCall()).showFavorite(true);
    }

    @Test
    public void initializeNoFilmNoFavorite() throws Exception {
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(
                BehaviorSubject.create(SelectedFilm.EMPTY)));

        presenter = new FilmDetailPresenter(view).initialize();

        verify(view, never()).showMovie(anyObject());
        verify(view, never()).showFavorite(anyBoolean());
    }

    @Test
    public void testOnSwipeRight() throws Exception {
        initializeDefaultSelectedFilm();
        presenter = new FilmDetailPresenter(view).initialize();

        presenter.onSwipeRight();
        RxStore.SELECTED_FILM.subscribe(film -> Assert.assertEquals(film, SelectedFilm.EMPTY));
    }

    @Test
    public void testToggleFavorite() throws Exception {
        initializeDefaultSelectedFilm();
        presenter = new FilmDetailPresenter(view).initialize();

        presenter.toggleFavorite();
        verify(filmOneFavorite, new LastMethodCall()).delete();
    }

    private void initializeDefaultSelectedFilm() throws Exception {
        MockUtils.setFinalStatic(RxStore.class, "SELECTED_FILM", new SerializedSubject<>(
                BehaviorSubject.create(new SelectedFilm(filmOne.getId()))));
    }
}
